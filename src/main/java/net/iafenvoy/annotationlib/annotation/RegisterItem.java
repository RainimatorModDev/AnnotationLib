package net.iafenvoy.annotationlib.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RegisterItem {
    String id();
}
