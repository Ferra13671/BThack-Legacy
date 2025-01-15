package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.api.Managers.managers.Command.AbstractCommand;
import com.ferra13671.BThack.api.Managers.managers.Command.Arguments;
import com.ferra13671.BThack.api.Managers.Managers;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public class HelpCommand extends AbstractCommand {
    private int maxPage;

    public HelpCommand() {
        super("lang.command.Help.description", "help");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        maxPage = (int) Math.ceil(Managers.COMMAND_MANAGER.getCommands().size() / 7d);
        builder.then(arg("page", Arguments.INTEGER(0, maxPage)).executes(context -> {
            int page = context.getArgument("page", Integer.class);
            sendMessage(String.format("%sCommands[page: %s]", Formatting.WHITE, Formatting.AQUA.toString() + page + Formatting.WHITE + "/" + Formatting.GRAY + maxPage + Formatting.WHITE));
            page--;
            for (int i = page * 7; i < (page * 7) + 7; i++) {
                if (i >= Managers.COMMAND_MANAGER.getCommands().size()) continue;
                AbstractCommand command = Managers.COMMAND_MANAGER.getCommands().get(i);
                sendMessage(Formatting.GRAY + Client.clientInfo.getChatPrefix() + command.getFormatedAliases() + Formatting.WHITE + " - " + Formatting.YELLOW + command.getDescription() + ";");
            }
            return SUCCESFUL;
        }));
    }
}
