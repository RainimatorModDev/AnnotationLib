package com.iafenvoy.annotationlib.annotation.registration;

import com.iafenvoy.annotationlib.api.AnnotationApi;

import java.lang.annotation.*;

/**
 * <p>Register this hotkey into network.</p>
 * <p>Use <b>{@link AnnotationApi}.registerHotkeyHandler</b> to register your handler.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface AutoNetwork {
    /**
     * The register ID.
     *
     * @return {@link String }
     */
    String value() default "";
}
