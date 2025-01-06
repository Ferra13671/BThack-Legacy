package com.ferra13671.BThack.api.Managers.ColourTheme;

public class ColorTheme {
    private final String name;

    private final int fontColour;
    private final int backgroundFontColour;
    private final int backgroundFontHoveredColour;
    private final int moduleEnabledColour;
    private final int moduleDisabledColour;
    private final int arrayListColour;

    public ColorTheme(String name , int fontColour, int backgroundFontColour, int backgroundFontHoveredColour, int moduleEnabledColour, int moduleDisabledColour, int arrayListColour) {
        this.name = name;
        this.fontColour = fontColour;
        this.backgroundFontColour = backgroundFontColour;
        this.backgroundFontHoveredColour = backgroundFontHoveredColour;
        this.moduleEnabledColour = moduleEnabledColour;
        this.moduleDisabledColour = moduleDisabledColour;
        this.arrayListColour = arrayListColour;
    }

    public String getName() {
        return this.name;
    }

    public int getFontColour() {
        return this.fontColour;
    }

    public int getBackgroundFontColour() {
        return this.backgroundFontColour;
    }

    public int getBackgroundFontHoveredColour() {
        return this.backgroundFontHoveredColour;
    }

    public int getModuleEnabledColour() {
        return this.moduleEnabledColour;
    }

    public int getModuleDisabledColour() {
        return this.moduleDisabledColour;
    }

    public int getArrayListColour() {
        return this.arrayListColour;
    }
}
