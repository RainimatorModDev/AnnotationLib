package com.iafenvoy.annotationlib;

import com.iafenvoy.annotationlib.api.IAnnotationLibEntryPoint;
import com.iafenvoy.annotationlib.api.RegistryApi;
import com.iafenvoy.annotationlib.network.NetworkManager;
import com.iafenvoy.annotationlib.registry.RegistrationManager;
import com.iafenvoy.annotationlib.test.TestRegistry;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.util.List;

public class AnnotationLib implements ModInitializer {
    public static final String MOD_ID = "annotation_lib";
    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.info("Start to run annotation powered registrations.");
        List<IAnnotationLibEntryPoint> entrypoints = FabricLoader.getInstance().getEntrypoints(MOD_ID, IAnnotationLibEntryPoint.class);
        for (IAnnotationLibEntryPoint entrypoint : entrypoints) {
            RegistrationManager.register(entrypoint.getClass());
            NetworkManager.register(entrypoint.getClass());
        }
        if (FabricLoader.getInstance().isDevelopmentEnvironment())
            RegistryApi.register(TestRegistry.class);
    }
}
