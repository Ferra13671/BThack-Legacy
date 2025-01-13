package com.ferra13671.BThack.impl.Commands.OtherList;

import com.ferra13671.BThack.api.Managers.Command.AbstractCommand;
import com.ferra13671.BThack.api.Plugin.Plugin;
import com.ferra13671.BThack.api.Plugin.PluginSystem;
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
