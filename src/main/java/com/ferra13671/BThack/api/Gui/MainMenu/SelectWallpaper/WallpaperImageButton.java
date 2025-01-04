package com.ferra13671.BThack.api.Gui.MainMenu.SelectWallpaper;

import com.ferra13671.BThack.api.Utils.System.buttons.ButtonWithOffset;

public class WallpaperImageButton extends ButtonWithOffset {
    public final Wallpaper wallpaper;

    public WallpaperImageButton(int id, int x, int y, int width, int height, Wallpaper wallpaper) {
        super(id, x, y, width, height, wallpaper.filename());

        this.wallpaper = wallpaper;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0)
            selected = true;
    }
}
