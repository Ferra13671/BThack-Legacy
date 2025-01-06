package com.ferra13671.BThack.api.Plugin;

import com.ferra13671.BThack.BThack;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;

import java.util.ArrayList;
import java.util.List;

public final class PluginSystem {

    private static final ArrayList<Plugin> loadedPlugins = new ArrayList<>();
    private static boolean pluginsLoaded = false;

    public static void loadPlugins() {
        if (pluginsLoaded) {
            BThack.log("The plugins are already loaded.");
            return;
        }
        for (EntrypointContainer<Plugin> entrypoint : FabricLoader.getInstance().getEntrypointContainers("bthack", Plugin.class)) {

            ModMetadata metadata = entrypoint.getProvider().getMetadata();
            Plugin plugin;
            try {
                plugin = entrypoint.getEntrypoint();
                plugin.pluginName = metadata.getName();
                if (!metadata.getAuthors().isEmpty()) {
                    plugin.authors = new String[metadata.getAuthors().size()];
                    int i = 0;
                    for (Person author : metadata.getAuthors()) {
                        plugin.authors[i++] = author.getName();
                    }
                } else {
                    plugin.authors = new String[]{"None"};
                }
                loadedPlugins.add(plugin);
            } catch (Exception e) {
                BThack.error("An error occurred while trying to initialize the plugin '" + metadata.getName() + "'!");
            }
        }
        pluginsLoaded = true;
    }

    public static List<Plugin> getLoadedPlugins() {
        return loadedPlugins;
    }
}
