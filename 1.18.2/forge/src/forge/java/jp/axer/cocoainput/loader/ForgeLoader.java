package jp.axer.cocoainput.loader;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.config.forge.ForgeConfigImpl;
import jp.axer.cocoainput.config.forge.ForgeConfigScreen;
import jp.axer.cocoainput.config.forge.WithDefaults;
import jp.axer.cocoainput.domain.*;
import jp.axer.cocoainput.mcutil.ModLogger;
import jp.axer.cocoainput.plugin.IMEReceiver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.ConfigGuiHandler;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.lang.reflect.Field;

@Mod("cocoainput")
public class ForgeLoader {
	private CocoaInput instance;
    private WithDefaults config;
    private static SimpleLogger logger;
    private static ForgeLoader modInstance;

    public static SimpleLogger getLoggerInstance() {
        return logger;
    }

    public static ForgeLoader getModInstance() {
        return modInstance;
    }

	public ForgeLoader(){
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event) {
        modInstance = this;
        logger = new ModLogger();
        var zip = ModList.get().getModFileById("cocoainput").getFile().getFilePath().toString();
        MinecraftRawWindowIdAccessor rawWindowIdAccessor = () -> Minecraft.getInstance().getWindow().getWindow();
        var nativeLibraryLoader = new LibraryCopyImpl(logger, zip, () -> Minecraft.getInstance().gameDirectory.getAbsolutePath());
        ScreenScaleFactorGetter screenScaleFactorGetter = () -> Minecraft.getInstance().getWindow().getGuiScale();
        this.instance=new CocoaInput("MinecraftForge", logger, rawWindowIdAccessor, nativeLibraryLoader, screenScaleFactorGetter);
        logger.log("Forge config setup");
        logger.log("Config path:"+FMLPaths.CONFIGDIR.get().resolve("cocoainput.json").toString());
        config = new WithDefaults("cocoainput",FMLPaths.CONFIGDIR.get().resolve("cocoainput.json"), WithDefaults.class);
        ModLoadingContext.get().registerExtensionPoint(
                ConfigGuiHandler.ConfigGuiFactory.class, () ->
                        new ConfigGuiHandler.ConfigGuiFactory((mc,modListScreen)->new ForgeConfigScreen(modListScreen, config)));
        logger.log("ConfigPack:"+config.isAdvancedPreeditDraw()+" "+config.isNativeCharTyped());
	}
	@SubscribeEvent
    public void didChangeGui(ScreenOpenEvent event) {
        var sc = event.getScreen();
        onOpenScreen(sc);
	}

    public void onOpenScreen(Screen sc) {
        WrapperChecker w = () -> {
            try {
                Field wrapper = sc.getClass().getField("wrapper");
                wrapper.setAccessible(true);
                return wrapper.get(sc) instanceof IMEReceiver;
            } catch (Exception e) {
                return false;
            }
        };

        this.instance.distributeScreen(w);
    }
}
