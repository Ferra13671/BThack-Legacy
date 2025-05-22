package com.ferra13671.BThack.api.Utils.DataList.Commands;

import com.ferra13671.BThack.managers.managers.Command.AbstractCommand;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public abstract class AbstractDataListCommand extends AbstractCommand {

    private final String dataListName;
    private final String messageKey;

    public AbstractDataListCommand(String descriptionKey, String messageKey, String dataListName, String alias) {
        super(descriptionKey, alias + "List");
        this.messageKey = messageKey;
        this.dataListName = dataListName;
    }

    @Override
    public String getDescription() {
        return String.format(super.getDescription(), dataListName);
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate(messageKey), dataListName));

            sendAllList();

            return SUCCESFUL;
        });
    }

    public abstract void sendAllList();
}
