package com.iafenvoy.annotationlib.annotation.registration;

import com.iafenvoy.annotationlib.annotation.UnusedYet;

import java.lang.annotation.*;

/**
 * <p>Register this feature with given properties.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@UnusedYet
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface FeatureReg {
}
