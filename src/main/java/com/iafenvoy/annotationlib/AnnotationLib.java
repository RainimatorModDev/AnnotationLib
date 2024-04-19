package com.iafenvoy.annotationlib;

import com.iafenvoy.annotationlib.registry.RegistryManager;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

public class AnnotationLib implements ModInitializer {
    public static final String MOD_ID = "annotation_lib";
    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        RegistryManager.registerDefault();
    }
}
