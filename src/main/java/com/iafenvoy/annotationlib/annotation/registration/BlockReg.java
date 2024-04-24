package com.iafenvoy.annotationlib.annotation.registration;

import com.iafenvoy.annotationlib.annotation.TargetId;
import com.iafenvoy.annotationlib.annotation.UnusedYet;
import net.minecraft.util.Identifier;
import net.minecraft.item.BlockItem;

import java.lang.annotation.*;

/**
 * <p>Register this block with given ID.</p>
 * <p>Field name will be used if no ID provided.</p>
 * <p>Processor will automatically create {@link BlockItem} and add it to item group if provided.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@UnusedYet
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface BlockReg {
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
