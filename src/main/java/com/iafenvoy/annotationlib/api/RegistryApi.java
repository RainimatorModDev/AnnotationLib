package com.iafenvoy.annotationlib.api;

import com.iafenvoy.annotationlib.annotation.ModId;
import com.iafenvoy.annotationlib.registry.RegistryManager;

/**
 * <p>Provide API to internal register system.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
public class RegistryApi {
    /**
     * <p>Register class object manually.</p>
     * <p>More recommended to put class entrypoint into <b>fabric.mod.json</b>.</p>
     *
     * @param clazz Class want to be registered. It must have {@link ModId} Annotation
     */
    public static void register(Class<?> clazz) {
        RegistryManager.register(clazz);
    }
}
