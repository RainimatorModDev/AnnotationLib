package com.iafenvoy.annotationlib.mixin;

import com.iafenvoy.annotationlib.registry.RegistrationGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(ItemGroup.class)
public class ItemGroupMixin {
    @Inject(method = "getDisplayStacks", at = @At("RETURN"), cancellable = true)
    private void pushOwnItems(CallbackInfoReturnable<Collection<ItemStack>> cir) {
        RegistryKey<ItemGroup> key = Registries.ITEM_GROUP.getKey((ItemGroup) (Object) this).orElseThrow(() -> new IllegalStateException("Unregistered creative tab: " + this));
        Collection<ItemStack> collection = cir.getReturnValue();
        RegistrationGroup.postItem(key.getValue(), cir.getReturnValue());
        cir.setReturnValue(collection);
    }
}
