package jp.axer.cocoainput.config.fabric;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class FabricConfigScreen extends Screen {
    private final FabricConfigImpl config;

    public FabricConfigScreen(Screen parent, FabricConfigImpl config) {
        super(new TextComponent("CocoaInput config"));
        this.parent = parent;
        this.config = config;
    }
    private final Screen parent;

    @Override
    protected void init() {
        super.init();

        Button done = this.addRenderableWidget(new Button(this.width/2 - 100,this.height - 28,200,20,
                new TranslatableComponent("gui.done"), (button) -> {
            for (FabricConfigImpl.EntryInfo info : config.entries)
                try { info.field.set(null, info.value); }
                catch (IllegalAccessException ignore) {}
            config.write();
            minecraft.setScreen(parent);
        }));

        int y = 45;
        for (FabricConfigImpl.EntryInfo info : config.entries) {
            if (info.widget instanceof Map.Entry) {
                Map.Entry<Button.OnPress, Function<Object, BaseComponent>> widget = (Map.Entry<Button.OnPress, Function<Object, BaseComponent>>) info.widget;
                addRenderableWidget(new Button(width-85,y,info.width,20, widget.getValue().apply(info.value), widget.getKey()));
            }
            else {
                EditBox widget = addWidget(new EditBox(font, width-85, y, info.width, 20, null));
                widget.setValue(info.tempValue);

                Predicate<String> processor = ((BiFunction<EditBox, Button, Predicate<String>>) info.widget).apply(widget,done);
                widget.setFilter(processor);
                processor.test(info.tempValue);

                addWidget(widget);
            }
            y += 30;
        }

    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        if (mouseY >= 40 && mouseY <= 39 + config.entries.size()*30) {
            int low = ((mouseY-10)/30)*30 + 10 + 2;
            fill(matrices, 0, low, width, low+30-4, 0x33FFFFFF);
        }

        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredString(matrices, font, title, width/2, 15, 0xFFFFFF);

        int y = 40;
        for (FabricConfigImpl.EntryInfo info : config.entries) {
            drawString(matrices, font, new TextComponent(info.comment), 12, y + 10, 0xFFFFFF);
				/*
                if (info.error != null && info.error.getKey().isMouseOver(mouseX,mouseY))
                    renderTooltip(matrices, info.error.getValue(), mouseX, mouseY);
                else if (mouseY >= y && mouseY < (y + 30)) {
                    if (info.dynamicTooltip != null) {
                        try {
                            renderComponentTooltip(matrices, (List<ITextComponent>) info.dynamicTooltip.invoke(null, entries), mouseX, mouseY);
                            y += 30;
                            continue;
                        } catch (Exception e) { e.printStackTrace(); }
                    }
                    String key = translationPrefix + info.field.getName() + ".tooltip";

                    List<ITextComponent> list = new ArrayList<>();
                    for (String str : I18n.get(key).split("\n"))
                         list.add(new TextComponent(str));
                    renderComponentTooltip(matrices, list, mouseX, mouseY);

                }
                */
            y += 30;
        }
    }
}
