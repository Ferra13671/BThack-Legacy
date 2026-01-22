package com.ferra13671.BThack.impl.commands;

import com.ferra13671.BThack.api.imixin.ModifyChatHud;
import com.ferra13671.BThack.managers.impl.command.AbstractCommand;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;

public class ClearChatCommand extends AbstractCommand {

    public ClearChatCommand() {
        super("lang.command.ClearChat.description", "clearChat");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            ((ModifyChatHud) mc.inGameHud.getChatHud())._clearChat();

            return SUCCESFUL;
        });
    }
}
