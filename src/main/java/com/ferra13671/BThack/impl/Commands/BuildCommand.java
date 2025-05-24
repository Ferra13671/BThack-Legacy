package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.managers.managers.Command.AbstractCommand;
import com.ferra13671.BThack.managers.managers.Command.Arguments;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.BlockPos;

public class BuildCommand extends AbstractCommand {
    public BuildCommand() {
        super("lang.command.BuildCommand.description", "place");
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(arg("x delta", Arguments.DOUBLE).then(arg("y delta", Arguments.DOUBLE).then(arg("z delta", Arguments.DOUBLE).executes(context -> {
            ItemUtils.useItemOnBlock(BlockPos.ofFloored(mc.player.getX() + context.getArgument("x delta", Double.class), mc.player.getY() + context.getArgument("y delta", Double.class), mc.player.getZ() + context.getArgument("z delta", Double.class)));
            return SUCCESFUL;
        }))));
    }
}
