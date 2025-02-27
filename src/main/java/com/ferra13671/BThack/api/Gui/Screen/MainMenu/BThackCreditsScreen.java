package com.ferra13671.BThack.api.Gui.Screen.MainMenu;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Utils.DesktopUtils;
import com.ferra13671.BThack.api.GuiSystem.Screen.BThackScreen;
import com.ferra13671.BThack.api.GuiSystem.BThackScreens;
import com.ferra13671.BThack.api.GuiSystem.buttons.Button;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class BThackCreditsScreen extends BThackScreen {

    public BThackCreditsScreen() {
        super(Text.of("CreditsScreen"));
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
                100, 10, "Back").withAction(buttonClickInfo -> actionAfterClicking(() -> changeScreen(BThackScreens.BTHACK_MAIN_MENU))));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawMainMenuWallpaper(mouseX, mouseY);

        BThackRender.drawVerticalGradientRect(0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), ColorUtils.TRANSPARENT, ColorUtils.fastRGBA(0,0,0, 240));

        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public void close() {
        changeScreen(BThackScreens.BTHACK_MAIN_MENU);
    }
}
