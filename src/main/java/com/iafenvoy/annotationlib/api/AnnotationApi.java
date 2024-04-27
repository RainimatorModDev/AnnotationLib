package com.iafenvoy.annotationlib.api;

import com.iafenvoy.annotationlib.EntryPointLoader;
import com.iafenvoy.annotationlib.config.ConfigManager;
import com.iafenvoy.annotationlib.util.IAnnotationLibEntryPoint;
import com.iafenvoy.annotationlib.util.IAnnotationProcessor;
import io.netty.util.internal.UnstableApi;

/**
 * <p>Provide API to internal loading system.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
public class AnnotationApi {
    public static void register(Class<? extends IAnnotationLibEntryPoint> clazz) {
        EntryPointLoader.getInstance().loadClass(clazz);
    }

    @UnstableApi
    public static void registerProcessor(IAnnotationProcessor processor) {
        EntryPointLoader.getInstance().registerProcessor(processor);
    }

    @SuppressWarnings("unchecked")
    public static <T extends IAnnotatedConfigEntry> T getConfig(Class<T> clazz) {
        return (T) ConfigManager.INSTANCE.configs.get(clazz);
    }
}
