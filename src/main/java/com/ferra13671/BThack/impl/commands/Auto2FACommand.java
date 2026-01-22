package com.ferra13671.BThack.impl.commands;

import com.ferra13671.BThack.core.client.systems.config.SubConfigs;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.impl.command.AbstractCommand;
import com.ferra13671.BThack.managers.impl.command.Arguments;
import com.ferra13671.BThack.api.utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

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
            SubConfigs.TWO_FA_KEYS.save();
            ChatUtils.sendMessage(Formatting.AQUA + String.format(text, playerName));
            return SUCCESFUL;
        }))));
        builder.then(literal("remove").then(arg("player name", Arguments.TWOFA_PLAYERS).executes(context -> {
            String playerName = context.getArgument("player name", String.class);

            Managers.TWOFA_MANAGER.remove(playerName);
            SubConfigs.TWO_FA_KEYS.save();
            sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.Auto2FA.successfulRemove"), playerName));
            return SUCCESFUL;
        })));
    }
}
