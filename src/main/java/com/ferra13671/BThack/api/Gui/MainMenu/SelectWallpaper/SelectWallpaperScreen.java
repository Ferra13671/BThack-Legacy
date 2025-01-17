package com.ferra13671.BThack.api.Gui.MainMenu.SelectWallpaper;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigSystem;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Gui.MainMenu.BThackMainMenuScreen;
import com.ferra13671.BThack.api.Utils.System.BThackScreen;
import com.ferra13671.BThack.api.Utils.System.buttons.Button;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.io.IOException;
import java.util.*;

public class SelectWallpaperScreen extends BThackScreen {

    public final ArrayList<WallpaperImageButton> imageButtons = new ArrayList<>();

    public static final ArrayList<Wallpaper> wallpapers = new ArrayList<>();

    public Wallpaper preSelectWallpaper;

    private Button confirmButton;

    private double maxYScroll;

    public SelectWallpaperScreen() {
        super(Text.of("Select Wallpaper"));
    }

    @Override
    protected void init() {
        ConfigSystem.refreshWallpapers();

        imageButtons.clear();
        this.buttons.clear();

        int offset = 1;

        WallpaperImageButton defaultWallpaper = new WallpaperImageButton(0,
                getX100P() * 13, 15,
                getX100P() * 10, 10,
                new Wallpaper("default", Client.clientInfo.getDefaultMainMenuImage()));
        defaultWallpaper.setOffset(0);
        imageButtons.add(defaultWallpaper);

        for (Wallpaper wallpaper : wallpapers) {
            WallpaperImageButton button = new WallpaperImageButton(0,
                    getX100P() * 13, 15,
                    getX100P() * 10, 10,
                    wallpaper);
            imageButtons.add(button);
            button.setOffset(offset);
            offset++;
        }
        maxYScroll = imageButtons.getLast().getCenterY();

        confirmButton = Button.of(3, mc.getWindow().getScaledWidth() - 55, mc.getWindow().getScaledHeight() - 35, 40, 10, "lang.screen.SelectWallpaper.Confirm");
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawMainMenuWallpaper(mouseX, mouseY);

        BThackRender.drawVerticalGradientRect(0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), ColorUtils.TRANSPARENT, ColorUtils.fastRGBA(0,0,0, 240));

        BThackRender.drawRect(getX100P() * 2, 0, getX100P() * 24, mc.getWindow().getScaledHeight(), ColorUtils.fastRGBA(0,0,0, 140));
        BThackRender.drawRect(getX100P() * 27, 20, mc.getWindow().getScaledWidth() - 10, mc.getWindow().getScaledHeight() - 20, ColorUtils.fastRGBA(0,0,0, 140));

        if (preSelectWallpaper != null) {
            BThackRender.drawTextureRect(this.preSelectWallpaper.texture(), (float)(this.getX100P() * 45), 50.0F, (float)(mc.getWindow().getScaledWidth() - 70), (float)(mc.getWindow().getScaledHeight() - 50));

            confirmButton.updateButton(mouseX, mouseY);
            confirmButton.renderButton();
        }

        for (WallpaperImageButton button : imageButtons) {
            button.renderButton();
        }

        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        imageButtons.forEach(button -> button.setSelected(false));

        for (WallpaperImageButton button : imageButtons) {
            if (button.isMouseOnButton((int) mouseX, (int) mouseY)) {
                button.mouseClicked((int) mouseX, (int) mouseY, mouseButton);
            }
            if (button.isSelected()) {
                preSelectWallpaper = button.wallpaper;
                return false;
            }
        }

        if (preSelectWallpaper != null && confirmButton.isMouseOnButton((int) mouseX, (int) mouseY)) {
            confirmButton.mouseClicked((int) mouseX, (int) mouseY, mouseButton);
            BThackMainMenuScreen.mainMenuTexture = preSelectWallpaper.texture();
            try {
                ConfigSystem.saveWallpaper(preSelectWallpaper.filename());
            } catch (IOException ignored) {}
        }

        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        Button button = imageButtons.getLast();
        if ((button.getCenterY() + (verticalAmount * 10)) > maxYScroll)
            return false;

        for (WallpaperImageButton wButton : imageButtons) {
            wButton.setCenterY((int) (wButton.getCenterY() + (verticalAmount * 10)));
        }

        return false;
    }
}
