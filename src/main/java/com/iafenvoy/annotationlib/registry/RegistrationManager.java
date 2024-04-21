package com.iafenvoy.annotationlib.registry;

import com.iafenvoy.annotationlib.AnnotationLib;
import com.iafenvoy.annotationlib.annotation.*;
import com.iafenvoy.annotationlib.annotation.registration.*;
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
        ItemReg itemReg = field.getAnnotation(ItemReg.class);
        if (!autoRegister && objectReg == null && itemReg == null) return null;
        String name = field.getName();
        if (objectReg != null && !objectReg.value().isBlank())
            name = objectReg.value();
        if (itemReg != null && !itemReg.value().isBlank())
            name = itemReg.value();
        return name.toLowerCase();
    }

    private static void tryPutGroup(String modId, Field field) {
        Group group = field.getAnnotation(Group.class);
        ItemReg itemReg = field.getAnnotation(ItemReg.class);
        if (group == null && itemReg == null) return;
        TargetId targetId = group == null ? itemReg.group() : group.value();
        RegistrationGroup.add(new Identifier(targetId.namespace().isBlank() ? modId : targetId.namespace(), targetId.value()), field);
    }

    private static boolean tryPutLink(String modId, Field field) {
        Link link = field.getAnnotation(Link.class);
        if (link == null) return false;
        List<TargetId> targets = link.target().value().isBlank() ? List.of(link.targets()) : List.of(link.target());
        RegistrationLink.link(modId, link.type(), targets, field);
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
                tryPutGroup(modId, field);
                //@Link
                if (tryPutLink(modId, field)) continue;
                String name = getName(field, autoRegister);
                if (name == null) {
                    AnnotationLib.LOGGER.warn(String.format("Field %s in class %s is not marked as requiring registration, game may crash with this.", field.getName(), clazz.getName()));
                    continue;
                }
                try {
                    Object obj = field.get(null);
                    if (Item.class.isAssignableFrom(field.getType()))
                        register(Registries.ITEM, modId, name, (Item) obj);
                    else if (Block.class.isAssignableFrom(field.getType())) {
                        linkableChanged = true;
                        register(Registries.BLOCK, modId, name, (Block) obj);
                    } else if (EntityType.class.isAssignableFrom(field.getType())) {
                        register(Registries.ENTITY_TYPE, modId, name, (EntityType<?>) obj);
                        RegistrationHelper.processEntity(clazz, field, obj);
                    } else if (SoundEvent.class.isAssignableFrom(field.getType()))
                        register(Registries.SOUND_EVENT, modId, name, (SoundEvent) obj);
                    else if (StatusEffect.class.isAssignableFrom(field.getType()))
                        register(Registries.STATUS_EFFECT, modId, name, (StatusEffect) obj);
                    else if (ItemGroup.class.isAssignableFrom(field.getType()))
                        register(Registries.ITEM_GROUP, modId, name, (ItemGroup) obj);
                    else
                        AnnotationLib.LOGGER.error("Cannot register this item since this type is not implemented yet: " + field.getName());
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
}
