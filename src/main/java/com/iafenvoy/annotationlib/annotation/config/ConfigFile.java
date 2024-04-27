package com.iafenvoy.annotationlib.annotation.config;

import java.lang.annotation.*;
import com.iafenvoy.annotationlib.api.AnnotationApi;

/**
 * <p><b>Only provide basic usage. Use Cloth Config for advanced functions.</b></p>
 * <p>Tell the config system to load config. <b>ONLY LOAD NO SAVE</b></p>
 * <p>Gson will be used so you can use Gson features.</p>
 * <p>Use <b>{@link AnnotationApi}.getConfig(this class);</b> to get your config.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ConfigFile {
    /**
     * The config folder.
     * @return {@link String }
     */
    String path();

    /**
     * The config file without folder path.
     * @return {@link String }
     */
    String file();

    /**
     * Automatically create with default values when config file not found.
     * @return boolean
     */
    boolean autoCreate() default true;
}
