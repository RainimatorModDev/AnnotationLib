package com.iafenvoy.annotationlib.annotation.registration;

import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.particle.ParticleEffect;

import java.lang.annotation.*;

/**
 * <p>Register this particle with given provider class.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ParticleReg {
    /**
     * The particle provider.
     * @return {@link Class }<? extends {@link ParticleFactory }<{@link ParticleEffect }>>
     */
    Class<? extends ParticleFactory<ParticleEffect>> value();

    /**
     * The register id. Can be blank.
     * @return {@link String }
     */
    String name() default "";
}
