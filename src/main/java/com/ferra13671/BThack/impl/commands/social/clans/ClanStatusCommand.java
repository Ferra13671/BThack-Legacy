package com.ferra13671.BThack.impl.commands.social.clans;

import com.ferra13671.BThack.managers.impl.command.AbstractCommand;
import com.ferra13671.BThack.managers.impl.command.Arguments;
import com.ferra13671.BThack.managers.impl.clans.Clan;
import com.ferra13671.BThack.managers.impl.clans.ClanStatus;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public class ClanStatusCommand extends AbstractCommand {
    public ClanStatusCommand() {
        super("lang.command.ClanStatus.description", "clanStatus");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(arg("clan name", Arguments.CLAN).then(arg("mode", Arguments.MODE("Friendly", "Neutral", "Enemy")).executes(context -> {
            Clan clan = context.getArgument("clan name", Clan.class);
            String mode = context.getArgument("mode", String.class);

            clan.setStatus(ClanStatus.valueOf(mode));
            sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.ClanStatus.setStatus"), Formatting.WHITE + clan.getName() + Formatting.AQUA, Formatting.WHITE + mode + Formatting.AQUA));
            return SUCCESFUL;
        })));
    }
}
