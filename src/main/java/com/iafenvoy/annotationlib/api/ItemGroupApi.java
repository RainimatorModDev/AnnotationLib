package com.iafenvoy.annotationlib.api;

import com.iafenvoy.annotationlib.registry.RegistrationGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

/**
 * <p>Provide API to internal item group.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@SuppressWarnings("unused")
public class ItemGroupApi {
    /**
     * Call this in ItemGroup entry
     */
    public static void addItemToGroup(ItemGroup group, ItemGroup.Entries entries) {
        addItemToGroup(Registries.ITEM_GROUP.getId(group), entries);
    }

    /**
     * Call this in ItemGroup entry
     */
    public static void addItemToGroup(Identifier group, ItemGroup.Entries entries) {
        RegistrationGroup.postItem(group, entries);
    }
}