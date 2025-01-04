package com.ferra13671.BThack.impl.Commands.ItemList;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Utils.List.ItemList.ItemList;

public class EditItemListCommand extends AbstractCommand {
    public final String descName;
    public final ItemList itemList;

    public EditItemListCommand(String descName, String alias, ItemList itemList) {
        super("lang.command.ListItem.description", alias + " [clear] / [[add/remove] [item_name]]", alias
        );
        this.descName = descName;
        this.itemList = itemList;
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
                    itemList.preAction(args[1]);
                    if (itemList.item == null) return;

                    switch (args[0]) {
                        case "add":
                            itemList.addToList(args[1]);
                            itemList.postAction();
                            break;
                        case "remove":
                            itemList.removeFromList(args[1]);
                            itemList.postAction();
                            break;
                    }
                } else invalidArgumentError();
            } else {
                itemList.clearList();
                itemList.postAction();
            }
        }
    }
}
