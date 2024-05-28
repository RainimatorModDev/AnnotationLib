package com.iafenvoy.annotationlib.command;

import com.iafenvoy.annotationlib.AnnotationLib;
import com.iafenvoy.annotationlib.annotation.AnnotationProcessor;
import com.iafenvoy.annotationlib.annotation.command.CommandProcessor;
import com.iafenvoy.annotationlib.annotation.command.Permission;
import com.iafenvoy.annotationlib.api.IAnnotatedCommandEntry;
import com.iafenvoy.annotationlib.util.CommandArgumentType;
import com.iafenvoy.annotationlib.util.IAnnotationProcessor;
import com.iafenvoy.annotationlib.util.MethodHelper;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@AnnotationProcessor(IAnnotatedCommandEntry.class)
public class CommandRegistration implements IAnnotationProcessor {
    @Override
    public void process(Class<?> clazz) {
        CommandProcessor main = clazz.getAnnotation(CommandProcessor.class);
        if (main == null) return;
        if (main.type() != CommandArgumentType.LITERAL)
            throw new IllegalArgumentException("Root command must be literal");
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> dispatcher.register((LiteralArgumentBuilder<ServerCommandSource>) subRegister(CommandManager.literal(main.value()), clazz, environment))));
    }

    private static int tryToInvoke(Method method, CommandContext<ServerCommandSource> context) {
        try {
            return (int) method.invoke(null, context);
        } catch (IllegalAccessException | InvocationTargetException | ClassCastException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T extends ArgumentBuilder<ServerCommandSource, T>> ArgumentBuilder<ServerCommandSource, T> subRegister(ArgumentBuilder<ServerCommandSource, T> builder, Class<?> clazz, CommandManager.RegistrationEnvironment environment) {
        for (Method method : clazz.getMethods()) {
            if (!Modifier.isStatic(method.getModifiers())) continue;
            CommandProcessor processor = method.getAnnotation(CommandProcessor.class);
            if (processor == null)
                continue;
            if (MethodHelper.match(method, Command.class)) {
                Permission permission = method.getAnnotation(Permission.class);
                if (processor.value().isBlank())
                    builder.executes(context -> tryToInvoke(method, context));
                else if (processor.type() == CommandArgumentType.LITERAL) {
                    LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = CommandManager.literal(processor.value());
                    if (permission != null)
                        literalArgumentBuilder.requires(source -> source.hasPermissionLevel(permission.value()));
                    builder.then(literalArgumentBuilder.executes(context -> tryToInvoke(method, context)));
                } else {
                    RequiredArgumentBuilder<ServerCommandSource, ?> requiredArgumentBuilder = processor.type().getArgumentBuilder(processor.value());
                    if (permission != null)
                        requiredArgumentBuilder.requires(source -> source.hasPermissionLevel(permission.value()));
                    builder.then(requiredArgumentBuilder.executes(context -> tryToInvoke(method, context)));
                }
            } else
                AnnotationLib.LOGGER.warn(String.format("Method %s in class %s has a wrong signature, see @CommandProcessor for more info.", method.getName(), clazz.getName()));
        }
        for (Class<?> c : clazz.getClasses()) {
            if (!Modifier.isStatic(c.getModifiers())) continue;
            CommandProcessor processor = c.getAnnotation(CommandProcessor.class);
            if (processor == null)
                continue;
            if (processor.value().isBlank()) {
                AnnotationLib.LOGGER.warn(String.format("Sub-class %s in class %s shouldn't have blank value, see @CommandProcessor for more info.", c.getName(), clazz.getName()));
                continue;
            }
            Permission permission = c.getAnnotation(Permission.class);
            if (permission != null)
                builder.requires(source -> source.hasPermissionLevel(permission.value()));
            if (processor.type() == CommandArgumentType.LITERAL)
                builder.then(subRegister(CommandManager.literal(processor.value()), c, environment));
            else
                builder.then(subRegister(processor.type().getArgumentBuilder(processor.value()), c, environment));
        }
        return builder;
    }
}
