package com.ferra13671.BThack.impl.Commands.BlockList;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Utils.List.BlockList.BlockList;

public class EditBlockListCommand extends AbstractCommand {
    public final String descName;
    public final BlockList blockList;

    public EditBlockListCommand(String descName, String alias, BlockList blockList) {
        super("lang.command.ListBlock.description", alias + " [clear] / [[add/remove] [block_name]]", alias
        );
        this.descName = descName;
        this.blockList = blockList;
    }

    @Override
    public String getDescription() {
        return String.format(super.getDescription(), descName);
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0 || !args[0].equals("add") && !args[0].equals("remove") && !args[0].equals("clear")) {
            invalidArgumentError();
        } else {
            if (args[0].equals("add") || args[0].equals("remove")) {
                if (args.length > 1) {
                    blockList.preAction(args[1]);
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
}
