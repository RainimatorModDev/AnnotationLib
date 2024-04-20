package com.iafenvoy.annotationlib.annotation;

import net.minecraft.util.Identifier;

import java.lang.annotation.*;

/**
 * <p>Add item into given item group.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Group {
    /**
     * <p>The target item group. Must in identifier format (namespace:path).</p>
     *
     * @return {@link String }
     * @see Identifier
     */
    TargetId value();
}
