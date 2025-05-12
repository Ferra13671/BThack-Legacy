package com.ferra13671.BThack.api.Module;

import com.ferra13671.BThack.api.Plugin.Plugin;

public class PluginModule extends Module {
    public final Plugin plugin;

    public PluginModule(Plugin plugin) {
        this.plugin = plugin;
    }
}
