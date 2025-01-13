package com.ferra13671.BThack.impl.Commands.BlockList;

import com.ferra13671.BThack.api.Managers.Command.AbstractCommand;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public abstract class AbstractBlockListCommand extends AbstractCommand {

    private final String listName;

    public AbstractBlockListCommand(String listName ,String alias) {
        super("", alias);

        this.listName = listName;
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.BlockList.message"), listName));

            sendAllList();

            return SUCCESFUL;
        });
    }

    @Override
    public String getDescription() {
        return String.format(LanguageSystem.translate("lang.command.BlockList.description"), listName);
    }

    public abstract void sendAllList();
}
