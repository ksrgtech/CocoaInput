package jp.axer.cocoainput;

import java.io.IOException;

import com.sun.jna.Platform;

import jp.axer.cocoainput.arch.darwin.DarwinController;
import jp.axer.cocoainput.arch.dummy.DummyController;
import jp.axer.cocoainput.arch.win.WinController;
import jp.axer.cocoainput.arch.x11.X11Controller;
import jp.axer.cocoainput.plugin.CocoaInputController;
import jp.axer.cocoainput.domain.*;

public class CocoaInput {
	private static CocoaInputController controller;
	// TODO(kisargi): あとで直す
    // public static ConfigPack config = ConfigPack.defaultConfig;
    private static SimpleLogger logger;
	
	public CocoaInput(String loader, SimpleLogger logger, MinecraftRawWindowIdAccessor w, NativeLibraryLoader n, ScreenScaleFactorGetter s) {
		CocoaInput.logger = logger;
		logger.log("Modloader:" + loader);
		try {
			if (Platform.isMac()) {
				CocoaInput.applyController(new DarwinController(n, logger, s));
			} else if (Platform.isWindows()) {
				CocoaInput.applyController(new WinController(w, n, s));
			} else if (Platform.isX11()) {
				CocoaInput.applyController(new X11Controller(w, n));
			} else {
				logger.log("CocoaInput cannot find appropriate Controller in running OS.");
				CocoaInput.applyController(new DummyController(logger));
			}
			logger.log("CocoaInput has been initialized.");
		} catch (Throwable e) {
			logger.log("Failed to initialize native CocoaInput controller: " + e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
			CocoaInput.applyFallbackController(logger);
		}
	}

	public static double getScreenScaledFactor(ScreenScaleFactorGetter getter) {
        return getter.getFactor();
		// return Minecraft.getInstance().getWindow().getGuiScale();
	}

	public static void applyController(CocoaInputController controller) throws IOException {
		CocoaInput.controller = controller;
		if (logger != null) {
			logger.log("CocoaInput is now using controller:" + controller.getClass());
		}
	}

	private static void applyFallbackController(SimpleLogger logger) {
		try {
			CocoaInput.applyController(new DummyController(logger));
		} catch (IOException ignored) {
			CocoaInput.controller = new DummyController(logger);
		}
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
