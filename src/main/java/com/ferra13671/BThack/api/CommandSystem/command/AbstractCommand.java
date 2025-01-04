package com.ferra13671.BThack.api.CommandSystem.command;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;

public abstract class AbstractCommand implements ICommand, Mc {
    private final String[] aliases;
    private final String description, usage;

    public AbstractCommand(String description, String usage, String... aliases) {
        this.aliases = aliases;
        this.description = description;
        this.usage = usage;
    }

    @Override public String[] getAliases() { return aliases; }
    @Override public String getDescription() {
        return description.startsWith("lang.") ? LanguageSystem.translate(description) : description;
    }
    @Override public String getUsage() { return usage; }

    public void invalidArgumentError() {
        ChatUtils.sendMessage(Formatting.RED + LanguageSystem.translate("lang.command.invalidargs") + " " + Client.clientInfo.getChatPrefix() + getUsage());
    }
}