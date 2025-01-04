package com.ferra13671.BThack.impl.Commands.Social.Clans;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Social.Clans.Clan;
import com.ferra13671.BThack.api.Social.Clans.ClansUtils;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;

public class ClanMembersCommand extends AbstractCommand {

    public ClanMembersCommand() {
        super("lang.command.ClanMembers.description", "clanMember/clanMembers [add / remove] [clanName] [memberName]", "clanMember", "clanMembers"
        );
    }

    @Override
    public void execute(String[] args) {
        if (args.length > 2 && (args[0].equals("add") || args[0].equals("remove"))) {
            switch (args[0]) {
                case "add":
                    Clan clan = ClansUtils.getClan(args[1]);
                    if (clan != null) {
                        if (clan.addMemberToClan(args[2])) {
                            ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.ClanMembers.memberAdded"));
                        } else {
                            ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.ClanMembers.memberInTheClan"));
                        }
                    } else {
                        ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.ClanMembers.noSuchClan"));
                    }
                    break;
                case "remove":
                    Clan clan2 = ClansUtils.getClan(args[1]);
                    if (clan2 != null) {
                        if (clan2.removeMemberFromClan(args[2])) {
                            ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.ClanMembers.memberRemoved"));
                        } else {
                            ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.ClanMembers.memberNoLongerInTheClan"));
                        }
                    } else {
                        ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.ClanMembers.noSuchClan"));
                    }
                    break;
            }
        } else
            invalidArgumentError();
    }
}
