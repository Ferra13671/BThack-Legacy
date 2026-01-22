package com.ferra13671.BThack.impl.commands;

import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.impl.command.AbstractCommand;
import com.ferra13671.BThack.managers.impl.command.Arguments;
import com.ferra13671.BThack.api.utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

import java.io.IOException;

public class AutoAuthCommand extends AbstractCommand {
    public AutoAuthCommand() {
        super("lang.command.AutoAuth.description", "autoAuth");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("add").then(arg("player name", Arguments.STRING_ONE).then(arg("password", Arguments.STRING_ONE).executes(context -> {
            String playerName = context.getArgument("player name", String.class);
            String password = context.getArgument("password", String.class);

            String text = (Managers.AUTO_AUTH_MANAGER.contains(playerName) ? LanguageSystem.translate("lang.command.AutoAuth.successfulRewrite") : LanguageSystem.translate("lang.command.AutoAuth.successfulSave"));
            Managers.AUTO_AUTH_MANAGER.put(playerName, password);
            try {
                Managers.AUTO_AUTH_MANAGER.save();
            } catch (IOException ignored) {}
            ChatUtils.sendMessage(Formatting.AQUA + String.format(text, playerName));
            return SUCCESFUL;
        }))));
        builder.then(literal("remove").then(arg("player name", Arguments.AUTO_AUTH_PLAYERS).executes(context -> {
            String playerName = context.getArgument("player name", String.class);

            Managers.AUTO_AUTH_MANAGER.remove(playerName);
            try {
                Managers.AUTO_AUTH_MANAGER.save();
            } catch (IOException ignored) {}
            sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.AutoAuth.successfulRemove"), playerName));

            return SUCCESFUL;
        })));
    }
}
