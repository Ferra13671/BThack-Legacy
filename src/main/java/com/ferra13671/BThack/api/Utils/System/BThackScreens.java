package com.ferra13671.BThack.api.Utils.System;

import com.ferra13671.BThack.api.Gui.ClickGui.ClickGuiScreen;
import com.ferra13671.BThack.api.Gui.ExitScreen;
import com.ferra13671.BThack.api.Gui.HudEditor.HudEditorScreen;
import com.ferra13671.BThack.api.Gui.MainMenu.BThackCreditsScreen;
import com.ferra13671.BThack.api.Gui.MainMenu.BThackMainMenuScreen;
import com.ferra13671.BThack.api.Gui.MainMenu.LanguageSelectorScreen;
import com.ferra13671.BThack.api.Gui.MainMenu.OutdatedVersionScreen;
import com.ferra13671.BThack.api.Gui.MainMenu.SelectWallpaper.SelectWallpaperScreen;

public class BThackScreens {
    private static boolean inited = false;

    public static ClickGuiScreen CLICK_GUI;
    public static BThackMainMenuScreen BTHACK_MAIN_MENU;
    public static HudEditorScreen HUD_EDITOR;
    public static LanguageSelectorScreen LANGUAGE_SELECTOR;
    public static OutdatedVersionScreen OUTDATED_VERSION;
    public static BThackCreditsScreen BTHACK_CREDITS;
    public static SelectWallpaperScreen SELECT_WALLPAPER;
    public static ExitScreen EXIT;


    public static void init() {
        if (inited) return;

        CLICK_GUI = new ClickGuiScreen();
        BTHACK_MAIN_MENU = new BThackMainMenuScreen();
        HUD_EDITOR = new HudEditorScreen();
        LANGUAGE_SELECTOR = new LanguageSelectorScreen();
        OUTDATED_VERSION = new OutdatedVersionScreen();
        BTHACK_CREDITS = new BThackCreditsScreen();
        SELECT_WALLPAPER = new SelectWallpaperScreen();
        EXIT = new ExitScreen();

        inited = true;
    }
}
