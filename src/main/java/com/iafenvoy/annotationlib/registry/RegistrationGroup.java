package com.iafenvoy.annotationlib.registry;

import com.iafenvoy.annotationlib.AnnotationLib;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class RegistrationGroup {
    private static final List<Pair<Identifier, Field>> NEED_TO_ADD = new ArrayList<>();

    public static void add(Identifier id, Field field) {
        NEED_TO_ADD.add(new Pair<>(id, field));
    }

    static {
        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((group, entries) -> {
            Identifier groupId = Registries.ITEM_GROUP.getId(group);
            for (Pair<Identifier, Field> pair : NEED_TO_ADD)
                if (pair.getLeft().equals(groupId))
                    try {
                        if (Item.class.isAssignableFrom(pair.getRight().getType()))
                            entries.add(new ItemStack((Item) pair.getRight().get(null)));
                        else if (ItemStack.class.isAssignableFrom(pair.getRight().getType()))
                            entries.add((ItemStack) pair.getRight().get(null));
                        else
                            AnnotationLib.LOGGER.error("Cannot put an object which is not Item or ItemStack into item group: " + pair.getRight().getName());
                    } catch (IllegalAccessException e) {
                        AnnotationLib.LOGGER.error("Fail to get object: " + pair.getRight().getName(), e);
                    }
        });
    }
}
