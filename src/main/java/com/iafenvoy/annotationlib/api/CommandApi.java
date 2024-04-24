package com.iafenvoy.annotationlib.api;

import com.iafenvoy.annotationlib.command.CommandRegistration;

/**
 * <p>Provide API to internal command system.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
public class CommandApi {
    public static void register(Class<?> clazz) {
        CommandRegistration.register(clazz);
    }
}
