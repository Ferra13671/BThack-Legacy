package com.ferra13671.BThack.impl.Commands.BlockList;

import com.ferra13671.BThack.api.Managers.Command.AbstractCommand;
import com.ferra13671.BThack.api.Managers.Command.Arguments;
import com.ferra13671.BThack.api.Utils.List.BlockList.BlockList;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;

public class EditBlockListCommand extends AbstractCommand {
    public final String descName;
    public final BlockList blockList;

    public EditBlockListCommand(String descName, String alias, BlockList blockList) {
        super("lang.command.ListBlock.description", alias
        );
        this.descName = descName;
        this.blockList = blockList;
    }

    @Override
    public String getDescription() {
        return String.format(super.getDescription(), descName);
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("add").then(arg("block", Arguments.BLOCK).executes(context -> {
            Block block = context.getArgument("block", Block.class);

            blockList.addToList(block);
            blockList.postAction();

            return SUCCESFUL;
        })));
        builder.then(literal("remove").then(arg("block", Arguments.BLOCK).executes(context -> {
            Block block = context.getArgument("block", Block.class);

            blockList.removeFromList(block);
            blockList.postAction();

            return SUCCESFUL;
        })));
        builder.then(literal("clear").executes(context -> {
            blockList.clearList();
            blockList.postAction();

            return SUCCESFUL;
        }));
    }

    /*
    @Override
    public void execute(String[] args) {
        if (args.length == 0 || !args[0].equals("add") && !args[0].equals("remove") && !args[0].equals("clear")) {
            invalidArgumentError();
        } else {
            if (args[0].equals("add") || args[0].equals("remove")) {
                if (args.length > 1) {
                    if (blockList.block == null) return;

                    switch (args[0]) {
                        case "add":
                            blockList.addToList(args[1]);
                            blockList.postAction();
                            break;
                        case "remove":
                            blockList.removeFromList(args[1]);
                            blockList.postAction();
                            break;
                    }
                } else invalidArgumentError();
            } else {
                blockList.clearList();
                blockList.postAction();
            }
        }
    }

     */
}
