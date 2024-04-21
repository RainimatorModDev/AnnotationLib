package com.iafenvoy.annotationlib.annotation.registration;

import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;

import java.lang.annotation.*;

/**
 * <p>This attribute is used to mark the particle factory build method.
 * This method should be placed in the particle class and be static.
 * This method must have a {@link SpriteProvider} parameter and return a {@link ParticleFactory}.</p>
 * <p>For example, you are registering an EntityType&lt;MyEntity&gt;.
 * Registration system will find the first method with correct signature and this annotation in MyEntity class.
 * Then it will use it to build a Attribute and register it.</p>
 * <p>If no matched method found, The registration system will not register default attribute.
 * You need to register it by yourself.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@Deprecated
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ParticleProvider {
}
