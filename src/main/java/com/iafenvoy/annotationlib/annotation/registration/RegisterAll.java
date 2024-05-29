package com.iafenvoy.annotationlib.annotation.registration;

import java.lang.annotation.*;
import net.minecraft.util.registry.Registry;

/**
 * <p>Automatically register all static fields in this class.</p>
 * <p>Processor will automatically detect which {@link Registry} should be used.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RegisterAll {
}
