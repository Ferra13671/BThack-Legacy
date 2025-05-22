package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.api.IMixin.ModifyChatHud;
import com.ferra13671.BThack.managers.managers.Command.AbstractCommand;
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
