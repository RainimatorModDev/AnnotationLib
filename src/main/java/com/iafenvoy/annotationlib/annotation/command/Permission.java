package com.iafenvoy.annotationlib.annotation.command;

import java.lang.annotation.*;

/**
 * <p>Indicate the permission to execute this command.</p>
 * <p>If you use it on method, the value field {@link CommandProcessor} shouldn't be blank.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Permission {
    /**
     * The permission value.
     *
     * @return int
     */
    int value();
}
