# The 1.12.2 implementation

## Supported loader

- [x] Minecraft Forge

## Supported platform

- [ ] Windows
- [ ] Linux
- [x] macOS

## Supported JDK

- 8.x

Other version causes a confusing error, which is triggered by the ForgeGradle's TaskApplyBinPatches:

```
Caused by: java.lang.ClassNotFoundException: java.util.jar.Pack200
```

You should set JAVA_HOME before build, especially if you are on systems which have multiple JDK installations.
