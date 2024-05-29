package com.iafenvoy.annotationlib.mixin;

import com.iafenvoy.annotationlib.registry.RegistrationGroup;
import com.iafenvoy.annotationlib.registry.RegistrationLink;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Registry.class)
public class RegistryMixin {
    @Inject(method = "freezeRegistries", at = @At("HEAD"))
    private static void onRegistryFreeze(CallbackInfo ci) {
        RegistrationLink.postEndRegister();
    }
}
