package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.core.Client.Systems.ConfigSystem.ConfigSystem;
import com.ferra13671.BThack.managers.managers.Command.AbstractCommand;
import com.ferra13671.BThack.managers.managers.Command.Arguments;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

import java.io.IOException;

public class ConfigCommand extends AbstractCommand {
    public ConfigCommand() {
        super("lang.command.Config.description", "config");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("save").then(arg("config_name", Arguments.GREEDY_STRING).executes(context -> {
            try {
                ConfigSystem.saveConfigFile(context.getArgument("config_name", String.class));
            } catch (IOException ignored) {
                error(LanguageSystem.translate("lang.command.Config.saveError"));
                return SUCCESFUL;
            }
            sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Config.successfulSave"));
            return SUCCESFUL;
        })));
        builder.then(literal("load").then(arg("config_name", Arguments.GREEDY_STRING).executes(context -> {
            try {
                ConfigSystem.loadConfigFile(context.getArgument("config_name", String.class));
            } catch (IOException ignored) {
                error(LanguageSystem.translate("lang.command.Config.configNotFound"));
                return SUCCESFUL;
            }
            sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Config.successfulLoad"));
            return SUCCESFUL;
        })));
    }
}
