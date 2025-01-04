package com.ferra13671.BThack.api.Plugin;

public abstract class Plugin {

    public String pluginName;

    public String[] authors;



    public abstract void preInit();

    public abstract void postInit();

    public void onInitModules() {}

    public void onInitHudComponents() {}

    public void onInitCommands() {}

    public void onInitBlockLists() {}

    public void onInitItemLists() {}

    public void onInitCustomCategories() {}

    public void onLoadColourThemes() {}

    public void onLoadLanguages() {}
}
