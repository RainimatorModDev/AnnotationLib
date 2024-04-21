package com.iafenvoy.annotationlib.annotation.registration;

import com.iafenvoy.annotationlib.annotation.TargetId;
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
     * <p>The target item group.</p>
     *
     * @return {@link String }
     * @see Identifier
     */
    TargetId value();
}
