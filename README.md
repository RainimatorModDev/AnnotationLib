# Annotation Lib

**The magic of annotations :)**

This is a library to provide some operations powered by annotations.

Currently supported: `Registration`, `Network Handler`, `Command System`, `Config System`

**NOTE: 1.20.1+ is the primary develop version. 1.17.1-1.19.2 only update with big change. 1.16.5- will not supported.**

## Maven?

See wiki for installation

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

Or you can use `AnnotationApi.register(YourClass.class);`.

### Registration

You need to implement `IAnnotatedRegistryEntry`.

[Example Code](https://github.com/IAFEnvoy/AnnotationLib/blob/main/src/main/java/com/iafenvoy/annotationlib/test/TestRegistry.java)

### Network Handler

You need to implement `IAnnotatedNetworkEntry`.

[Example Code](https://github.com/RainimatorModDev/RainimatorMod/tree/master/src/main/java/com/rainimator/rainimatormod/network)

### Command System

You need to implement `IAnnotatedNetworkEntry`.

[Example Code](https://github.com/IAFEnvoy/AnnotationLib/blob/main/src/main/java/com/iafenvoy/annotationlib/test/TestCommand.java)

### Config System

**More recommended to use Cloth Config, this is just a simple one.**

You need to implement `IAnnotatedConfigEntry`.