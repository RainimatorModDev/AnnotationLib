package com.iafenvoy.annotationlib.annotation;

import java.lang.annotation.*;

/**
 * <p>Register this feature with given properties.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@Deprecated
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface FeatureReg {
}
