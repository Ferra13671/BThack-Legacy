package com.ferra13671.BThack.impl.Commands.OtherList;

import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.managers.Command.AbstractCommand;
import com.ferra13671.BThack.managers.managers.Clans.Clan;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public class ClansListCommand extends AbstractCommand {
    public ClansListCommand() {
        super("lang.command.ClanList.description", "clanlist");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            sendMessage(Formatting.AQUA + "|$#> " + Formatting.WHITE + "Clans" + Formatting.AQUA + " <#&|");

            for (Clan clan : Managers.CLAN_MANAGER.getClans()) {
                sendMessage(clan.getName());
                sendMessage(LanguageSystem.translate("lang.command.ClanList.message2"));
                for (String ally : clan.getMembers()) {
                    sendMessage("    " + ally);
                }
            }

            return SUCCESFUL;
        });
    }
}
