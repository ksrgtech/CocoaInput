package jp.axer.cocoainput.loader;


import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import jp.axer.cocoainput.config.fabric.FabricConfigScreen;

public class CocoaInputModMenu implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> new FabricConfigScreen(parent, FabricLoader.instance.config);
	}
}
