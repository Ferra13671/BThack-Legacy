package com.ferra13671.BThack.impl.commands.otherlist;

import com.ferra13671.BThack.managers.impl.command.AbstractCommand;
import com.ferra13671.BThack.api.plugin.Plugin;
import com.ferra13671.BThack.api.plugin.PluginSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public class PluginListCommand extends AbstractCommand {
    public PluginListCommand() {
        super("lang.command.PluginList.description", "pluginList");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("list").executes(context -> {
            sendMessage(Formatting.AQUA + "|$#> " + Formatting.WHITE + "Plugins" + Formatting.AQUA + " <#&|");
            for (Plugin plugin : PluginSystem.getLoadedPlugins()) {
                sendMessage(plugin.pluginName);
            }
            return SUCCESFUL;
        }));
    }
}
