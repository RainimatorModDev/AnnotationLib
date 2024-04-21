package com.iafenvoy.annotationlib.annotation.network;

import com.iafenvoy.annotationlib.annotation.TargetId;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import java.lang.annotation.*;

/**
 * <p>Register a network handler.</p>
 * <p>This method must have signature like {@link ClientPlayNetworking.PlayChannelHandler} or {@link ServerPlayNetworking.PlayChannelHandler}</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface NetworkHandler {
    /**
     * The handle place. (Client/Server)
     *
     * @return {@link EnvType }
     */
    EnvType side();

    /**
     * The message ID
     *
     * @return {@link TargetId }
     */
    TargetId id();
}
