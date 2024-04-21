package com.iafenvoy.annotationlib.network;

import com.iafenvoy.annotationlib.AnnotationLib;
import com.iafenvoy.annotationlib.annotation.network.NetworkHandler;
import com.iafenvoy.annotationlib.util.IdentifierHelper;
import com.iafenvoy.annotationlib.util.MethodHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NetworkManager {
    public static void register(Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            NetworkHandler networkHandler = method.getAnnotation(NetworkHandler.class);
            if (networkHandler == null) continue;
            switch (networkHandler.side()) {
                case CLIENT -> {
                    if (MethodHelper.match(method, ClientPlayNetworking.PlayChannelHandler.class))
                        ClientPlayNetworking.registerReceiver(IdentifierHelper.buildFromTarget(networkHandler.id()), (client, handler, buf, responseSender) -> {
                            try {
                                method.invoke(null, client, handler, buf, responseSender);
                            } catch (InvocationTargetException | IllegalAccessException e) {
                                AnnotationLib.LOGGER.error("Failed to invoke method: ", e);
                            }
                        });
                    else
                        AnnotationLib.LOGGER.error("Cannot register handler " + method.getName() + " with wrong signature.");
                }
                case SERVER -> {
                    if (MethodHelper.match(method, ServerPlayNetworking.PlayChannelHandler.class))
                        ServerPlayNetworking.registerGlobalReceiver(IdentifierHelper.buildFromTarget(networkHandler.id()), (server, player, handler, buf, sender) -> {
                            try {
                                method.invoke(null, server, player, handler, buf, sender);
                            } catch (InvocationTargetException | IllegalAccessException e) {
                                AnnotationLib.LOGGER.error("Failed to invoke method: ", e);
                            }
                        });
                    else
                        AnnotationLib.LOGGER.error("Cannot register handler " + method.getName() + " with wrong signature.");
                }
            }
        }
    }
}
