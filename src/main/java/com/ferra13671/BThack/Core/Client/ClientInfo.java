package com.ferra13671.BThack.Core.Client;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Managers.managers.ColourTheme.ColorTheme;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.TextureUtils.GLTexture;
import com.ferra13671.TextureUtils.PathMode;

public final class ClientInfo implements Mc {
    private String name = "BThack " + BThack.instance.VERSION + " | " + mc.getSession().getUsername();
    private final String cName = "BThack " + BThack.instance.VERSION;
    private final GLTexture defaultMainMenuImage = new GLTexture("assets/bthack/bthack_mainmenu.jpg", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA) {
        @Override
        public GLTexture deleteTexture() {
            return null;
        }
    };
    private String chatPrefix = "$";
    private String currentConfigName = "";
    private ColorTheme colorTheme;

    ClientInfo() {}

    public void updateName() {
        name = "BThack " + BThack.instance.VERSION + " | " + mc.getSession().getUsername();
    }

    public String getName() {
        return name;
    }

    public String getCName() {
        return cName;
    }

    public GLTexture getDefaultMainMenuImage() {
        return defaultMainMenuImage;
    }

    public void setChatPrefix(String chatPrefix) {
        this.chatPrefix = chatPrefix;
    }

    public String getChatPrefix() {
        return chatPrefix;
    }

    public void setCurrentConfigName(String currentConfigName) {
        this.currentConfigName = currentConfigName;
    }

    public String getCurrentConfigName() {
        return currentConfigName;
    }

    public void setColorTheme(ColorTheme colorTheme) {
        this.colorTheme = colorTheme;
    }

    public ColorTheme getColorTheme() {
        return colorTheme;
    }
}
