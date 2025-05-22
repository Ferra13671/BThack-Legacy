package com.ferra13671.BThack.impl.Commands.OtherList;

import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.managers.Command.AbstractCommand;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public class AutoAuthListCommand extends AbstractCommand {

    public AutoAuthListCommand() {
        super("lang.command.AutoAuthList.description", "autoAuthList");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            sendMessage(Formatting.AQUA + "|$#> " + Formatting.WHITE + "AutoAuth Passwords" + Formatting.AQUA + " <#&|");
            for (String nickname : Managers.AUTO_AUTH_MANAGER.getNames())
                sendMessage(nickname + Formatting.GRAY + " --- " + Formatting.AQUA + Managers.AUTO_AUTH_MANAGER.getPassword(nickname));

            return SUCCESFUL;
        });
    }
}
