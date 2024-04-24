package com.iafenvoy.annotationlib;

import com.iafenvoy.annotationlib.api.CommandApi;
import com.iafenvoy.annotationlib.api.RegistryApi;
import com.iafenvoy.annotationlib.test.TestCommand;
import com.iafenvoy.annotationlib.test.TestRegistry;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

public class AnnotationLib implements ModInitializer {
    public static final String MOD_ID = "annotation_lib";
    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        EntryPointLoader.loadCommon();
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            RegistryApi.register(TestRegistry.class);
            CommandApi.register(TestCommand.class);
        }
    }
}
