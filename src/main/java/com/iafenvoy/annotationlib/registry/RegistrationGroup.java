package com.iafenvoy.annotationlib.registry;

import com.iafenvoy.annotationlib.AnnotationLib;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
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

    public static void postItem(Identifier group, ItemGroup.Entries itemCollection) {
        for (Pair<Identifier, Field> pair : NEED_TO_ADD)
            if (pair.getLeft().equals(group))
                try {
                    if (Item.class.isAssignableFrom(pair.getRight().getType()))
                        itemCollection.add(new ItemStack((Item) pair.getRight().get(null)));
                    else if (ItemStack.class.isAssignableFrom(pair.getRight().getType()))
                        itemCollection.add((ItemStack) pair.getRight().get(null));
                    else
                        AnnotationLib.LOGGER.error("Cannot put an object which is not Item or ItemStack into item group: " + pair.getRight().getName());
                } catch (IllegalAccessException e) {
                    AnnotationLib.LOGGER.error("Fail to get object: " + pair.getRight().getName(), e);
                }
    }
}
