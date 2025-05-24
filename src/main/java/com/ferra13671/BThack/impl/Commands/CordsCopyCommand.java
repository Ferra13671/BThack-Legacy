package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.managers.managers.Command.AbstractCommand;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public class CordsCopyCommand extends AbstractCommand {
    public CordsCopyCommand() {
        super("lang.command.CordsCopy.description", "cordsCopy");
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            try {
                mc.keyboard.setClipboard(String.format("%s %s %s", (int) mc.player.getX(), (int) mc.player.getY(), (int) mc.player.getZ()));
                sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.CordsCopy.successfulMessage"));
            } catch (Exception e) {
                error(LanguageSystem.translate("lang.command.CordsCopy.errorMessage"));
            }

            return SUCCESFUL;
        });
    }
}
