package com.iafenvoy.annotationlib.annotation.registration;

import com.iafenvoy.annotationlib.annotation.TargetId;
import net.minecraft.util.Identifier;

import java.lang.annotation.*;

/**
 * <p>Register this item with given ID.</p>
 * <p>Field name will be used if no ID provided.</p>
 * <p>Processor will add it to item group if provided.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ItemReg {
    /**
     * <p>The register ID</p>
     *
     * @return {@link String }
     */
    String value() default "";
    /**
     * <p>The target item group.</p>
     *
     * @return {@link String }
     * @see Identifier
     */
    TargetId group() default @TargetId("");
}
