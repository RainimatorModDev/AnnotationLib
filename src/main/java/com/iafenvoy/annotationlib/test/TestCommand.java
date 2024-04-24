package com.iafenvoy.annotationlib.test;

import com.iafenvoy.annotationlib.annotation.command.CommandProcessor;
import com.iafenvoy.annotationlib.api.IAnnotatedCommandEntry;
import com.iafenvoy.annotationlib.util.CommandArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

@CommandProcessor("test_command")
@SuppressWarnings("unused")
public class TestCommand implements IAnnotatedCommandEntry {
    @CommandProcessor//Command: /test_command
    public static int root(CommandContext<ServerCommandSource> context) {
        if (context.getSource().getPlayer() != null)
            context.getSource().getPlayer().sendMessage(Text.literal("Hello from test command - root!"));
        return 1;
    }

    @CommandProcessor(value = "entity", type = CommandArgumentType.SINGLE_ENTITY_SELECTOR)
    //Command: /test_command <entity_selector>
    public static int entity(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Entity entity = EntityArgumentType.getEntity(context, "entity");
        if (context.getSource().getPlayer() != null)
            context.getSource().getPlayer().sendMessage(Text.literal("The entity type you selected: " + entity.getType().toString()));
        return 1;
    }

    @CommandProcessor("sub")
    public static class SubCommand {
        @CommandProcessor//Command: /test_command sub
        public static int root(CommandContext<ServerCommandSource> context) {
            if (context.getSource().getPlayer() != null)
                context.getSource().getPlayer().sendMessage(Text.literal("Hello from test command - sub!"));
            return 1;
        }

        @CommandProcessor(value = "ok")//Command: /test_command sub ok
        public static int ok(CommandContext<ServerCommandSource> context) {
            if (context.getSource().getPlayer() != null)
                context.getSource().getPlayer().sendMessage(Text.literal("OK"));
            return 1;
        }

        @CommandProcessor(value = "greet", type = CommandArgumentType.GREEDY)//Command: /test_command sub <greet>
        public static int greet(CommandContext<ServerCommandSource> context) {
            String greet = StringArgumentType.getString(context, "greet");
            if (context.getSource().getPlayer() != null)
                context.getSource().getPlayer().sendMessage(Text.literal("Greet: " + greet));
            return 1;
        }
    }
}
