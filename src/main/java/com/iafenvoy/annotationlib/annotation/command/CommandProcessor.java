package com.iafenvoy.annotationlib.annotation.command;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import java.lang.annotation.*;
/**
 * <p>Register a command processor.</p>
 * <p>If this is a field, it must implement {@link ClientPlayNetworking.PlayChannelHandler}</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@Deprecated
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface CommandProcessor {
    String value();
}
