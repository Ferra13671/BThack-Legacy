package com.ferra13671.BThack.api.Gui.MainMenu;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Gui.MainMenu.SelectWallpaper.SelectWallpaperScreen;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.SoundSystem.Sounds;
import com.ferra13671.BThack.api.Utils.System.BThackScreen;
import com.ferra13671.BThack.api.Utils.System.buttons.Button;
import com.ferra13671.BThack.impl.Modules.CLIENT.BThackMainMenu;
import com.ferra13671.BThack.impl.Modules.CLIENT.HUD;
import com.ferra13671.TextureUtils.GLTexture;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.text.Text;

public class BThackMainMenuScreen extends BThackScreen {
    private static boolean firstEntered = true;
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
                "lang.screen.MainMenu.Singleplayer"));
        buttons.add(Button.of(2, mc.getWindow().getScaledWidth() / 7, (mc.getWindow().getScaledHeight() / 2) + 22,
                (mc.getWindow().getScaledWidth() / 7) - 5, 10,
                "lang.screen.Mainmenu.Multiplayer"));
        buttons.add(Button.of(3, mc.getWindow().getScaledWidth() / 7, (mc.getWindow().getScaledHeight() / 2) + 44,
                (mc.getWindow().getScaledWidth() / 7) - 5, 10,
                "lang.screen.Mainmenu.Options"));
        buttons.add(Button.of(4, mc.getWindow().getScaledWidth() / 7, (mc.getWindow().getScaledHeight() / 2) + 66,
                (mc.getWindow().getScaledWidth() / 7) - 5, 10,
                "lang.screen.Mainmenu.Quit"));

        buttons.add(Button.of(5, 32, mc.getWindow().getScaledHeight() - 12,
                30, 10,
                "Credits"));
        buttons.add(Button.of(6, mc.getWindow().getScaledWidth() - 40, 15, 38, 10, "lang.screen.Mainmenu.SetWallpaper"));

        buttons.add(Button.of(7, 104, mc.getWindow().getScaledHeight() - 12, 38, 10, "ClickGui"));

        buttons.forEach(button -> button.outline = true);

        if (firstEntered) {
            if (BThackMainMenu.playStartMusic.getValue())
                mc.getSoundManager().play(PositionedSoundInstance.master(Sounds.START.getSoundEvent(), 1, 1));
                //SoundSystem.playSound(Sounds.START);
                //MusicStorage.playSound(MusicStorage.start, mc.options.getSoundVolume(SoundCategory.MASTER) / 2f);
            firstEntered = false;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        switch (activeButton.getId()) {
            case 1 -> mc.setScreen(new SelectWorldScreen(this));
            case 2 -> mc.setScreen(new MultiplayerScreen(this));
            case 3 -> mc.setScreen(new OptionsScreen(this, mc.options));
            case 4 -> mc.stop();
            case 5 -> mc.setScreen(new BThackCreditsScreen(this));
            case 6 -> mc.setScreen(new SelectWallpaperScreen());
            case 7 -> {
                buttons.forEach(button -> {
                    button.allowUpdate = false;
                    button.hovered = false;
                });
                BThack.instance.clickGui.setInstanceScreen(() -> BThackMainMenuScreen.this);
                mc.setScreen(BThack.instance.clickGui);
            }
        }

        return false;
    }

    public static void drawWallpaper(float mouseX, float mouseY) {
        if (ModuleList.menuShader.isEnabled() && ModuleList.bthackMainMenu.isEnabled()) {
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
