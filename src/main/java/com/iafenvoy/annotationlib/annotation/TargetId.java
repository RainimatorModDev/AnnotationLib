package com.iafenvoy.annotationlib.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>The replacement of Identifier in annotation.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface TargetId {
    /**
     * The Namespace
     *
     * @return {@link String }
     */
    String namespace() default "";

    /**
     * The Path
     *
     * @return {@link String }
     */
    String value();
}
