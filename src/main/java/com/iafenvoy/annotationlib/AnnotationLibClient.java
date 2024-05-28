package com.iafenvoy.annotationlib;

import com.iafenvoy.annotationlib.network.ClientHotkeyNetwork;
import net.fabricmc.api.ClientModInitializer;

public class AnnotationLibClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntryPointLoader.getInstance().loadClient();
        ClientHotkeyNetwork.init();
    }
}
