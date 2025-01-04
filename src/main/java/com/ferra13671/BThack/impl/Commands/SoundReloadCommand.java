package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;

public class SoundReloadCommand extends AbstractCommand {

    public SoundReloadCommand() {
        super("lang.command.SoundReloadCommand.description", "reloadSounds/rls", "reloadSounds", "rls"
        );
    }

    @Override
    public void execute(String[] args) {
        mc.getSoundManager().reloadSounds();
        ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.SoundReloadCommand.message"));
    }
}
