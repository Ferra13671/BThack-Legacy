package com.ferra13671.BThack.impl.commands;

import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.impl.command.AbstractCommand;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public class RefreshCommand extends AbstractCommand {
    public RefreshCommand() {
        super("lang.command.Refresh.description", "refresh");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("friends").executes(context -> {
            Managers.FRIENDS_MANAGER.load();
            sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Refresh.friendRefreshed"));
            return SUCCESFUL;
        }));
        builder.then(literal("enemies").executes(context -> {
            Managers.ENEMIES_MANAGER.load();
            sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Refresh.enemyRefreshed"));
            return SUCCESFUL;
        }));
        builder.then(literal("clans").executes(context -> {
            Managers.CLAN_MANAGER.reload();
            sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Refresh.clanRefreshed"));
            return SUCCESFUL;
        }));
    }
}
