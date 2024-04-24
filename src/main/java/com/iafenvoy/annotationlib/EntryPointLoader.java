package com.iafenvoy.annotationlib;

import com.iafenvoy.annotationlib.api.IAnnotatedCommandEntry;
import com.iafenvoy.annotationlib.api.IAnnotatedNetworkEntry;
import com.iafenvoy.annotationlib.api.IAnnotatedRegistryEntry;
import com.iafenvoy.annotationlib.command.CommandRegistration;
import com.iafenvoy.annotationlib.network.NetworkManager;
import com.iafenvoy.annotationlib.registry.RegistrationManager;
import com.iafenvoy.annotationlib.util.IAnnotationLibEntryPoint;
import net.fabricmc.loader.api.FabricLoader;

import java.util.List;

public class EntryPointLoader {
    public static void loadCommon() {
        AnnotationLib.LOGGER.info("Start to run common annotation powered tasks.");
        load(FabricLoader.getInstance().getEntrypoints(AnnotationLib.MOD_ID, IAnnotationLibEntryPoint.class));
    }

    public static void loadClient() {
        AnnotationLib.LOGGER.info("Start to run client annotation powered tasks.");
        load(FabricLoader.getInstance().getEntrypoints(AnnotationLib.MOD_ID + "_client", IAnnotationLibEntryPoint.class));
    }

    public static void loadServer() {
        AnnotationLib.LOGGER.info("Start to run server annotation powered tasks.");
        load(FabricLoader.getInstance().getEntrypoints(AnnotationLib.MOD_ID + "_server", IAnnotationLibEntryPoint.class));
    }

    private static void load(List<IAnnotationLibEntryPoint> entryPoints) {
        for (IAnnotationLibEntryPoint entrypoint : entryPoints) {
            Class<?> clazz = entrypoint.getClass();
            if (IAnnotatedRegistryEntry.class.isAssignableFrom(clazz))
                RegistrationManager.register(entrypoint.getClass());
            if (IAnnotatedNetworkEntry.class.isAssignableFrom(clazz))
                NetworkManager.register(entrypoint.getClass());
            if (IAnnotatedCommandEntry.class.isAssignableFrom(clazz))
                CommandRegistration.register(entrypoint.getClass());
        }
    }
}
