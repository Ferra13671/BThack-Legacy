package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigSystem;
import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import net.minecraft.util.Formatting;

import java.io.IOException;

public class PrefixCommand extends AbstractCommand {

    public PrefixCommand() {
        super("lang.command.Prefix.description", "prefix/p [new_prefix]", "prefix", "p");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            invalidArgumentError();
            return;
        }

        Client.clientInfo.setChatPrefix(args[0]);

        try {
            ConfigSystem.savePrefix();
            ConfigSystem.loadPrefix();
        } catch (IOException ignored) {}
        ChatUtils.sendMessage(Formatting.AQUA + "Prefix rewritten!");
    }
}
