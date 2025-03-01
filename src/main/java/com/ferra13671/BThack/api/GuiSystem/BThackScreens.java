package com.ferra13671.BThack.api.GuiSystem;

import com.ferra13671.BThack.api.Gui.Screen.ClickGui.ClickGuiScreen;
import com.ferra13671.BThack.api.Gui.Screen.ExitScreen;
import com.ferra13671.BThack.api.Gui.Screen.HudEditor.HudEditorScreen;
import com.ferra13671.BThack.api.Gui.Screen.MainMenu.BThackCreditsScreen;
import com.ferra13671.BThack.api.Gui.Screen.MainMenu.BThackMainMenuScreen;

public class BThackScreens {
    private static boolean inited = false;

    public static ClickGuiScreen CLICK_GUI;
    public static BThackMainMenuScreen BTHACK_MAIN_MENU;
    public static HudEditorScreen HUD_EDITOR;
    public static BThackCreditsScreen BTHACK_CREDITS;
    public static ExitScreen EXIT;


    public static void init() {
        if (inited) return;

        CLICK_GUI = new ClickGuiScreen();
        BTHACK_MAIN_MENU = new BThackMainMenuScreen();
        HUD_EDITOR = new HudEditorScreen();
        BTHACK_CREDITS = new BThackCreditsScreen();
        EXIT = new ExitScreen();

        inited = true;
    }
}
