package com.iafenvoy.annotationlib.annotation;

import com.iafenvoy.annotationlib.util.IAnnotationLibEntryPoint;

import java.lang.annotation.*;

/**
 * <p>Indicate this is a processor and provide filter.</p>
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface AnnotationProcessor {
    /**
     * The class filter. Recommended to be an interface.
     * @return {@link Class }<? extends {@link IAnnotationLibEntryPoint }>
     */
    Class<? extends IAnnotationLibEntryPoint> value();
}
