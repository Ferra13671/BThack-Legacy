package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.api.Managers.managers.Command.AbstractCommand;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class CordsCopyCommand extends AbstractCommand {
    public CordsCopyCommand() {
        super("lang.command.CordsCopy.description", "cordsCopy");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            try {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(String.format("%s %s %s", mc.player.getX(), mc.player.getY(), mc.player.getZ())), null);
                sendMessage(Formatting.AQUA + "lang.command.CordsCopy.successfulMessage");
            } catch (Exception e) {
                error(LanguageSystem.translate("lang.command.CordsCopy.errorMessage"));
            }

            return SUCCESFUL;
        });
    }
}
