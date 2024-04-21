package com.iafenvoy.annotationlib.annotation.registration;

import net.minecraft.registry.Registry;

import java.lang.annotation.*;

/**
 * <p>Register this field with given ID.</p>
 * <p>Field name will be used if no ID provided.</p>
 * <p>Processor will automatically detect which {@link Registry} should be used.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ObjectReg {
    /**
     * <p>The register ID</p>
     *
     * @return {@link String }
     */
    String value() default "";
}
