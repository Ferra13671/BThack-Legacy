package com.ferra13671.BThack.api.Managers.managers.ColourTheme;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Utils.Initializable;

import java.util.ArrayList;
import java.util.Objects;

public class ColorThemeManager implements Initializable {
    private final ArrayList<ColorTheme> colorThemes = new ArrayList<>();

    @Override
    public void init() {
        Managers.COLOR_THEME_MANAGER.updateColorTheme();
    }

    public void addColorTheme(ColorTheme in){
        this.colorThemes.add(in);
    }

    public ArrayList<ColorTheme> getColorThemes(){
        return this.colorThemes;
    }

    public void updateColorTheme() {
        for (ColorTheme theme : Managers.COLOR_THEME_MANAGER.getColorThemes()) {
            if (Objects.equals(ModuleList.clickGui.activeTheme.getValue(), theme.name())) {
                Client.clientInfo.setColorTheme(theme);
            }
        }
    }
}
