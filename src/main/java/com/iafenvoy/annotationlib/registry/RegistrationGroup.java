package com.iafenvoy.annotationlib.registry;

import com.iafenvoy.annotationlib.AnnotationLib;
import com.ibm.icu.impl.Pair;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class RegistrationGroup {
    private static final List<Pair<Identifier, Field>> NEED_TO_ADD = new ArrayList<>();

    public static void add(Identifier id, Field field) {
        NEED_TO_ADD.add(Pair.of(id, field));
    }

    public static void postItem(Identifier group, ItemGroup.Entries itemCollection) {
        for (Pair<Identifier, Field> pair : NEED_TO_ADD)
            if (pair.first.equals(group))
                try {
                    if (Item.class.isAssignableFrom(pair.second.getType()))
                        itemCollection.add(new ItemStack((Item) pair.second.get(null)));
                    else if (ItemStack.class.isAssignableFrom(pair.second.getType()))
                        itemCollection.add((ItemStack) pair.second.get(null));
                    else
                        AnnotationLib.LOGGER.error("Cannot put an object which is not Item or ItemStack into item group: " + pair.second.getName());
                } catch (IllegalAccessException e) {
                    AnnotationLib.LOGGER.error("Fail to get object: " + pair.second.getName(), e);
                }
    }
}
