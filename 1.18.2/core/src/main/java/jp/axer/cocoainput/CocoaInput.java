package jp.axer.cocoainput;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.sun.jna.Platform;

import jp.axer.cocoainput.arch.darwin.DarwinController;
import jp.axer.cocoainput.arch.dummy.DummyController;
import jp.axer.cocoainput.arch.win.WinController;
import jp.axer.cocoainput.arch.x11.X11Controller;
import jp.axer.cocoainput.plugin.CocoaInputController;
import jp.axer.cocoainput.domain.*;

public class CocoaInput {
	private static CocoaInputController controller;
	private static String zipsource;
	// TODO(kisargi): あとで直す
    // public static ConfigPack config = ConfigPack.defaultConfig;
    private static SimpleLogger logger;
	
	public CocoaInput(String loader, String zipfile, SimpleLogger logger, MinecraftRawWindowIdAccessor w, NativeLibraryLoader n, ScreenScaleFactorGetter s) {
		logger.log("Modloader:" + loader);
		CocoaInput.zipsource = zipfile;
		try {
			if (Platform.isMac()) {
				CocoaInput.applyController(new DarwinController(n, logger, s));
			} else if (Platform.isWindows()) {
				CocoaInput.applyController(new WinController(w, n, s));
			} else if (Platform.isX11()) {
				CocoaInput.applyController(new X11Controller(n, w));
			} else {
				logger.log("CocoaInput cannot find appropriate Controller in running OS.");
				CocoaInput.applyController(new DummyController(n, logger));
			}
			logger.log("CocoaInput has been initialized.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static double getScreenScaledFactor(ScreenScaleFactorGetter getter) {
        return getter.getFactor();
		// return Minecraft.getInstance().getWindow().getGuiScale();
	}

	public static void applyController(CocoaInputController controller) throws IOException {
		CocoaInput.controller = controller;
		logger.log("CocoaInput is now using controller:" + controller.getClass().toString());
	}

	public static CocoaInputController getController() {
		return CocoaInput.controller;
	}

	public void distributeScreen(WrapperChecker sc) {
		if (CocoaInput.getController() != null) {
			CocoaInput.getController().screenOpenNotify(sc);
		}
	}
}
