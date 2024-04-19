package com.iafenvoy.annotationlib.annotation;

import net.minecraft.registry.Registry;

import java.lang.annotation.*;

/**
 * <p>Automatically register all static fields in this class.</p>
 * <p>Processor will automatically detect which {@link Registry} should be used.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RegisterAll {
}
