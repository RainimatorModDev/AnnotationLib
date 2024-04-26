package com.iafenvoy.annotationlib;

import net.fabricmc.api.ClientModInitializer;

public class AnnotationLibClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntryPointLoader.getInstance().loadClient();
    }
}
