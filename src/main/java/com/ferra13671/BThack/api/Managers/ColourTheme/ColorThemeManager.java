package com.ferra13671.BThack.api.Managers.ColourTheme;

import java.util.ArrayList;

public class ColorThemeManager {
    private final ArrayList<ColorTheme> colorThemes = new ArrayList<>();

    public void addColorTheme(ColorTheme in){
        this.colorThemes.add(in);
    }

    public ArrayList<ColorTheme> getColorThemes(){
        return this.colorThemes;
    }
}
