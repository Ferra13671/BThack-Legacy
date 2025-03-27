package com.ferra13671.BThack.api.Gui.Screen.MainMenu;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.GuiSystem.BThackWidgets;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Utils.DesktopUtils;
import com.ferra13671.BThack.api.GuiSystem.Screen.BThackScreen;
import com.ferra13671.BThack.api.GuiSystem.BThackScreens;
import com.ferra13671.BThack.api.GuiSystem.buttons.Button;
import com.ferra13671.BThack.api.Utils.Textures;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.text.Text;

public class BThackMainMenuScreen extends BThackScreen {
    public static boolean firstOpened = true;

    public BThackMainMenuScreen() {
        super(Text.literal("BThack Main Menu"));

        Managers.MAIN_MENU_SHADER_MANAGER.setMainMenuShader(ModuleList.menuShader.getShader());
    }

    @Override
    public void onDisplayed() {
        if (firstOpened) {
            ThreadManager.startNewThread(thread -> {
                thread.sleepThread(1300);
                if (BThack.instance.versionInfo.isFirstLaunched())
                    widgetManage.addWidget(BThackWidgets.LANGUAGE_SELECTOR);
                BThack.instance.versionInfo.setFirstLaunched(false);
                if (BThack.instance.versionInfo.isOutdated()) {
                    if (BThack.instance.versionInfo.isNeedShowAgainAllReleases()) {
                        if (BThack.instance.versionInfo.isNeedShowAgainOneRelease()) {
                            widgetManage.addWidget(BThackWidgets.OUTDATED_VERSION);
                        }
                    }
                }
            });
            firstOpened = false;
        }

        init();
        buttons.forEach(button -> button.setAllowUpdate(true));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {

        drawMainMenuWallpaper(mouseX, mouseY);

        BThackRender.drawHorizontalGradientRect(0,0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), ColorUtils.fastRGBA(0,0,0, 80), ColorUtils.TRANSPARENT);

        BThackRender.drawTextureRect(Textures.BTHACK_LOGO, (mc.getWindow().getScaledWidth() / 2f) - 138, (mc.getWindow().getScaledHeight() / 2f) - 72 - 75, (mc.getWindow().getScaledWidth() / 2f) + 138, (mc.getWindow().getScaledHeight() / 2f) +72 - 75);

        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void init() {
        super.init();
        buttons.clear();

        int xCenter = mc.getWindow().getScaledWidth() / 2;
        int yCenter = mc.getWindow().getScaledHeight() / 2;

        buttons.add(Button.of(1, xCenter, yCenter,
                100, 10,
                "lang.screen.MainMenu.Singleplayer").withAction(buttonClickInfo -> actionAfterClicking(() -> changeScreen(() -> new SelectWorldScreen(this)))));
        buttons.add(Button.of(2, xCenter, yCenter + 22,
                100, 10,
                "lang.screen.Mainmenu.Multiplayer").withAction(buttonClickInfo -> actionAfterClicking(() -> changeScreen(() -> new MultiplayerScreen(this)))));
        buttons.add(Button.of(3, xCenter, yCenter + 44,
                100, 10,
                "lang.screen.Mainmenu.Options").withAction(buttonClickInfo -> actionAfterClicking(() -> changeScreen(() -> new OptionsScreen(this, mc.options)))));
        buttons.add(Button.of(5, xCenter - 51, yCenter + 66,
                49, 10,
                "Credits").withAction(buttonClickInfo -> actionAfterClicking(() -> changeScreen(() -> BThackScreens.BTHACK_CREDITS))));
        buttons.add(Button.of(6, xCenter + 51, yCenter + 66,
                49, 10,
                "Donate").withAction(buttonClickInfo -> DesktopUtils.openURI("https://www.donationalerts.com/r/bebra_tyan")));
        buttons.add(Button.of(8, xCenter, yCenter + 88, 100, 10, "ClickGui")
                .withAction(buttonClickInfo -> {
                    buttons.forEach(button -> {
                        button.setAllowUpdate(false);
                        button.setHovered(false);
                    });
                    mc.setScreen(BThackScreens.CLICK_GUI);
                }));
        buttons.add(Button.of(4, xCenter, yCenter + 110,
                100, 10,
                "lang.screen.Mainmenu.Quit").withAction(buttonClickInfo -> actionAfterClicking(() -> changeScreen(() -> BThackScreens.EXIT, new Animation(Easing.LINEAR, 1000)))));
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
