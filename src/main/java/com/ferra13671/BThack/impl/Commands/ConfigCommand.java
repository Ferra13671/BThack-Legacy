package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigSystem;
import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;

import java.io.IOException;

public class ConfigCommand extends AbstractCommand {

    public ConfigCommand() {
        super("lang.command.Config.description", "config [save / load] [config_name]", "config"
        );
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            invalidArgumentError();
            return;
        }
        switch (args[0]) {
            case "save" -> {
                try {
                    ConfigSystem.saveConfigFile(args[1]);
                } catch (IOException ignored) {
                    ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.Config.saveError"));
                    return;
                }
                ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Config.successfulSave"));
            }
            case "load" -> {
                try {
                    ConfigSystem.loadConfigFile(args[1]);
                } catch (IOException ignored) {
                    ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.Config.configNotFound"));
                    return;
                }
                ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Config.successfulLoad"));
            }
            default -> invalidArgumentError();
        }
    }
}
