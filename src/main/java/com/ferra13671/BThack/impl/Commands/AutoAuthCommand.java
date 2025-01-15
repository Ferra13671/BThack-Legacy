package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigSystem;
import com.ferra13671.BThack.api.Managers.managers.Command.AbstractCommand;
import com.ferra13671.BThack.api.Managers.managers.Command.Arguments;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.impl.Modules.MISC.AutoAuth;
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

            String text = (AutoAuth.passwords.containsKey(playerName) ? LanguageSystem.translate("lang.command.AutoAuth.successfulRewrite") : LanguageSystem.translate("lang.command.AutoAuth.successfulSave"));
            AutoAuth.passwords.put(playerName, password);
            try {
                ConfigSystem.saveAutoAuthPasswords();
            } catch (IOException ignored) {}
            ChatUtils.sendMessage(Formatting.AQUA + String.format(text, playerName));
            return SUCCESFUL;
        }))));
        builder.then(literal("remove").then(arg("player name", Arguments.AUTO_AUTH_PLAYERS).executes(context -> {
            String playerName = context.getArgument("player name", String.class);

            AutoAuth.passwords.remove(playerName);
            try {
                ConfigSystem.saveAutoAuthPasswords();
            } catch (IOException ignored) {}
            sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.AutoAuth.successfulRemove"), playerName));

            return SUCCESFUL;
        })));
    }
}
