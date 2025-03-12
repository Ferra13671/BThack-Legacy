package com.ferra13671.BThack.api.SoundSystem;

public final class Sounds {
    private static boolean inited = false;

    public static Sound START;
    public static Sound MODULE_ON;
    public static Sound MODULE_OFF;
    public static Sound BUTTON_CLICK;
    public static Sound CONFIG_SAVED_OR_LOADED;


    public static void initSounds() {
        if (inited) return;

        START = new Sound("start");
        MODULE_ON = new Sound("module_on");
        MODULE_OFF = new Sound("module_off");
        BUTTON_CLICK = new Sound("button_click");
        CONFIG_SAVED_OR_LOADED = new Sound("config_saved_or_loaded");

        inited = true;
    }
}
