package com.iafenvoy.annotationlib.annotation;

import java.lang.annotation.*;

/**
 * <p>If any annotation has this, <b>DO NOT USE</b> it anyway.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface UnusedYet {
}
