package com.ferra13671.BThack.impl.Commands.Social.Clans;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Social.Clans.Clan;
import com.ferra13671.BThack.api.Social.Clans.ClansUtils;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;

public class ClanStatusCommand extends AbstractCommand {

    public ClanStatusCommand() {
        super("lang.command.ClanStatus.description", "clanStatus [clanName] [Friendly / Neutral / Enemy]", "clanStatus"
        );
    }

    @Override
    public void execute(String[] args) {
        if (args.length > 1) {
            Clan clan = ClansUtils.getClan(args[0]);

            if (clan != null) {
                if (clan.setStatus(args[1])) {
                    ChatUtils.sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.ClanStatus.setStatus"), args[0], args[1]));
                } else {
                    ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.ClanStatus.incorrectStatus"));
                }
            } else {
                ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.ClanMembers.noSuchClan"));
            }
        } else
            invalidArgumentError();
    }
}
