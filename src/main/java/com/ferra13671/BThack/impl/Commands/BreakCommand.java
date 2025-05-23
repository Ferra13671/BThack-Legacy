package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.managers.managers.Command.AbstractCommand;
import com.ferra13671.BThack.managers.managers.Command.Arguments;
import com.ferra13671.BThack.managers.managers.Break.BreakManager;
import com.ferra13671.BThack.managers.managers.Break.BreakThread3D;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class BreakCommand extends AbstractCommand {
    public BreakCommand() {
        super("lang.command.BreakCommand.description", "break");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(arg("x delta", Arguments.DOUBLE).then(arg("y delta", Arguments.DOUBLE).then(arg("z delta", Arguments.DOUBLE).executes(context -> {
            if (BreakManager.isDestroying) {
                error(LanguageSystem.translate("lang.command.BreakCommand.alreadyDestroying"));
                return SUCCESFUL;
            }
            BreakThread3D thread3D = new BreakThread3D();
            thread3D.set3DSchematic(new ArrayList<>(List.of(new Vec3d(context.getArgument("x delta", Double.class), context.getArgument("y delta", Double.class), context.getArgument("z delta", Double.class)))), BlockPos.ofFloored(mc.player.getX(), mc.player.getY(), mc.player.getZ()));
            thread3D.start();
            return SUCCESFUL;
        }))));
    }
}
