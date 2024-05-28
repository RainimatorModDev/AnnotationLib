package com.iafenvoy.annotationlib.annotation;

import java.lang.annotation.*;

/**
 * <p>Provide ModId to registration system.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ModId {
    /**
     * The Mod ID.
     *
     * @return {@link String }
     */
    String value();
}
