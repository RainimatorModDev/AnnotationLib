package com.iafenvoy.annotationlib;

import com.iafenvoy.annotationlib.api.AnnotationApi;
import com.iafenvoy.annotationlib.test.TestCommand;
import com.iafenvoy.annotationlib.test.TestRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnnotationLib implements ModInitializer {
    public static final String MOD_ID = "annotation_lib";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        EntryPointLoader.getInstance().loadCommon();
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            AnnotationApi.register(TestRegistry.class);
            AnnotationApi.register(TestCommand.class);
        }
    }
}
