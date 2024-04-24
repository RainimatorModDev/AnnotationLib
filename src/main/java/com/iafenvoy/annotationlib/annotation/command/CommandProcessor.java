package com.iafenvoy.annotationlib.annotation.command;

import com.iafenvoy.annotationlib.util.CommandArgumentType;
import com.mojang.brigadier.Command;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.lang.annotation.*;

/**
 * <p>Register a command processor.</p>
 * <p>If this is a method, it must have the same signature with {@link Command}&lt;{@link ServerCommandSource}&gt;</p>
 * <p>If you want to register for root, leave value field empty.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface CommandProcessor {
    /**
     * The name of literal or argument name, blank for root command.
     *
     * @return {@link String }
     */
    String value() default "";

    /**
     * The argument type, default for literal.
     *
     * @return {@link CommandArgumentType }
     */
    CommandArgumentType type() default CommandArgumentType.LITERAL;

    /**
     * The command register environment.
     *
     * @return {@link CommandManager.RegistrationEnvironment }
     */
    CommandManager.RegistrationEnvironment environment() default CommandManager.RegistrationEnvironment.ALL;
}
