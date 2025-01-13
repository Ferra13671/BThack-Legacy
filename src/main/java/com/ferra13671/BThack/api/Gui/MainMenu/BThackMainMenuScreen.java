package com.ferra13671.BThack.api.Gui.MainMenu;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
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

    public BThackMainMenuScreen() {
        super(Text.of("BThack Main Menu"));

        Managers.MAIN_MENU_SHADER_MANAGER.setMainMenuShader(ModuleList.menuShader.getShader());
    }

    @Override
    public void onDisplayed() {
        super.onDisplayed();

        init();
        buttons.forEach(button -> button.allowUpdate = true);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {

        drawWallpaper(mouseX, mouseY);

        BThackRender.drawHorizontalGradientRect(0,0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), ColorUtils.fastRGBA(0,0,0, 80), ColorUtils.TRANSPARENT);

        BThackRender.drawTextureRect(HUD.bthack_logo, 20, 20, 20 + 138 * 2, 20 + 72 * 2);

        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void init() {
        buttons.clear();

        buttons.add(Button.of(1, mc.getWindow().getScaledWidth() / 7, mc.getWindow().getScaledHeight() / 2,
                (mc.getWindow().getScaledWidth() / 7) - 5, 10,
                "lang.screen.MainMenu.Singleplayer").withAction(buttonClickInfo -> mc.setScreen(new SelectWorldScreen(this))));
        buttons.add(Button.of(2, mc.getWindow().getScaledWidth() / 7, (mc.getWindow().getScaledHeight() / 2) + 22,
                (mc.getWindow().getScaledWidth() / 7) - 5, 10,
                "lang.screen.Mainmenu.Multiplayer").withAction(buttonClickInfo -> mc.setScreen(new MultiplayerScreen(this))));
        buttons.add(Button.of(3, mc.getWindow().getScaledWidth() / 7, (mc.getWindow().getScaledHeight() / 2) + 44,
                (mc.getWindow().getScaledWidth() / 7) - 5, 10,
                "lang.screen.Mainmenu.Options").withAction(buttonClickInfo -> mc.setScreen(new OptionsScreen(this, mc.options))));
        buttons.add(Button.of(4, mc.getWindow().getScaledWidth() / 7, (mc.getWindow().getScaledHeight() / 2) + 66,
                (mc.getWindow().getScaledWidth() / 7) - 5, 10,
                "lang.screen.Mainmenu.Quit").withAction(buttonClickInfo -> mc.stop()));

        buttons.add(Button.of(5, 32, mc.getWindow().getScaledHeight() - 12,
                30, 10,
                "Credits").withAction(buttonClickInfo -> mc.setScreen(new BThackCreditsScreen(this))));
        buttons.add(Button.of(6, 32, mc.getWindow().getScaledHeight() - 34,
                30, 10,
                "Donate").withAction(buttonClickInfo -> DesktopUtils.openURI("https://www.donationalerts.com/r/bebra_tyan")));
        buttons.add(Button.of(7, mc.getWindow().getScaledWidth() - 40, 15, 38, 10, "lang.screen.Mainmenu.SetWallpaper")
                .withAction(buttonClickInfo -> mc.setScreen(new SelectWallpaperScreen())));

        buttons.add(Button.of(8, 104, mc.getWindow().getScaledHeight() - 12, 38, 10, "ClickGui")
                .withAction(buttonClickInfo -> {
                    buttons.forEach(button -> {
                        button.allowUpdate = false;
                        button.hovered = false;
                    });
                    BThack.instance.clickGui.setInstanceScreen(() -> BThackMainMenuScreen.this);
                    mc.setScreen(BThack.instance.clickGui);
                }));

        buttons.forEach(button -> button.outline = true);
    }

    public static void drawWallpaper(float mouseX, float mouseY) {
        if (ModuleList.menuShader.isEnabled()) {
            float width = mc.getWindow().getScaledWidth();
            float height = mc.getWindow().getScaledHeight();

            Managers.MAIN_MENU_SHADER_MANAGER.getMainMenuShader().use();
            Managers.MAIN_MENU_SHADER_MANAGER.getMainMenuShader().setParameters(mouseX, mouseY, width, height, Managers.MAIN_MENU_SHADER_MANAGER.getShaderTime());
            BThackRender.drawShader(Managers.MAIN_MENU_SHADER_MANAGER.getMainMenuShader(), 0, 0, width, height);
        } else {
            BThackRender.drawTextureRect(mainMenuTexture, 0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
        }
    }
}
