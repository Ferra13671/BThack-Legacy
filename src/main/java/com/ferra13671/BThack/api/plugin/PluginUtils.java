package com.ferra13671.BThack.api.plugin;

import com.ferra13671.BThack.api.module.HudComponent;
import com.ferra13671.BThack.managers.impl.command.AbstractCommand;
import com.ferra13671.BThack.api.module.PluginModule;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class PluginUtils {

    private static final ArrayList<PluginModule> pluginsModules = new ArrayList<>();
    private static final ArrayList<HudComponent> pluginsHudComponents = new ArrayList<>();
    private static final ArrayList<AbstractCommand> pluginsCommands = new ArrayList<>();


    @SuppressWarnings("unused")
    public static void addPluginModules(List<PluginModule> pluginModules) {
        pluginsModules.addAll(pluginModules);
    }

    @SuppressWarnings("unused")
    public static void addPluginHudComponents(List<HudComponent> pluginHudComponents) {
        pluginsHudComponents.addAll(pluginHudComponents);
    }

    @SuppressWarnings("unused")
    public static void addPluginCommands(List<AbstractCommand> pluginCommands) {
        pluginsCommands.addAll(pluginCommands);
    }

    @SuppressWarnings("unused")
    public static void loadPluginTranslations(InputStream inputStream, String lang) {
        LanguageSystem.loadTranslations(inputStream, lang);
    }



    public static List<PluginModule> getPluginsModules() {
        return pluginsModules;
    }

    public static List<HudComponent> getPluginsHudComponents() {
        return pluginsHudComponents;
    }

    public static List<AbstractCommand> getPluginsCommands() {
        return pluginsCommands;
    }
}
