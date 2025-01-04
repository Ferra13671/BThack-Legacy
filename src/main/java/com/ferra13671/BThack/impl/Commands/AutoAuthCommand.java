package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigSystem;
import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.impl.Modules.MISC.AutoAuth;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;

import java.io.IOException;

public class AutoAuthCommand extends AbstractCommand {

    public AutoAuthCommand() {
        super("lang.command.AutoAuth.description", "autoAuth [[add] [player_name] [password]] / [[remove] [player_name]]", "autoAuth"
        );
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            invalidArgumentError();
            return;
        }

        switch (args[0]) {
            case "add" -> {
                if (args.length < 3) {
                    invalidArgumentError();
                    return;
                }
                String text = (AutoAuth.passwords.containsKey(args[1]) ? LanguageSystem.translate("lang.command.AutoAuth.successfulRewrite") : LanguageSystem.translate("lang.command.AutoAuth.successfulSave"));
                AutoAuth.passwords.put(args[1], args[2]);
                try {
                    ConfigSystem.saveAutoAuthPasswords();
                } catch (IOException ignored) {}
                ChatUtils.sendMessage(Formatting.AQUA + String.format(text, args[1]));
            }
            case "remove" -> {
                if (!AutoAuth.passwords.containsKey(args[1])) {
                    ChatUtils.sendMessage(Formatting.YELLOW + String.format(LanguageSystem.translate("lang.command.AutoAuth.notExists"), args[1]));
                    return;
                }
                AutoAuth.passwords.remove(args[1]);
                try {
                    ConfigSystem.saveAutoAuthPasswords();
                } catch (IOException ignored) {}
                ChatUtils.sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.AutoAuth.successfulRemove"), args[1]));
            }
            default -> invalidArgumentError();
        }
    }
}
