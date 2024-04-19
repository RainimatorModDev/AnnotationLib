package com.iafenvoy.annotationlib.annotation;

import java.lang.annotation.*;

/**
 * <p>Tell the registry system to register this class and used to get the Mod ID.</p>
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
     * The Mod ID
     *
     * @return {@link String }
     */
    String value();
}
