package com.iafenvoy.annotationlib.annotation.network;

import com.iafenvoy.annotationlib.annotation.TargetId;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import java.lang.annotation.*;

/**
 * <p>Register a network handler.</p>
 * <p>This class must implement {@link ClientPlayNetworking.PlayChannelHandler} or {@link ServerPlayNetworking.PlayChannelHandler}</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface NetworkHandler {
    /**
     * The message ID
     *
     * @return {@link TargetId }
     */
    TargetId value();
}
