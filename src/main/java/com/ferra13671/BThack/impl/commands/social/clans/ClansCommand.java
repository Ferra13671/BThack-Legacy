package com.ferra13671.BThack.impl.commands.social.clans;

import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.impl.command.AbstractCommand;
import com.ferra13671.BThack.managers.impl.command.Arguments;
import com.ferra13671.BThack.managers.impl.clans.Clan;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public class ClansCommand extends AbstractCommand {
    public ClansCommand() {
        super("lang.command.Clans.description", "clans");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("add").then(arg("clan name", Arguments.CLAN_ADD).then(arg("red", Arguments.INTEGER(0, 255)).then(arg("green", Arguments.INTEGER(0, 255)).then(arg("blue", Arguments.INTEGER(0, 255)).executes(context -> {
            Clan clan = Clan.of(context.getArgument("clan name", String.class),
                    (float) context.getArgument("red", Integer.class) / 255f,
                    (float) context.getArgument("green", Integer.class) / 255f,
                    (float) context.getArgument("blue", Integer.class) / 255f
            );
            Managers.CLAN_MANAGER.addClan(clan);
            sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Clans.clanAdded"));
            return SUCCESFUL;
        }))))));
        builder.then(literal("remove").then(arg("clan name", Arguments.CLAN_REMOVE).executes(context -> {
            Managers.CLAN_MANAGER.removeClan(Managers.CLAN_MANAGER.getClans().stream().filter(clan -> clan.getName().equals(context.getArgument("clan name", String.class))).findFirst().orElse(null));
            sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Clans.clanRemoved"));
            return SUCCESFUL;
        })));
    }
}
