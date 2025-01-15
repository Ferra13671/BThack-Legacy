package com.ferra13671.BThack.impl.Commands.Social.Clans;

import com.ferra13671.BThack.api.Managers.Command.AbstractCommand;
import com.ferra13671.BThack.api.Managers.Command.Arguments;
import com.ferra13671.BThack.api.Social.Clans.Clan;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public class ClanMembersCommand extends AbstractCommand {
    public ClanMembersCommand() {
        super("lang.command.ClanMembers.description", "clanMembers");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("add").then(arg("clan name", Arguments.CLAN).then(arg("member name", Arguments.GREEDY_STRING).executes(context -> {
            Clan clan = context.getArgument("clan name", Clan.class);
            if (clan.addMember(context.getArgument("member name", String.class)))
                sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.ClanMembers.memberAdded"));
            else
                sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.ClanMembers.memberInTheClan"));
            return SUCCESFUL;
        }))));
        builder.then(literal("remove").then(arg("clan name", Arguments.CLAN).then(arg("member name", Arguments.CLAN_MEMBER).executes(context -> {
            Clan clan = context.getArgument("clan name", Clan.class);
            if (clan.removeMember(context.getArgument("member name", String.class)))
                sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.ClanMembers.memberRemoved"));
            else
                sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.ClanMembers.memberNoLongerInTheClan"));
            return SUCCESFUL;
        }))));
    }
}
