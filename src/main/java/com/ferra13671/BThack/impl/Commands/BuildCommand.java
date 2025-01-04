package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.BThack.api.Utils.ModifyBlockPos;

public class BuildCommand extends AbstractCommand {

    public BuildCommand() {
        super("lang.command.BuildCommand.description", "place [x] [y] [z]", "place"
        );
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 3) {
            invalidArgumentError();
            return;
        }
        if (!MathUtils.isDouble(args[0]) || !MathUtils.isDouble(args[1]) || !MathUtils.isDouble(args[2])) {
            invalidArgumentError();
            return;
        }
        ItemUtils.useItemOnBlock(new ModifyBlockPos(mc.player.getX() + Double.parseDouble(args[0]), mc.player.getY() + Double.parseDouble(args[1]), mc.player.getZ() + Double.parseDouble(args[2])));
    }
}
