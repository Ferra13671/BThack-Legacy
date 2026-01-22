package com.ferra13671.BThack.api.module;

import com.ferra13671.BThack.api.plugin.Plugin;

public class PluginModule extends Module {
    public final Plugin plugin;

    public PluginModule(Plugin plugin) {
        this.plugin = plugin;
    }
}
