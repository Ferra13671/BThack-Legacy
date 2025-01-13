package com.ferra13671.BThack.impl.Commands.Social.Clans;

import com.ferra13671.BThack.api.Managers.Command.AbstractCommand;
import com.ferra13671.BThack.api.Managers.Command.Arguments;
import com.ferra13671.BThack.api.Social.Clans.ClansUtils;
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
            ClansUtils.addClan(
                    context.getArgument("clan name", String.class),
                    (float) context.getArgument("red", Integer.class) / 255f,
                    (float) context.getArgument("green", Integer.class) / 255f,
                    (float) context.getArgument("blue", Integer.class) / 255f
            );
            sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Clans.clanAdded"));
            return SUCCESFUL;
        }))))));
        builder.then(literal("remove").then(arg("clan name", Arguments.CLAN_REMOVE).executes(context -> {
            ClansUtils.removeClan(context.getArgument("clan name", String.class));
            sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Clans.clanRemoved"));
            return SUCCESFUL;
        })));
    }
}
