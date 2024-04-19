package com.iafenvoy.annotationlib.test;

import com.iafenvoy.annotationlib.AnnotationLib;
import com.iafenvoy.annotationlib.annotation.Link;
import com.iafenvoy.annotationlib.annotation.ModId;
import com.iafenvoy.annotationlib.annotation.ObjectReg;
import com.iafenvoy.annotationlib.api.IAnnotationLibEntryPoint;
import com.iafenvoy.annotationlib.util.TargetType;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

@SuppressWarnings("unused")
@ModId(AnnotationLib.MOD_ID)
public class TestRegistry implements IAnnotationLibEntryPoint {
    //This will print a warning
    public static final String UNUSED_STRING = "unused";
    //Success
    @ObjectReg
    public static final Item TEST_ITEM = new Item(new FabricItemSettings());
    //Success
    @ObjectReg
    public static final Block TEST_BLOCK = new Block(AbstractBlock.Settings.create());
    //Success and link to the block above
    @Link(type = TargetType.BLOCK, target = AnnotationLib.MOD_ID + ":test_block")
    public static Item TEST_BLOCK_ITEM = null;
    //This will print a warning
    @Link(type = TargetType.BLOCK, target = "not_existed")
    public static Item UNUSED_LINK_ITEM = null;
}
