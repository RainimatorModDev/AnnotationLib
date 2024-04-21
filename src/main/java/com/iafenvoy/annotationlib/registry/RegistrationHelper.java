package com.iafenvoy.annotationlib.registry;

import com.iafenvoy.annotationlib.AnnotationLib;
import com.iafenvoy.annotationlib.annotation.registration.AttributeBuilder;
import com.iafenvoy.annotationlib.annotation.registration.ParticleProvider;
import io.netty.util.internal.UnstableApi;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

import java.lang.reflect.*;

public class RegistrationHelper {
    public static void processEntity(Class<?> clazz, Field field, Object obj) {
        if (field.getGenericType() instanceof ParameterizedType paramType && paramType.getActualTypeArguments()[0] instanceof Class<?> entityClass && LivingEntity.class.isAssignableFrom(entityClass)) {
            @SuppressWarnings("unchecked")
            EntityType<? extends LivingEntity> entityType = (EntityType<? extends LivingEntity>) obj;
            for (Method method : entityClass.getMethods()) {
                AttributeBuilder attributeBuilder = method.getAnnotation(AttributeBuilder.class);
                if (Modifier.isStatic(method.getModifiers()) && attributeBuilder != null) {
                    if (method.getParameterCount() == 0 && DefaultAttributeContainer.Builder.class.isAssignableFrom(method.getReturnType())) {
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

    @UnstableApi
    @Deprecated
    @SuppressWarnings("unchecked")
    public static <T extends ParticleEffect> void processParticle(Class<?> clazz, Field field, Object obj) {
        if (field.getGenericType() instanceof ParameterizedType paramType && paramType.getActualTypeArguments()[0] instanceof Class<?> particleClass && Particle.class.isAssignableFrom(particleClass)) {
            ParticleType<T> particleType = (ParticleType<T>) obj;
            for (Method method : particleClass.getMethods()) {
                ParticleProvider attributeBuilder = method.getAnnotation(ParticleProvider.class);
                if (Modifier.isStatic(method.getModifiers()) && attributeBuilder != null) {
                    if (method.getParameterCount() == 0 && ParticleFactory.class.isAssignableFrom(method.getReturnType())) {
                        if (Modifier.isPrivate(method.getModifiers()))
                            method.setAccessible(true);
                        ParticleFactoryRegistry.getInstance().register(particleType, context -> {
                            try {
                                return (ParticleFactory<T>) method.invoke(null, context);
                            } catch (InvocationTargetException | IllegalAccessException e) {
                                AnnotationLib.LOGGER.error("Fail to invoke method: " + method.getName(), e);
                            }
                            return null;
                        });
                    } else
                        AnnotationLib.LOGGER.warn(String.format("Method %s in class %s has a wrong signature, see @AttributeBuilder for more info.", method.getName(), clazz.getName()));
                }
            }
        } else
            AnnotationLib.LOGGER.warn(String.format("Field %s in class %s is a ParticleType but cannot resolve particle factory.", field.getName(), clazz.getName()));
    }
}
