package jp.axer.cocoainput.arch.dummy;

import jp.axer.cocoainput.plugin.CocoaInputController;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import jp.axer.cocoainput.domain.*;

public class DummyController implements CocoaInputController{
    public DummyController(SimpleLogger logger) {
        logger.log("This is a dummy controller.");
    }

    @Override
    public IMEOperator generateIMEOperator(IMEReceiver ime) {
        return new DummyIMEOperator();
    }

	@Override
	public void screenOpenNotify(WrapperChecker sc) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}