# Annotation Lib

**The magic of annotations :)**

This is a library to provide some operations powered by annotations.

Currently supported: `Registration`, `Network Handler`, `Command System`

## Maven?

Not use until release. Please import it with `flatDir`.

## How to use?

### Common

For every registration class, add a `entrypoint` into `fabric.mod.json`

```json
{
  "entrypoints": {
    "annotation_lib": [
      "your class here"
    ],
    "annotation_lib_client": [
      "your client only class here"
    ],
    "annotation_lib_server": [
      "your server only class here"
    ]
  }
}
```

Or you can use register APIs. (See below)

### Registration

You need to implement `IAnnotatedRegistryEntry` and entrypoint or use `RegistryApi.register(YourClass.class);`.

[Example Code](https://github.com/IAFEnvoy/AnnotationLib/blob/main/src/main/java/com/iafenvoy/annotationlib/test/TestRegistry.java)

**Notice: if you use `@Group` or `@ItemGroup` and use the `group` param, you should use `ItemGroupApi` manually.**

[Example Code](https://github.com/RainimatorModDev/RainimatorMod/blob/master/src/main/java/com/rainimator/rainimatormod/registry/ModCreativeTabs.java)

### Network Handler

You need to implement `IAnnotatedNetworkEntry` and entrypoint or use `NetworkApi.register(YourClass.class);`.

[Example Code](https://github.com/RainimatorModDev/RainimatorMod/tree/master/src/main/java/com/rainimator/rainimatormod/network)

### Command System

You need to implement `IAnnotatedNetworkEntry` and entrypoint or use `CommandApi.register(YourClass.class);`.

[Example Code](https://github.com/IAFEnvoy/AnnotationLib/blob/main/src/main/java/com/iafenvoy/annotationlib/test/TestCommand.java)
