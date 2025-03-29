package com.ferra13671.BThack.api.SoundSystem;

public final class Sounds {
    private static boolean inited = false;

    public static Sound START;
    public static Sound MODULE_ON;
    public static Sound MODULE_OFF;
    public static Sound BUTTON_CLICK;
    public static Sound CONFIG_SAVED_OR_LOADED;
    public static Sound GUI_SLIDER_UP;
    public static Sound GUI_SLIDER_DOWN;
    public static Sound GUI_TYPING;
    public static Sound GUI_WIDGET_SHOW;
    public static Sound GUI_WIDGET_HIDE;
    public static Sound GUI_CHECKBOX_ENABLE;
    public static Sound GUI_CHECKBOX_DISABLE;


    public static void initSounds() {
        if (inited) return;

        START = new Sound("start");
        MODULE_ON = new Sound("module_on");
        MODULE_OFF = new Sound("module_off");
        BUTTON_CLICK = new Sound("button_click");
        CONFIG_SAVED_OR_LOADED = new Sound("config_saved_or_loaded");
        GUI_SLIDER_UP = new Sound("gui_slider_up");
        GUI_SLIDER_DOWN = new Sound("gui_slider_down");
        GUI_TYPING = new Sound("gui_typing");
        GUI_WIDGET_SHOW = new Sound("gui_widget_show");
        GUI_WIDGET_HIDE = new Sound("gui_widget_hide");
        GUI_CHECKBOX_ENABLE = new Sound("gui_checkbox_enable");
        GUI_CHECKBOX_DISABLE = new Sound("gui_checkbox_disable");

        inited = true;
    }
}
