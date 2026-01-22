package com.ferra13671.BThack.core.client.systems.gui;

import com.ferra13671.BThack.gui.screen.clickgui.ClickGuiScreen;
import com.ferra13671.BThack.gui.screen.ExitScreen;
import com.ferra13671.BThack.gui.screen.hudeditor.HudEditorScreen;
import com.ferra13671.BThack.gui.screen.mainmenu.BThackCreditsScreen;
import com.ferra13671.BThack.gui.screen.mainmenu.BThackMainMenuScreen;

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
