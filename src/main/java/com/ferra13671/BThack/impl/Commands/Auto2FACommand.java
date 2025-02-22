package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigSystem;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Command.AbstractCommand;
import com.ferra13671.BThack.api.Managers.managers.Command.Arguments;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

import java.io.IOException;

public class Auto2FACommand extends AbstractCommand {
    public Auto2FACommand() {
        super("lang.command.Auto2FA.description", "auto2FA");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("add").then(arg("player name", Arguments.STRING_ONE).then(arg("key", Arguments.STRING_ONE).executes(context -> {
            String playerName = context.getArgument("player name", String.class);
            String key = context.getArgument("key", String.class);

            String text = (Managers.TWOFA_MANAGER.contains(playerName) ? LanguageSystem.translate("lang.command.Auto2FA.successfulRewrite") : LanguageSystem.translate("lang.command.Auto2FA.successfulSave"));
            Managers.TWOFA_MANAGER.put(playerName, key);
            try {
                ConfigSystem.save2FAKeys();
            } catch (IOException ignored) {}
            ChatUtils.sendMessage(Formatting.AQUA + String.format(text, playerName));
            return SUCCESFUL;
        }))));
        builder.then(literal("remove").then(arg("player name", Arguments.TWOFA_PLAYERS).executes(context -> {
            String playerName = context.getArgument("player name", String.class);

            Managers.TWOFA_MANAGER.remove(playerName);
            try {
                ConfigSystem.save2FAKeys();
            } catch (IOException ignored) {}
            sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.Auto2FA.successfulRemove"), playerName));
            return SUCCESFUL;
        })));
    }
}
