package com.iafenvoy.annotationlib.annotation;

import net.minecraft.entity.attribute.DefaultAttributeContainer;

import java.lang.annotation.*;

/**
 * <p>This attribute is used to mark the default attribute build method.
 * This method should be placed in the entity class and be static.
 * This method must have no parameter and return a {@link DefaultAttributeContainer.Builder}</p>
 * <p>For example, you are registering an EntityType&lt;MyEntity&gt;.
 * Registration system will find the first method with correct signature and this annotation in MyEntity class.
 * Then it will use it to build a Attribute and register it.</p>
 * <p>If no matched method found, The registration system will not register default attribute.
 * You need to register it by yourself.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AttributeBuilder {
}
