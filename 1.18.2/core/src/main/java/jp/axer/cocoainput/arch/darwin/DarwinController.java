package jp.axer.cocoainput.arch.darwin;

import java.lang.reflect.Field;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.domain.*;
import jp.axer.cocoainput.plugin.CocoaInputController;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;

public class DarwinController implements CocoaInputController {
    private SimpleLogger logger;
    private ScreenScaleFactorGetter factorGetter;

    public DarwinController(NativeLibraryLoader nativeLibraryLoader, SimpleLogger logger, ScreenScaleFactorGetter getter) throws Exception {
        this.logger = logger;
        this.factorGetter = getter;
        nativeLibraryLoader.copyFrom("libcocoainput.dylib", "darwin/libcocoainput.dylib");
        Handle.INSTANCE.initialize(CallbackFunction.Func_log, CallbackFunction.Func_error, CallbackFunction.Func_debug);
        logger.log("DarwinController has been initialized.");
    }

    @Override
    public IMEOperator generateIMEOperator(IMEReceiver ime) {
        return new DarwinIMEOperator(ime, logger, factorGetter);
    }


	@Override
    public void screenOpenNotify(WrapperChecker checker) {
            if (checker.isAlreadyInitialized()) {
                return;
            }
            /*
            try {
                Field wrapper = gui.getClass().getField("wrapper");
                wrapper.setAccessible(true);
                if (wrapper.get(gui) instanceof IMEReceiver)
                    return;
            } catch (Exception e) {
                /* relax */ //}
	        Handle.INSTANCE.refreshInstance();//GUIの切り替えでIMの使用をoffにする

	}
}
