package jp.axer.cocoainput.config.forge;

import jp.axer.cocoainput.config.ConfigPack;

import java.nio.file.Path;

public class WithDefaults extends ForgeConfigImpl implements ConfigPack{

	@Entry(comment="AdvancedPreeditDraw - Is preedit marking - Default:true")
	public static boolean advancedPreeditDraw=true;
	@Entry(comment="NativeCharTyped - Is text inserted with native way - Default:true")
	public static boolean nativeCharTyped=true;

    public WithDefaults(String modid, Path apath, Class<?> config) {
        super(modid, apath, config);
    }

    @Override
	public boolean isAdvancedPreeditDraw() {
		// TODO 自動生成されたメソッド・スタブ
		return advancedPreeditDraw;
	}
	@Override
	public boolean isNativeCharTyped() {
		// TODO 自動生成されたメソッド・スタブ
		return nativeCharTyped;
	}


}
