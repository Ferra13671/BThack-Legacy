package com.ferra13671.BThack.api.Plugin;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Module.PluginModule;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class PluginUtils {

    private static final ArrayList<PluginModule> pluginsModules = new ArrayList<>();
    private static final ArrayList<HudComponent> pluginsHudComponents = new ArrayList<>();
    private static final ArrayList<AbstractCommand> pluginsCommands = new ArrayList<>();


    public static void addPluginModules(List<PluginModule> pluginModules) {
        pluginsModules.addAll(pluginModules);
    }

    public static void addPluginHudComponents(List<HudComponent> pluginHudComponents) {
        pluginsHudComponents.addAll(pluginHudComponents);
    }

    public static void addPluginCommands(List<AbstractCommand> pluginCommands) {
        pluginsCommands.addAll(pluginCommands);
    }

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
