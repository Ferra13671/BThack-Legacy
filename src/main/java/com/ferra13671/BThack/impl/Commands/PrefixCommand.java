package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigSystem;
import com.ferra13671.BThack.api.Managers.managers.Command.AbstractCommand;
import com.ferra13671.BThack.api.Managers.managers.Command.Arguments;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

import java.io.IOException;

public class PrefixCommand extends AbstractCommand {
    public PrefixCommand() {
        super("lang.command.Prefix.description", "prefix");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("reload").executes(context -> {
            try {
                ConfigSystem.loadPrefix();
            } catch (IOException ignored) {}
            sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Prefix.loaded"));
            return SUCCESFUL;
        }));
        builder.then(literal("set").then(arg("prefix", Arguments.GREEDY_STRING).executes(context -> {
            String newPrefix = context.getArgument("prefix", String.class);

            Client.clientInfo.setChatPrefix(newPrefix);

            try {
                ConfigSystem.savePrefix();
                ConfigSystem.loadPrefix();
            } catch (IOException ignored) {}

            sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.Prefix.changed"), Formatting.WHITE + newPrefix + Formatting.AQUA));
            return SUCCESFUL;
        })));
    }
}
