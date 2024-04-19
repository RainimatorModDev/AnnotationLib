package com.iafenvoy.annotationlib.test;

import com.iafenvoy.annotationlib.AnnotationLib;
import com.iafenvoy.annotationlib.annotation.ModId;
import com.iafenvoy.annotationlib.annotation.ObjectReg;
import com.iafenvoy.annotationlib.api.IAnnotationLibEntryPoint;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;

@ModId(AnnotationLib.MOD_ID)
public class TestItem implements IAnnotationLibEntryPoint {
    public static final String UNUSED_STRING = "unused";
    @ObjectReg
    public static final Item TEST_ITEM = new Item(new FabricItemSettings());
}
