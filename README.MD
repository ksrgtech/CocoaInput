# CocoaInput
Support IME - Input Method Editor(Japanese,Chinese,Korean) in Minecraft on Multi-platforms(macOS, Windows, X11).

## Repository Structure
This repository contains MOD code for multiple Minecraft versions in separate directories:
- `1.12.2/` - MOD code for Minecraft 1.9-1.12.2 (Forge)
- `1.18.2/` - MOD code for Minecraft 1.18.2+ (Forge and Fabric)
- `libcocoainput/` - Native platform libraries (shared across all versions)

## Compiling
1. Clone git repository
```Bash
git clone https://github.com/Axeryok/CocoaInput.git
cd CocoaInput
```
2. Compile native library<br />
CocoaInput needs native-platform libraries to work. 
To compile them, run one of below scripts which is your platform.
    - build_lib_for_mac.sh - For macOS
    - build_lib_for_x11.sh - For Linux
    - build_lib_for_win.sh - For Windows
    - (build_lib_all.sh - For all platform, You should create remote_build.sh to build macOS lib remotely.)

### For Minecraft 1.12.2 (Forge only)
3. Compile Forge Mod<br />
Type below command. The mod will be located in "1.12.2/build/libs".
```Bash
cd 1.12.2
gradle build
```

### For Minecraft 1.18.2+
3. Compile Forge Mod<br />
Type below command. The mod will be located in "1.18.2/forge/build/libs".
```Bash
cd 1.18.2/forge
./gradlew build
```
4. Compile Fabric Mod<br />
Type below command. The mod will be located in "1.18.2/fabric/build/libs".
```Bash
cd 1.18.2/fabric
./gradlew build
```

## Installing
CocoaInput official binaries has been distributed on https://www.curseforge.com/minecraft/mc-mods/cocoainput .
Get jar from above URL or trying ”Compiling" task.
Place it in your mods directory.

CocoaInput requires MinecraftForge or Fabric.

This mod uses Java Native Access (Apache Licence2) and binuary for Minecraft1.7.10 contains it.

## License
Minecraft Mod Public License Japanese Transration
