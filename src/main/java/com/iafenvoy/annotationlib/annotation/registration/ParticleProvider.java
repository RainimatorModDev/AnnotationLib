package com.iafenvoy.annotationlib.annotation.registration;

import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;

import java.lang.annotation.*;

/**
 * <p>This attribute is used to mark the particle factory build method.
 * This method should be placed in the particle class and be static.
 * This method must have a {@link SpriteProvider} parameter and return a {@link ParticleFactory}.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ParticleProvider {
}
