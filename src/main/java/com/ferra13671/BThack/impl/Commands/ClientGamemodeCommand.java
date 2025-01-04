package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import net.minecraft.world.GameMode;

public class ClientGamemodeCommand extends AbstractCommand {

    public ClientGamemodeCommand() {
        super("lang.command.ClientGamemode.description", "clientGamemode [ [0/1/2/3] / [survival/creative/spectator/adventure] ]", "clientGamemode"
        );
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            invalidArgumentError();
            return;
        }
        GameMode gameMode = null;
        switch (args[0]) {
            case "0", "survival" -> gameMode = GameMode.SURVIVAL;
            case "1", "creative" -> gameMode = GameMode.CREATIVE;
            case "2", "spectator" -> gameMode = GameMode.SPECTATOR;
            case "3", "adventure" -> gameMode = GameMode.ADVENTURE;
            default -> {
                invalidArgumentError();
                return;
            }
        }

        mc.interactionManager.setGameMode(gameMode);
    }
}
