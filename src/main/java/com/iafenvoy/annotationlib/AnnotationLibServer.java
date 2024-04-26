package com.iafenvoy.annotationlib;

import net.fabricmc.api.DedicatedServerModInitializer;

public class AnnotationLibServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        EntryPointLoader.getInstance().loadServer();
    }
}
