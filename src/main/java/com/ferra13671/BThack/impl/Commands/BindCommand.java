package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;

public class BindCommand extends AbstractCommand {

    public BindCommand() {
        super("lang.command.Bind.description", "bind/b [module] [key / clear]", "bind", "b"
        );
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            invalidArgumentError();
            return;
        }

        Module module = Client.getModuleByName(args[0]);

        if (module == null) {
            ChatUtils.sendMessage(Formatting.RED + String.format(LanguageSystem.translate("lang.command.Bind.noFindModule"), Formatting.WHITE + args[0] + Formatting.RED));
            return;
        }

        module.setKey(args[1].equals("clear") ? KeyboardUtils.RELEASE : KeyboardUtils.getKeyIndex(args[1]));
        ChatUtils.sendMessage(String.format(Formatting.AQUA + LanguageSystem.translate("lang.command.Bind.bound"), Formatting.WHITE + module.getName() + Formatting.AQUA, Formatting.WHITE + (module.getKey() == KeyboardUtils.RELEASE ? "NONE" : args[1].toUpperCase()) + Formatting.AQUA));
    }
}
