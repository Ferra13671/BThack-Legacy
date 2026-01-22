package com.ferra13671.BThack.impl.commands;

import com.ferra13671.BThack.core.client.Client;
import com.ferra13671.BThack.core.client.systems.config.ConfigUtils;
import com.ferra13671.BThack.core.client.systems.config.SubConfigs;
import com.ferra13671.BThack.core.client.systems.file.JsonUtils;
import com.ferra13671.BThack.managers.impl.command.AbstractCommand;
import com.ferra13671.BThack.managers.impl.command.Arguments;
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
                ConfigUtils.loadFromJson("ClientInfo", "", jsonObject -> {
                    if (!JsonUtils._null(jsonObject, "prefix")) Client.clientInfo.setChatPrefix(jsonObject.get("prefix").getAsString());
                }, () -> {});
            } catch (IOException ignored) {}
            sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Prefix.loaded"));
            return SUCCESFUL;
        }));
        builder.then(literal("set").then(arg("prefix", Arguments.GREEDY_STRING).executes(context -> {
            String newPrefix = context.getArgument("prefix", String.class);

            Client.clientInfo.setChatPrefix(newPrefix);

            SubConfigs.CLIENT_INFO.save();

            sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.Prefix.changed"), Formatting.WHITE + newPrefix + Formatting.AQUA));
            return SUCCESFUL;
        })));
    }
}
