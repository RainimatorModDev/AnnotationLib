package com.iafenvoy.annotationlib.network;


import com.iafenvoy.annotationlib.AnnotationLib;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ClientHotkeyNetwork {
    private static final HashMap<String, KeyBinding> hotkeys = new HashMap<>();

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            synchronized (hotkeys) {
                for (Map.Entry<String, KeyBinding> keyBinding : hotkeys.entrySet())
                    if (keyBinding.getValue().wasPressed())
                        ClientPlayNetworking.send(new Identifier(AnnotationLib.MOD_ID, ServerHotkeyNetwork.HOTKEY_EVENT_ID), PacketByteBufs.create().writeString(keyBinding.getKey()));
            }
        });
    }

    public static void register(String id, KeyBinding keyBinding) {
        hotkeys.put(id, keyBinding);
    }
}
