package com.ferra13671.BThack.api.Utils.DataList;

import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigUtils;
import com.ferra13671.BThack.api.Managers.Command.Arguments;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.DataList.Commands.AbstractDataListCommand;
import com.ferra13671.BThack.api.Utils.DataList.Commands.EditDataListCommand;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

import java.io.IOException;

public class BlockList extends DataList<Block, Block> {

    public BlockList(String descName, String alias, String txtName) {
        super(descName, txtName);
        initEditDataListCommand(new EditBlockListCommand("lang.command.ListBlock.description", descName, alias, this));
        initAbstractDataListCommand(new AbstractDataListCommand("lang.command.ListBlock.description", "lang.command.BlockList.message", descName, alias) {
            @Override
            public void sendAllList() {
                BlockList.this.sendAllList();
            }
        });
    }

    public void saveInFile() throws IOException {
        ConfigUtils.saveInTxt(txtName, editDataListCommand.descName, writer -> {
            for (String blockName : valueNames) {
                try {
                    writer.write(blockName + System.lineSeparator());
                } catch (IOException ignored) {}
            }
        });
    }

    public void loadFromFile() throws IOException {
        ConfigUtils.loadFromTxt(txtName, editDataListCommand.descName, line -> {
            Block block = BlockUtils.getBlockFromNameOrID(line);
            if (block != null) {
                values.add(block);
                valueNames.add(line);
            }
        });
    }

    public void addToList(Block block) {
        if (!values.contains(block)) {
            values.add(block);
            valueNames.add(BlockUtils.getBlockName(block));
            try {
                saveInFile();
            } catch (IOException ignored) {}
            ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.List.blockAdded"));
        } else {
            ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.List.blockAlreadyAdded"));
        }
    }

    public void removeFromList(Block block) {
        if (values.contains(block)) {
            values.remove(block);
            valueNames.remove(BlockUtils.getBlockName(block));
            try {
                saveInFile();
            } catch (IOException ignored) {}
            ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.List.blockRemoved"));
        } else {
            ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.List.blockAlreadyRemoved"));
        }
    }

    public void clearList() {
        values.clear();
        valueNames.clear();
        try {
            saveInFile();
        } catch (IOException ignored) {}
        ChatUtils.sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.List.listCleared"), editDataListCommand.descName));
    }

    public void sendAllList() {
        for (String blockName : valueNames) {
            ChatUtils.sendMessage(blockName);
        }
    }

    public static class EditBlockListCommand extends EditDataListCommand<Block, Block> {

        public EditBlockListCommand(String descriptionKey, String descName, String alias, DataList<Block, Block> dataList) {
            super(descriptionKey, descName, alias, dataList);
        }

        @Override
        public void compile(LiteralArgumentBuilder<CommandSource> builder) {
            builder.then(literal("add").then(arg("block", Arguments.BLOCK).executes(context -> {
                Block block = context.getArgument("block", Block.class);

                dataList.addToList(block);
                dataList.postAction();

                return SUCCESFUL;
            })));
            builder.then(literal("remove").then(arg("block", Arguments.BLOCK).executes(context -> {
                Block block = context.getArgument("block", Block.class);

                dataList.removeFromList(block);
                dataList.postAction();

                return SUCCESFUL;
            })));
            builder.then(literal("clear").executes(context -> {
                dataList.clearList();
                dataList.postAction();

                return SUCCESFUL;
            }));
        }
    }
}
