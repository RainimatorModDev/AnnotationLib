package com.iafenvoy.annotationlib.registry;

import com.iafenvoy.annotationlib.AnnotationLib;
import com.iafenvoy.annotationlib.annotation.registration.AttributeBuilder;
import com.iafenvoy.annotationlib.annotation.registration.AutoNetwork;
import com.iafenvoy.annotationlib.annotation.registration.ParticleProvider;
import com.iafenvoy.annotationlib.network.ClientHotkeyNetwork;
import com.iafenvoy.annotationlib.util.MethodHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

import java.lang.reflect.*;

public class RegistrationHelper {
    @SuppressWarnings("unchecked")
    public static void processEntity(Class<?> clazz, Field field, Object obj) {
        if (field.getGenericType() instanceof ParameterizedType paramType && paramType.getActualTypeArguments()[0] instanceof Class<?> entityClass && LivingEntity.class.isAssignableFrom(entityClass)) {
            EntityType<? extends LivingEntity> entityType = (EntityType<? extends LivingEntity>) obj;
            for (Method method : entityClass.getMethods()) {
                AttributeBuilder attributeBuilder = method.getAnnotation(AttributeBuilder.class);
                if (Modifier.isStatic(method.getModifiers()) && attributeBuilder != null) {
                    if (MethodHelper.check(method, DefaultAttributeContainer.Builder.class)) {
                        if (Modifier.isPrivate(method.getModifiers()))
                            method.setAccessible(true);
                        try {
                            DefaultAttributeContainer.Builder builder = (DefaultAttributeContainer.Builder) method.invoke(null);
                            FabricDefaultAttributeRegistry.register(entityType, builder);
                        } catch (InvocationTargetException | IllegalAccessException e) {
                            AnnotationLib.LOGGER.error("Fail to invoke method: " + method.getName(), e);
                        }
                    } else
                        AnnotationLib.LOGGER.warn(String.format("Method %s in class %s has a wrong signature, see @AttributeBuilder for more info.", method.getName(), clazz.getName()));
                }
            }
        } else
            AnnotationLib.LOGGER.warn(String.format("Field %s in class %s is a EntityType but cannot resolve entity class to LivingEntity. You can ignore this message when there's a ProjectileEntity.", field.getName(), clazz.getName()));
    }

    @SuppressWarnings("unchecked")
    public static void processParticle(Class<?> clazz, Object obj) {
        for (Method method : clazz.getMethods()) {
            ParticleProvider particleProvider = method.getAnnotation(ParticleProvider.class);
            if (Modifier.isStatic(method.getModifiers()) && particleProvider != null) {
                if (MethodHelper.check(method, ParticleFactory.class, SpriteProvider.class)) {
                    if (Modifier.isPrivate(method.getModifiers()))
                        method.setAccessible(true);
                    ParticleFactoryRegistry.getInstance().register((ParticleType<ParticleEffect>) obj, spiritSet -> {
                        try {
                            return (ParticleFactory<ParticleEffect>) method.invoke(null, spiritSet);
                        } catch (InvocationTargetException | IllegalAccessException e) {
                            AnnotationLib.LOGGER.error("Fail to invoke method: " + method.getName(), e);
                            throw new RuntimeException(e);
                        }
                    });
                } else
                    AnnotationLib.LOGGER.warn(String.format("Method %s in class %s has a wrong signature, see @AttributeBuilder for more info.", method.getName(), clazz.getName()));
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public static void processKeybindings(Field field, Object obj) {
        AutoNetwork autoNetwork = field.getAnnotation(AutoNetwork.class);
        if (autoNetwork != null) {
            String id = autoNetwork.value();
            if (id.isBlank())
                id = field.getName().toLowerCase();
            ClientHotkeyNetwork.register(id, (KeyBinding) obj);
        }
    }
}
