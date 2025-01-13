package com.ferra13671.BThack.impl.Commands.ItemList;

import com.ferra13671.BThack.api.Managers.Command.AbstractCommand;
import com.ferra13671.BThack.api.Managers.Command.Arguments;
import com.ferra13671.BThack.api.Utils.List.ItemList.ItemList;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.item.Item;

public class EditItemListCommand extends AbstractCommand {
    public final String descName;
    public final ItemList itemList;

    public EditItemListCommand(String descName, String alias, ItemList itemList) {
        super("lang.command.ListItem.description", alias);
        this.descName = descName;
        this.itemList = itemList;
    }

    @Override
    public String getDescription() {
        return String.format(super.getDescription(), descName);
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("add").then(arg("item", Arguments.ITEM).executes(context -> {
            Item item = context.getArgument("item", Item.class);

            itemList.addToList(item);
            itemList.postAction();

            return SUCCESFUL;
        })));
        builder.then(literal("remove").then(arg("item", Arguments.ITEM).executes(context -> {
            Item item = context.getArgument("item", Item.class);

            itemList.removeFromList(item);
            itemList.postAction();

            return SUCCESFUL;
        })));
        builder.then(literal("clear").executes(context -> {
            itemList.clearList();
            itemList.postAction();

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

     */
}
