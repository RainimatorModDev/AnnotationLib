package com.iafenvoy.annotationlib.util;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.argument.*;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public enum CommandArgumentType {
    LITERAL(null),
    WORD(StringArgumentType.word()),
    STRING(StringArgumentType.string()),
    GREEDY(StringArgumentType.greedyString()),
    SINGLE_ENTITY_SELECTOR(EntityArgumentType.entity()),
    MULTI_ENTITY_SELECTOR(EntityArgumentType.entities()),
    POSITION(Vec3ArgumentType.vec3()),
    NBT_Compound(NbtCompoundArgumentType.nbtCompound()),
    NBT_Element(NbtElementArgumentType.nbtElement()),
    NBT_Path(NbtPathArgumentType.nbtPath());

    private final ArgumentType<?> argumentType;

    CommandArgumentType(ArgumentType<?> argumentType) {
        this.argumentType = argumentType;
    }

    public RequiredArgumentBuilder<ServerCommandSource,?> getArgumentBuilder(String name){
        return CommandManager.argument(name,argumentType);
    }
}
