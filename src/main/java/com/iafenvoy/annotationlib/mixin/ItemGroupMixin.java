package com.iafenvoy.annotationlib.mixin;

import com.iafenvoy.annotationlib.registry.RegistrationGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemGroup.class)
public class ItemGroupMixin {
    @Inject(method = "appendStacks", at = @At("RETURN"))
    private void addStack(DefaultedList<ItemStack> stacks, CallbackInfo ci) {
        RegistrationGroup.addToGroup((ItemGroup) (Object) this, stacks);
    }
}
