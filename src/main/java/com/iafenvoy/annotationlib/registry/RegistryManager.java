package com.iafenvoy.annotationlib.registry;

import com.iafenvoy.annotationlib.AnnotationLib;
import com.iafenvoy.annotationlib.annotation.ModId;
import com.iafenvoy.annotationlib.annotation.ObjectReg;
import com.iafenvoy.annotationlib.annotation.RegisterAll;
import com.iafenvoy.annotationlib.api.IAnnotationLibEntryPoint;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public class RegistryManager {
    private static <T> void register(Registry<T> registry, String modId, String name, T obj) {
        register(registry, new Identifier(modId, name), obj);
    }

    private static <T> void register(Registry<T> registry, Identifier id, T obj) {
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

    public static void register(Class<?> clazz) {
        ModId modIdAnnotation = clazz.getAnnotation(ModId.class);
        if (modIdAnnotation != null) {//Has @ModId
            String modId = modIdAnnotation.value();
            Field[] fields = clazz.getDeclaredFields();
            boolean autoRegister = clazz.getAnnotation(RegisterAll.class) != null;
            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers()) || !Modifier.isPublic(field.getModifiers()))
                    continue;//Ignore non-static field
                String name = getName(field, autoRegister);
                if (name == null) {
                    AnnotationLib.LOGGER.warn(String.format("Field %s in class %s is not marked as requiring registration, game may crash with this.", field.getName(), clazz.getName()));
                    continue;
                }
                try {
                    Object obj = field.get(null);
                    if (Item.class.isAssignableFrom(field.getType()))
                        register(Registries.ITEM, modId, name, (Item) obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void registerDefault() {
        AnnotationLib.LOGGER.info("Start to run annotation powered tasks");
        List<IAnnotationLibEntryPoint> entrypoints = FabricLoader.getInstance().getEntrypoints(AnnotationLib.MOD_ID, IAnnotationLibEntryPoint.class);
        for (IAnnotationLibEntryPoint entrypoint : entrypoints)
            register(entrypoint.getClass());
    }
}
