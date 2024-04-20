package com.iafenvoy.annotationlib.registry;

import com.iafenvoy.annotationlib.AnnotationLib;
import com.iafenvoy.annotationlib.annotation.*;
import com.iafenvoy.annotationlib.api.IAnnotationLibEntryPoint;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

public class RegistrationManager {
    public static <T> void register(Registry<T> registry, String modId, String name, T obj) {
        register(registry, new Identifier(modId, name), obj);
    }

    public static <T> void register(Registry<T> registry, Identifier id, T obj) {
        Registry.register(registry, id, obj);
    }

    //return null if there is no need to register
    private static String getName(Field field, boolean autoRegister) {
        ObjectReg objectReg = field.getAnnotation(ObjectReg.class);
        if (!autoRegister && objectReg == null) return null;
        String name = field.getName();
        if (objectReg != null && !objectReg.value().isBlank())
            name = objectReg.value();
        return name.toLowerCase();
    }

    private static void tryPutGroup(Field field) {
        Group group = field.getAnnotation(Group.class);
        if (group == null) return;
        RegistrationGroup.add(group.value(), field);
    }

    private static boolean tryPutLink(Field field) {
        Link link = field.getAnnotation(Link.class);
        if (link == null) return false;
        RegistrationLink.link(link.type(), link.target(), field);
        return true;
    }

    public static void register(Class<?> clazz) {
        ModId modIdAnnotation = clazz.getAnnotation(ModId.class);
        if (modIdAnnotation != null) {//Has @ModId
            boolean linkableChanged = false;
            String modId = modIdAnnotation.value();
            Field[] fields = clazz.getDeclaredFields();
            boolean autoRegister = clazz.getAnnotation(RegisterAll.class) != null;
            for (Field field : fields) {
                int modifier = field.getModifiers();
                if (!Modifier.isStatic(modifier))//Ignore non-static field
                    continue;
                //@Group
                tryPutGroup(field);
                //@Link
                if (tryPutLink(field)) continue;
                String name = getName(field, autoRegister);
                if (name == null) {
                    AnnotationLib.LOGGER.warn(String.format("Field %s in class %s is not marked as requiring registration, game may crash with this.", field.getName(), clazz.getName()));
                    continue;
                }
                try {
                    Object obj = field.get(null);
                    if (Item.class.isAssignableFrom(field.getType()))
                        register(Registries.ITEM, modId, name, (Item) obj);
                    if (Block.class.isAssignableFrom(field.getType())) {
                        linkableChanged = true;
                        register(Registries.BLOCK, modId, name, (Block) obj);
                    }
                    if (EntityType.class.isAssignableFrom(field.getType())) {
                        register(Registries.ENTITY_TYPE, modId, name, (EntityType<?>) obj);
                        RegistrationHelper.processEntity(clazz, field, obj);
                    }
                    if (SoundEvent.class.isAssignableFrom(field.getType()))
                        register(Registries.SOUND_EVENT, modId, name, (SoundEvent) obj);
                    if (StatusEffect.class.isAssignableFrom(field.getType()))
                        register(Registries.STATUS_EFFECT, modId, name, (StatusEffect) obj);
                    if (ItemGroup.class.isAssignableFrom(field.getType()))
                        register(Registries.ITEM_GROUP, modId, name, (ItemGroup) obj);
                } catch (IllegalAccessException e) {
                    AnnotationLib.LOGGER.error("Fail to get object: " + field.getName(), e);
                }
            }
            //After we complete all register, check non-linked objects
            if (linkableChanged)
                RegistrationLink.findIfCanLink();
            //find @CallbackHandler
            for (Method method : clazz.getMethods()) {
                CallbackHandler callback = method.getAnnotation(CallbackHandler.class);
                if (Modifier.isStatic(method.getModifiers()) && callback != null)
                    if (method.getParameterCount() == 0 && method.getReturnType() == Void.TYPE) {
                        if (Modifier.isPrivate(method.getModifiers()))
                            method.setAccessible(true);
                        try {
                            method.invoke(null);
                        } catch (InvocationTargetException | IllegalAccessException e) {
                            AnnotationLib.LOGGER.error("Fail to invoke method: " + method.getName(), e);
                        }
                    } else
                        AnnotationLib.LOGGER.warn(String.format("Method %s in class %s has a wrong signature, see @CallbackHandler for more info.", method.getName(), clazz.getName()));
            }
        }
    }

    public static void registerDefault() {
        AnnotationLib.LOGGER.info("Start to run annotation powered registrations.");
        List<IAnnotationLibEntryPoint> entrypoints = FabricLoader.getInstance().getEntrypoints(AnnotationLib.MOD_ID, IAnnotationLibEntryPoint.class);
        for (IAnnotationLibEntryPoint entrypoint : entrypoints)
            register(entrypoint.getClass());
    }
}
