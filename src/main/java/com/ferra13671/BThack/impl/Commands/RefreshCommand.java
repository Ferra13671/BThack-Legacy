package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.api.Managers.Command.AbstractCommand;
import com.ferra13671.BThack.api.Social.Clans.ClanManager;
import com.ferra13671.BThack.api.Social.SocialManagers;
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
            SocialManagers.FRIENDS.load();
            sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Refresh.friendRefreshed"));
            return SUCCESFUL;
        }));
        builder.then(literal("enemies").executes(context -> {
            SocialManagers.ENEMIES.load();
            sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Refresh.enemyRefreshed"));
            return SUCCESFUL;
        }));
        builder.then(literal("clans").executes(context -> {
            ClanManager.reload();
            sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Refresh.clanRefreshed"));
            return SUCCESFUL;
        }));
    }
}
