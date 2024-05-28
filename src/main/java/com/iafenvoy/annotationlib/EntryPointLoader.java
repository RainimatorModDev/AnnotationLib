package com.iafenvoy.annotationlib;

import com.iafenvoy.annotationlib.annotation.AnnotationProcessor;
import com.iafenvoy.annotationlib.annotation.CallbackHandler;
import com.iafenvoy.annotationlib.command.CommandRegistration;
import com.iafenvoy.annotationlib.config.ConfigManager;
import com.iafenvoy.annotationlib.network.NetworkManager;
import com.iafenvoy.annotationlib.registry.RegistrationManager;
import com.iafenvoy.annotationlib.util.IAnnotationLibEntryPoint;
import com.iafenvoy.annotationlib.util.IAnnotationProcessor;
import com.iafenvoy.annotationlib.util.MethodHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntryPointLoader {
    private static EntryPointLoader INSTANCE = null;
    public static final boolean isClientSide = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    public final HashMap<Class<? extends IAnnotationLibEntryPoint>, IAnnotationProcessor> processors = new HashMap<>();
    private final List<Class<?>> loadedClasses = new ArrayList<>();

    public static EntryPointLoader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EntryPointLoader();
            INSTANCE.registerProcessor(new RegistrationManager());
            INSTANCE.registerProcessor(new NetworkManager());
            INSTANCE.registerProcessor(new CommandRegistration());
            INSTANCE.registerProcessor(ConfigManager.INSTANCE);
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
        if (this.loadedClasses.contains(clazz)) {
            AnnotationLib.LOGGER.warn("Class %s has been loaded!", clazz.getName());
            return;
        }
        this.loadedClasses.add(clazz);
        //find @CallbackHandler
        final List<Method> before = new ArrayList<>(), after = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            CallbackHandler callback = method.getAnnotation(CallbackHandler.class);
            if (Modifier.isStatic(method.getModifiers()) && callback != null) {
                if (!isClientSide && callback.environment() == EnvType.CLIENT) continue;
                if (MethodHelper.check(method, Void.TYPE)) {
                    if (Modifier.isPrivate(method.getModifiers()))
                        method.setAccessible(true);
                    if (callback.value() == CallbackHandler.CallTime.AFTER) after.add(method);
                    else before.add(method);
                } else
                    AnnotationLib.LOGGER.warn(String.format("Method %s in class %s has a wrong signature, see @CallbackHandler for more info.", method.getName(), clazz.getName()));
            }
        }
        for (Method method : before)
            invokeCallback(method);
        for (Map.Entry<Class<? extends IAnnotationLibEntryPoint>, IAnnotationProcessor> entry : processors.entrySet())
            if (entry.getKey().isAssignableFrom(clazz))
                entry.getValue().process(clazz);
        for (Method method : after)
            invokeCallback(method);
    }

    private static void invokeCallback(Method method) {
        try {
            method.invoke(null);
        } catch (InvocationTargetException | IllegalAccessException e) {
            AnnotationLib.LOGGER.error("Fail to invoke method: " + method.getName(), e);
        }
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
