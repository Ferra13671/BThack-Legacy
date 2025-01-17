package com.ferra13671.BThack.api.Gui.MainMenu;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Gui.MainMenu.SelectWallpaper.SelectWallpaperScreen;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Utils.DesktopUtils;
import com.ferra13671.BThack.api.Utils.System.BThackScreen;
import com.ferra13671.BThack.api.Utils.System.buttons.Button;
import com.ferra13671.BThack.impl.Modules.CLIENT.HUD;
import com.ferra13671.TextureUtils.GLTexture;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.text.Text;

public class BThackMainMenuScreen extends BThackScreen {
    public static GLTexture mainMenuTexture = Client.clientInfo.getDefaultMainMenuImage();

    private Animation fadeInAnimation;

    public BThackMainMenuScreen() {
        super(Text.of("BThack Main Menu"));

        Managers.MAIN_MENU_SHADER_MANAGER.setMainMenuShader(ModuleList.menuShader.getShader());
    }

    public void initFadeInAnimation() {
        if (fadeInAnimation == null) fadeInAnimation = new Animation(Easing.LINEAR, 1000);
    }

    @Override
    public void onDisplayed() {
        BThack.instance.versionInfo.setFirstLaunched(false);

        init();
        buttons.forEach(button -> button.setAllowUpdate(true));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {

        drawMainMenuWallpaper(mouseX, mouseY);

        BThackRender.drawHorizontalGradientRect(0,0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), ColorUtils.fastRGBA(0,0,0, 80), ColorUtils.TRANSPARENT);

        BThackRender.drawTextureRect(HUD.bthack_logo, 20, 20, 20 + 138 * 2, 20 + 72 * 2);

        super.render(context, mouseX, mouseY, partialTicks);

        if (fadeInAnimation != null && fadeInAnimation.getEase() < 1) {
            BThackRender.guiGraphics.getMatrices().push();
            BThackRender.guiGraphics.getMatrices().translate(0, 0, 1000);
            BThackRender.drawRect(0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), ColorUtils.integrateAlpha(ColorUtils.BLACK, (int) ((1 - fadeInAnimation.getEase()) * 255)));
            BThackRender.guiGraphics.getMatrices().pop();
        }
    }

    @Override
    protected void init() {
        buttons.clear();

        int baseButtonsX = mc.getWindow().getScaledWidth() / 7;
        int yCenter = mc.getWindow().getScaledHeight() / 2;

        buttons.add(Button.of(1, baseButtonsX, yCenter,
                baseButtonsX - 5, 10,
                "lang.screen.MainMenu.Singleplayer").withAction(buttonClickInfo -> mc.setScreen(new SelectWorldScreen(this))));
        buttons.add(Button.of(2, baseButtonsX, yCenter + 22,
                baseButtonsX - 5, 10,
                "lang.screen.Mainmenu.Multiplayer").withAction(buttonClickInfo -> mc.setScreen(new MultiplayerScreen(this))));
        buttons.add(Button.of(3, baseButtonsX, yCenter + 44,
                baseButtonsX - 5, 10,
                "lang.screen.Mainmenu.Options").withAction(buttonClickInfo -> mc.setScreen(new OptionsScreen(this, mc.options))));
        buttons.add(Button.of(4, baseButtonsX, yCenter + 66,
                baseButtonsX - 5, 10,
                "lang.screen.Mainmenu.Quit").withAction(buttonClickInfo -> mc.stop()));

        buttons.add(Button.of(5, 32, mc.getWindow().getScaledHeight() - 12,
                30, 10,
                "Credits").withAction(buttonClickInfo -> mc.setScreen(new BThackCreditsScreen(this))));
        buttons.add(Button.of(6, 32, mc.getWindow().getScaledHeight() - 34,
                30, 10,
                "Donate").withAction(buttonClickInfo -> DesktopUtils.openURI("https://www.donationalerts.com/r/bebra_tyan")));
        buttons.add(Button.of(7, mc.getWindow().getScaledWidth() - 40, 12, 38, 10, "lang.screen.Mainmenu.SetWallpaper")
                .withAction(buttonClickInfo -> mc.setScreen(new SelectWallpaperScreen())));

        buttons.add(Button.of(8, 102, mc.getWindow().getScaledHeight() - 12, 38, 10, "ClickGui")
                .withAction(buttonClickInfo -> {
                    buttons.forEach(button -> {
                        button.setAllowUpdate(false);
                        button.setHovered(false);
                    });
                    BThack.instance.clickGui.setInstanceScreen(() -> BThackMainMenuScreen.this);
                    mc.setScreen(BThack.instance.clickGui);
                }));
    }
}
