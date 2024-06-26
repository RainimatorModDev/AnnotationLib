package com.iafenvoy.annotationlib.annotation;

import net.fabricmc.api.EnvType;

import java.lang.annotation.*;

/**
 * <p>Tell the registration system to run this method after registered all fields in this class.</p>
 * <p>This method must have no parameter and no return value.</p>
 * <p>You can use it to do something such as register recipes.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface CallbackHandler {
    /**
     * Call this before or after actions.
     *
     * @return {@link CallTime }
     */
    CallTime value() default CallTime.AFTER;

    /**
     * The running environment. CLIENT->Client only, SERVER->Both
     *
     * @return {@link EnvType }
     */
    EnvType environment() default EnvType.SERVER;

    enum CallTime {
        BEFORE, AFTER
    }
}
