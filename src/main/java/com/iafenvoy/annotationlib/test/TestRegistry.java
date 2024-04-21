package com.iafenvoy.annotationlib.test;

import com.iafenvoy.annotationlib.AnnotationLib;
import com.iafenvoy.annotationlib.annotation.*;
import com.iafenvoy.annotationlib.annotation.registration.Group;
import com.iafenvoy.annotationlib.annotation.registration.ItemReg;
import com.iafenvoy.annotationlib.annotation.registration.Link;
import com.iafenvoy.annotationlib.annotation.registration.ObjectReg;
import com.iafenvoy.annotationlib.api.IAnnotationLibEntryPoint;
import com.iafenvoy.annotationlib.util.EntityHelper;
import com.iafenvoy.annotationlib.util.TargetType;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;

@SuppressWarnings("unused")
@ModId(AnnotationLib.MOD_ID)
public class TestRegistry implements IAnnotationLibEntryPoint {
    //This will print a warning
    public static final String UNUSED_STRING = "unused";
    //Success
    @ItemReg(group = @TargetId(namespace = "minecraft", value = "building_blocks"))
    public static final Item TEST_ITEM = new Item(new FabricItemSettings());
    //Success
    @ObjectReg
    public static final Block TEST_BLOCK = new Block(AbstractBlock.Settings.create());
    //Success and link to the block above
    @Group(@TargetId(namespace = "minecraft", value = "building_blocks"))
    @Link(type = TargetType.BLOCK, targets = {@TargetId(namespace = AnnotationLib.MOD_ID, value = "test_block")})
    public static Item TEST_BLOCK_ITEM = null;
    //This will print a warning
    @Link(type = TargetType.BLOCK, target = @TargetId("not_existed"))
    public static Item UNUSED_LINK_ITEM = null;
    //Success
    @ObjectReg
    public static final EntityType<MyEntity> TEST_ENTITY_TYPE = EntityHelper.build(MyEntity::new, SpawnGroup.MONSTER, 64, 3, true, 0.6F, 1.8F);

    //This method will be called after register
    @CallbackHandler
    public static void callback() {
        AnnotationLib.LOGGER.info("Callback called");
    }
}
