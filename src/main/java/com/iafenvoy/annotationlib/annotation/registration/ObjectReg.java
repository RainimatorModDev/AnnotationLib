package com.iafenvoy.annotationlib.annotation.registration;

import java.lang.annotation.*;
import net.minecraft.util.registry.Registry;

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
     * The register ID.
     *
     * @return {@link String }
     */
    String value() default "";
}
