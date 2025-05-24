package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.managers.managers.Command.AbstractCommand;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.world.GameMode;

public class ClientGamemodeCommand extends AbstractCommand {
    public ClientGamemodeCommand() {
        super("lang.command.ClientGamemode.description", "clientGamemode");
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("survival").executes(context -> {
            mc.interactionManager.setGameMode(GameMode.SURVIVAL);
            return SUCCESFUL;
        }));
        builder.then(literal("creative").executes(context -> {
            mc.interactionManager.setGameMode(GameMode.CREATIVE);
            return SUCCESFUL;
        }));
        builder.then(literal("spectator").executes(context -> {
            mc.interactionManager.setGameMode(GameMode.SPECTATOR);
            return SUCCESFUL;
        }));
        builder.then(literal("adventure").executes(context -> {
            mc.interactionManager.setGameMode(GameMode.ADVENTURE);
            return SUCCESFUL;
        }));
    }
}
