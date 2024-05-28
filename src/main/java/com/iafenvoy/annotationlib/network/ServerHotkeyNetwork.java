package com.iafenvoy.annotationlib.network;

import com.iafenvoy.annotationlib.AnnotationLib;
import com.iafenvoy.annotationlib.annotation.TargetId;
import com.iafenvoy.annotationlib.annotation.network.NetworkHandler;
import com.iafenvoy.annotationlib.api.IAnnotatedNetworkEntry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@NetworkHandler(@TargetId(namespace = AnnotationLib.MOD_ID, value = ServerHotkeyNetwork.HOTKEY_EVENT_ID))
public class ServerHotkeyNetwork implements IAnnotatedNetworkEntry, ServerPlayNetworking.PlayChannelHandler {
    public static final String HOTKEY_EVENT_ID = "hotkey";
    private static final HashMap<String, List<ServerPlayNetworking.PlayChannelHandler>> handlers = new HashMap<>();

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        String id = buf.readString();
        List<ServerPlayNetworking.PlayChannelHandler> list = handlers.get(id);
        if (list != null)
            for (ServerPlayNetworking.PlayChannelHandler h : list)
                h.receive(server, player, handler, buf, responseSender);
    }

    public static void register(String id, ServerPlayNetworking.PlayChannelHandler handler) {
        if (!handlers.containsKey(id))
            handlers.put(id, new ArrayList<>());
        handlers.get(id).add(handler);
    }
}