package com.iafenvoy.annotationlib.util;

import com.iafenvoy.annotationlib.annotation.TargetId;
import net.minecraft.util.Identifier;

public class IdentifierHelper {
    public static Identifier buildFromTarget(TargetId target) {
        return buildFromTarget("minecraft", target);
    }

    public static Identifier buildFromTarget(String modId, TargetId target) {
        if (target.namespace().isBlank()) return build(modId, target.value());
        return build(target.namespace(), target.value());
    }

    public static Identifier build(String modId, String name) {
        return new Identifier(modId, name);
    }
}
