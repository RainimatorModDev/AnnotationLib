package com.iafenvoy.annotationlib;

import com.iafenvoy.annotationlib.annotation.AnnotationProcessor;
import com.iafenvoy.annotationlib.command.CommandRegistration;
import com.iafenvoy.annotationlib.network.NetworkManager;
import com.iafenvoy.annotationlib.registry.RegistrationManager;
import com.iafenvoy.annotationlib.util.IAnnotationLibEntryPoint;
import com.iafenvoy.annotationlib.util.IAnnotationProcessor;
import net.fabricmc.loader.api.FabricLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntryPointLoader {
    private static EntryPointLoader INSTANCE = null;
    public final HashMap<Class<? extends IAnnotationLibEntryPoint>, IAnnotationProcessor> processors = new HashMap<>();

    public static EntryPointLoader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EntryPointLoader();
            INSTANCE.registerProcessor(new RegistrationManager());
            INSTANCE.registerProcessor(new NetworkManager());
            INSTANCE.registerProcessor(new CommandRegistration());
        }
        return INSTANCE;
    }

    public void registerProcessor(IAnnotationProcessor processor) {
        AnnotationProcessor annotationProcessor = processor.getClass().getAnnotation(AnnotationProcessor.class);
        if (annotationProcessor == null)
            throw new IllegalArgumentException("Annotation processor should have @AnnotationProcessor.");
        processors.put(annotationProcessor.value(), processor);
    }

    public void loadClass(Class<?> clazz) {
        for(Map.Entry<Class<? extends IAnnotationLibEntryPoint>, IAnnotationProcessor> entry:processors.entrySet())
            if (entry.getKey().isAssignableFrom(clazz))
                entry.getValue().process(clazz);
    }

    public void loadEntryPoints(List<IAnnotationLibEntryPoint> entryPoints) {
        for (IAnnotationLibEntryPoint entryPoint : entryPoints)
            loadClass(entryPoint.getClass());
    }

    public void loadCommon() {
        AnnotationLib.LOGGER.info("Start to run common annotation powered tasks.");
        loadEntryPoints(FabricLoader.getInstance().getEntrypoints(AnnotationLib.MOD_ID, IAnnotationLibEntryPoint.class));
    }

    public void loadClient() {
        AnnotationLib.LOGGER.info("Start to run client annotation powered tasks.");
        loadEntryPoints(FabricLoader.getInstance().getEntrypoints(AnnotationLib.MOD_ID + "_client", IAnnotationLibEntryPoint.class));
    }

    public void loadServer() {
        AnnotationLib.LOGGER.info("Start to run server annotation powered tasks.");
        loadEntryPoints(FabricLoader.getInstance().getEntrypoints(AnnotationLib.MOD_ID + "_server", IAnnotationLibEntryPoint.class));
    }
}
