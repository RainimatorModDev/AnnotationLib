package com.iafenvoy.annotationlib.config;

import com.google.gson.Gson;
import com.iafenvoy.annotationlib.AnnotationLib;
import com.iafenvoy.annotationlib.annotation.AnnotationProcessor;
import com.iafenvoy.annotationlib.annotation.config.ConfigFile;
import com.iafenvoy.annotationlib.api.IAnnotatedConfigEntry;
import com.iafenvoy.annotationlib.util.IAnnotationProcessor;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

@AnnotationProcessor(IAnnotatedConfigEntry.class)
public class ConfigManager implements IAnnotationProcessor {
    public static final ConfigManager INSTANCE = new ConfigManager();
    public final HashMap<Class<?>, IAnnotatedConfigEntry> configs = new HashMap<>();

    @Override
    public void process(Class<?> clazz) {
        ConfigFile configFile = clazz.getAnnotation(ConfigFile.class);
        if (configFile == null) return;
        File configFolder = new File(configFile.path());
        File config = new File(configFolder, configFile.file());
        try {
            FileReader reader = new FileReader(config);
            IAnnotatedConfigEntry data = (IAnnotatedConfigEntry) new Gson().fromJson(reader, clazz);
            reader.close();
            configs.put(clazz, data);
        } catch (IOException e) {
            e.printStackTrace();
            if (!configFile.autoCreate()) return;
            AnnotationLib.LOGGER.info("Try to create: " + config.getAbsolutePath());
            if (!configFolder.exists())
                configFolder.mkdir();
            try {
                FileWriter writer = new FileWriter(config);
                IAnnotatedConfigEntry entry = (IAnnotatedConfigEntry) clazz.getConstructor().newInstance();
                new Gson().toJson(entry, writer);
                writer.close();
                configs.put(clazz, entry);
            } catch (IOException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                     NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
}
