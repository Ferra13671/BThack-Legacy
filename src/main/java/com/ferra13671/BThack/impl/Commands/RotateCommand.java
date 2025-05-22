package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.managers.managers.Command.AbstractCommand;
import com.ferra13671.BThack.managers.managers.Command.Arguments;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;

public class RotateCommand extends AbstractCommand {
    public RotateCommand() {
        super("lang.command.Rotate.description", "rotate");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(arg("yaw", Arguments.FLOAT(-30000, 30000)).then(arg("pitch", Arguments.FLOAT(-90, 90)).executes(context -> {
            mc.player.setYaw(context.getArgument("yaw", Float.class));
            mc.player.setPitch(context.getArgument("pitch", Float.class));
            return SUCCESFUL;
        })));
    }
}
