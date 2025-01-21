package com.ferra13671.BThack.api.Managers.managers.ColourTheme;

import com.ferra13671.BThack.api.Utils.Initializable;

import java.util.ArrayList;

public class ColorThemeManager implements Initializable {
    private final ArrayList<ColorTheme> colorThemes = new ArrayList<>();

    @Override
    public void init() {
        //no action
    }

    public void addColorTheme(ColorTheme in){
        this.colorThemes.add(in);
    }

    public ArrayList<ColorTheme> getColorThemes(){
        return this.colorThemes;
    }
}
