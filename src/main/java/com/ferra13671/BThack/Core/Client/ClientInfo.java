package com.ferra13671.BThack.Core.Client;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Managers.managers.Cape.CapeManager;
import com.ferra13671.BThack.api.Managers.managers.ColourTheme.ColorTheme;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.TextureUtils.GLTexture;
import com.ferra13671.TextureUtils.PathMode;

public final class ClientInfo implements Mc {
    private String name = "BThack " + BThack.instance.VERSION + " | " + mc.getSession().getUsername();
    private final String cName = "BThack " + BThack.instance.VERSION;
    private final GLTexture defaultMainMenuImage = GLTexture.fromPath("assets/bthack/bthack_mainmenu.jpg", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA);
    private String chatPrefix = "$";
    private String wallpaper = "default";
    private String font = "default";
    private String currentConfigName = "";
    private ColorTheme colorTheme;
    private CapeInfo capeInfo = CapeManager.DEFAULT_CAPE_INFO;

    ClientInfo() {}

    public void updateName() {
        name = "BThack " + BThack.instance.VERSION + " | " + mc.getSession().getUsername();
    }

    //get
    public String getName() {
        return name;
    }

    public String getCName() {
        return cName;
    }

    public GLTexture getDefaultMainMenuImage() {
        return defaultMainMenuImage;
    }

    public String getChatPrefix() {
        return chatPrefix;
    }

    public String getCurrentConfigName() {
        return currentConfigName;
    }

    public ColorTheme getColorTheme() {
        return colorTheme;
    }

    public String getWallpaper() {
        return wallpaper;
    }

    public String getFont() {
        return font;
    }

    public CapeInfo getCapeInfo() {
        return capeInfo;
    }

    //set
    public void setChatPrefix(String chatPrefix) {
        this.chatPrefix = chatPrefix;
    }

    public void setCurrentConfigName(String currentConfigName) {
        this.currentConfigName = currentConfigName;
    }

    public void setColorTheme(ColorTheme colorTheme) {
        this.colorTheme = colorTheme;
    }

    public void setWallpaper(String wallpaper) {
        this.wallpaper = wallpaper;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public void setCapeInfo(CapeInfo capeInfo) {
        this.capeInfo = capeInfo;
    }

    public record CapeInfo(String dataPath, CapeDataType dataType) {}
    public enum CapeDataType {
        NONE,
        FILE,
        URL
    }
}
