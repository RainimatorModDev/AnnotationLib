# Annotation Lib

**The magic of annotations :)**

This is a library to provide some operations powered by annotations.

Currently supported: `Registration`

## Maven?

Not use until release. Please import it with `flatDir`.

## How to use?

### Common

For every class, you need to implement `IAnnotationLibEntryPoint`.

Then add a `entrypoint` into `fabric.mod.json`

```json
{
  "entrypoints": {
    "annotation_lib": [
      "your class here"
    ]
  }
}
```

Or you can use `RegistryApi.register(YourClass.class);`

### Registration

```java

@ModId(AnnotationLib.MOD_ID)//Must have
public class TestRegistry implements IAnnotationLibEntryPoint {
    //This will print a warning
    public static final String UNUSED_STRING = "unused";

    //Success
    @ObjectReg//Register anything with this
    public static final Item TEST_ITEM = new Item(new FabricItemSettings());

    //Success
    @ObjectReg("new_id")//You can also use another name
    public static final Block TEST_BLOCK = new Block(AbstractBlock.Settings.create());

    //Success and link to the block above
    @Group("building_blocks")//Put into creative inventory
    @Link(type = TargetType.BLOCK, target = AnnotationLib.MOD_ID + ":test_block")//BlockItem
    public static Item TEST_BLOCK_ITEM = null;

    //This will print a warning
    @Link(type = TargetType.BLOCK, target = "not_existed")
    public static Item UNUSED_LINK_ITEM = null;

    //Success
    @ObjectReg//See @AttributeBuilder for more info
    public static final EntityType<MyEntity> TEST_ENTITY_TYPE = EntityHelper.build(MyEntity::new, SpawnGroup.MONSTER, 64, 3, true, 0.6F, 1.8F);

    //Also can be used to register SoundEvent, StatusEffect, ItemGroup
    
    //This method will be called after register
    @CallbackHandler
    public static void callback() {
        AnnotationLib.LOGGER.info("Callback called");
    }
}
```
