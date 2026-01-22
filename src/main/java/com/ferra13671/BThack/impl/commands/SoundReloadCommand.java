package com.ferra13671.BThack.impl.commands;

import com.ferra13671.BThack.managers.impl.command.AbstractCommand;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public class SoundReloadCommand extends AbstractCommand {
    public SoundReloadCommand() {
        super("lang.command.SoundReloadCommand.description", "reloadSounds");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            mc.getSoundManager().reloadSounds();
            sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.SoundReloadCommand.message"));
            return SUCCESFUL;
        });
    }
}
