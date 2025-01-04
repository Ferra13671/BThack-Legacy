package com.ferra13671.BThack.impl.Commands.OtherList;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Social.Clans.Ally;
import com.ferra13671.BThack.api.Social.Clans.Clan;
import com.ferra13671.BThack.api.Social.Clans.ClansUtils;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;

public class ClansListCommand extends AbstractCommand {

    public ClansListCommand() {
        super("lang.command.ClanList.description", "clanlist", "clanlist"
        );
    }

    @Override
    public void execute(String[] args) {
        ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.ClanList.message1"));

        for (Clan clan : ClansUtils.clans) {
            ChatUtils.sendMessage(clan.getName());
            ChatUtils.sendMessage(LanguageSystem.translate("lang.command.ClanList.message2"));
            for (Ally ally : clan.members) {
                ChatUtils.sendMessage("    " + ally.name());
            }
        }
    }
}
