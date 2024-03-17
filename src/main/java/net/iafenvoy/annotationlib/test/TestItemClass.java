package net.iafenvoy.annotationlib.test;

import net.iafenvoy.annotationlib.annotation.RegisterItem;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class TestItemClass {
    @RegisterItem(id = "test")
    public static Supplier<Item> testItem = () -> new Item(new Item.Properties());
}
