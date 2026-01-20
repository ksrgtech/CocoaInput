package jp.axer.cocoainput.loader;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.domain.*;
import jp.axer.cocoainput.mcutil.ModLogger;
import jp.axer.cocoainput.config.FCConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.Minecraft;
import jp.axer.cocoainput.LibraryCopyImpl;

public class FabricLoader implements ClientModInitializer {
	public static FabricLoader instance;
	public CocoaInput cocoainput;
	@Override
	public void onInitializeClient() {
		FabricLoader.instance=this;
	}
	public void onWindowLaunched(){
        var logger = new ModLogger();
        MinecraftRawWindowIdAccessor rawWindowIdAccessor = () -> Minecraft.getInstance().getWindow().getWindow();
        var nativeLibraryLoader = new LibraryCopyImpl();
        ScreenScaleFactorGetter screenScaleFactorGetter = () -> Minecraft.getInstance().getWindow().getGuiScale();

		this.cocoainput=new CocoaInput("Fabric",null, logger, rawWindowIdAccessor, nativeLibraryLoader, screenScaleFactorGetter);
		logger.log("Fabric config setup");
        logger.log("Config path:"+net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().resolve("cocoainput.json").toString());
		FCConfig.init("cocoainput",net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().resolve("cocoainput.json"), FCConfig.class);
		CocoaInput.config=new FCConfig();
        logger.log("ConfigPack:"+CocoaInput.config.isAdvancedPreeditDraw()+" "+CocoaInput.config.isNativeCharTyped());
	}

	public void onChangeScreen(Screen sc){
		if(this.cocoainput==null){
			this.onWindowLaunched();
			return;
		}
		this.cocoainput.distributeScreen(sc);
	}
}
