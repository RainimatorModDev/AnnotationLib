package com.iafenvoy.annotationlib.util;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

@SuppressWarnings("unchecked")
public class UncheckedMethods {
    public static ParticleType<ParticleEffect> getParticleType(ParticleType<? extends ParticleEffect> particleType) {
        return (ParticleType<ParticleEffect>) particleType;
    }
}
