package com.iafenvoy.annotationlib.annotation;

import com.iafenvoy.annotationlib.util.TargetType;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;

import java.lang.annotation.*;

/**
 * <p>Tell the registry system to link this object to a registered object.</p>
 * <p>Field with this annotation must be <b>non-final</b>, or registration system will throw an error.</p>
 * <p>Field with this annotation should be <b>null</b>, or value will be overwritten.</p>
 * <p>For example, if you have registered a block, you can create an item field with this annotation.
 * Registration system will create a {@link BlockItem} and give it to the field.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Link {
    /**
     * <p>The target object ID. Must in identifier format (namespace:path).</p>
     *
     * @return {@link String }
     * @see Identifier
     */
    TargetId target() default @TargetId(value = "");

    /**
     * The target objects ID.
     *
     * @return {@link TargetId[] }
     */
    TargetId[] targets() default {};

    /**
     * <p>The object type you want to link.</p>
     *
     * @return {@link TargetType }
     * @see TargetType
     */
    TargetType type();
}
