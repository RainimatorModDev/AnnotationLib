package com.iafenvoy.annotationlib.network;

import com.iafenvoy.annotationlib.AnnotationLib;
import com.iafenvoy.annotationlib.annotation.AnnotationProcessor;
import com.iafenvoy.annotationlib.annotation.network.NetworkHandler;
import com.iafenvoy.annotationlib.api.IAnnotatedNetworkEntry;
import com.iafenvoy.annotationlib.util.IAnnotationProcessor;
import com.iafenvoy.annotationlib.util.IdentifierHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@AnnotationProcessor(IAnnotatedNetworkEntry.class)
public class NetworkManager implements IAnnotationProcessor {
    @Override
    public void process(Class<?> clazz) {
        NetworkHandler networkHandler = clazz.getAnnotation(NetworkHandler.class);
        if (networkHandler == null) return;
        try {
            Object obj = clazz.getConstructor().newInstance();
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT && ClientPlayNetworking.PlayChannelHandler.class.isAssignableFrom(clazz)) {
                Method method = clazz.getDeclaredMethod("receive", MinecraftClient.class, ClientPlayNetworkHandler.class, PacketByteBuf.class, PacketSender.class);
                ClientPlayNetworking.registerGlobalReceiver(IdentifierHelper.buildFromTarget(networkHandler.value()), (client, handler, buf, responseSender) -> {
                    try {
                        method.invoke(obj, client, handler, buf, responseSender);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        AnnotationLib.LOGGER.error("Failed to invoke method: ", e);
                    }
                });
            }
            if (ServerPlayNetworking.PlayChannelHandler.class.isAssignableFrom(clazz)) {
                Method method = clazz.getDeclaredMethod("receive", MinecraftServer.class, ServerPlayerEntity.class, ServerPlayNetworkHandler.class, PacketByteBuf.class, PacketSender.class);
                ServerPlayNetworking.registerGlobalReceiver(IdentifierHelper.buildFromTarget(networkHandler.value()), (server, player, handler, buf, sender) -> {
                    try {
                        method.invoke(obj, server, player, handler, buf, sender);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        AnnotationLib.LOGGER.error("Failed to invoke method: ", e);
                    }
                });
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
