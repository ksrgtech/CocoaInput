package jp.axer.cocoainput.loader;

import jp.axer.cocoainput.plugin.IMEReceiver;
import net.fabricmc.api.ClientModInitializer;
import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.domain.*;
import jp.axer.cocoainput.mcutil.ModLogger;
import jp.axer.cocoainput.config.fabric.WithDefaults;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.Minecraft;
import jp.axer.cocoainput.domain.LibraryCopyImpl;

import java.lang.reflect.Field;

public class FabricLoader implements ClientModInitializer {
	public static FabricLoader instance;
	public CocoaInput cocoainput;
    public WithDefaults config;
    private static SimpleLogger loggerInstance;

    public static SimpleLogger getLoggerInstance() {
        return loggerInstance;
    }

	@Override
	public void onInitializeClient() {
		FabricLoader.instance=this;
	}
	public void onWindowLaunched(){
        var logger = new ModLogger();
        loggerInstance = logger;
        MinecraftRawWindowIdAccessor rawWindowIdAccessor = () -> Minecraft.getInstance().getWindow().getWindow();
        var nativeLibraryLoader = new LibraryCopyImpl(logger, null);
        ScreenScaleFactorGetter screenScaleFactorGetter = () -> Minecraft.getInstance().getWindow().getGuiScale();

		this.cocoainput = new CocoaInput("Fabric", logger, rawWindowIdAccessor, nativeLibraryLoader, screenScaleFactorGetter);
		logger.log("Fabric config setup");
        logger.log("Config path:"+net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().resolve("cocoainput.json").toString());
		config = new WithDefaults("cocoainput",net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().resolve("cocoainput.json"), WithDefaults.class);
        logger.log("ConfigPack:"+config.isAdvancedPreeditDraw()+" "+config.isNativeCharTyped());
	}

	public void onChangeScreen(Screen sc){
		if(this.cocoainput==null){
			this.onWindowLaunched();
			return;
		}
        WrapperChecker w = () -> {
            try {
                Field wrapper = sc.getClass().getField("wrapper");
                wrapper.setAccessible(true);
                return wrapper.get(sc) instanceof IMEReceiver;
            } catch (Exception e) {
                return false;
            }
        };

		this.cocoainput.distributeScreen(w);
	}
}
