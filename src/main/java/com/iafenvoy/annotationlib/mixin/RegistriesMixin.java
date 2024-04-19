package com.iafenvoy.annotationlib.mixin;

import com.iafenvoy.annotationlib.registry.RegistrationLink;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Registries.class)
public class RegistriesMixin {
    @Inject(method = "freezeRegistries", at = @At("TAIL"))
    private static void onRegistryFreeze(CallbackInfo ci) {
        RegistrationLink.postEndRegister();
    }
}
