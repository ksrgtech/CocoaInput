package jp.axer.cocoainput.mixin;

import jp.axer.cocoainput.wrapper.BookEditScreenWrapper;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BookEditScreen.class)
public class BookEditScreenMixin {
	 BookEditScreenWrapper wrapper;
	 
	 @Inject(method="init*",at=@At("RETURN"))
	 private void init(CallbackInfo ci) {
		 wrapper = new BookEditScreenWrapper((BookEditScreen)(Object)this);
	 }
	 
	 @Redirect(method="tick",at = @At(value="FIELD", target="Lnet/minecraft/client/gui/screens/inventory/BookEditScreen;frameTick:I",opcode=Opcodes.PUTFIELD))
	 private void injectCurosr(BookEditScreen esc,int n) {
		 esc.frameTick=wrapper.renewCursorCounter();
	 }
}
