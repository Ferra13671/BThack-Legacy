package com.ferra13671.BThack.api.Gui.MainMenu;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Utils.DesktopUtils;
import com.ferra13671.BThack.api.Utils.System.BThackScreen;
import com.ferra13671.BThack.api.Utils.System.buttons.Button;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class BThackCreditsScreen extends BThackScreen {

    private final Screen parent;

    public BThackCreditsScreen(Screen parent) {
        super(Text.of("CreditsScreen"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        this.buttons.clear();

        this.buttons.add(Button.of(1, mc.getWindow().getScaledWidth() / 2, mc.getWindow().getScaledHeight() / 2,
                100, 10, "Youtube Channel").withAction(buttonClickInfo -> DesktopUtils.openURI("https://www.youtube.com/@Bebra_tyan")));
        this.buttons.add(Button.of(2, mc.getWindow().getScaledWidth() / 2, (mc.getWindow().getScaledHeight() / 2) + 22,
                100, 10, "Discord Channel").withAction(buttonClickInfo -> DesktopUtils.openURI("https://discord.gg/xecWXN97s6")));
        this.buttons.add(Button.of(3, mc.getWindow().getScaledWidth() / 2, (mc.getWindow().getScaledHeight() / 2) + 44,
                100, 10, "My GitHub").withAction(buttonClickInfo -> DesktopUtils.openURI("https://github.com/Ferra13671")));


        this.buttons.add(Button.of(10, mc.getWindow().getScaledWidth() / 2, mc.getWindow().getScaledHeight() - 22,
                100, 10, "Back").withAction(buttonClickInfo -> mc.setScreen(parent)));


        this.buttons.forEach(button -> button.outline = true);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        BThackMainMenuScreen.drawWallpaper(mouseX, mouseY);

        BThackRender.drawVerticalGradientRect(0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), ColorUtils.TRANSPARENT, ColorUtils.fastRGBA(0,0,0, 240));

        super.render(context, mouseX, mouseY, partialTicks);
    }
}
