package com.ferra13671.BThack.api.Utils.DataList;

import com.ferra13671.BThack.Core.Client.Systems.FileSystem.JsonUtils;
import com.ferra13671.BThack.api.Managers.managers.Command.Arguments;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.DataList.Commands.AbstractDataListCommand;
import com.ferra13671.BThack.api.Utils.DataList.Commands.EditDataListCommand;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

import java.io.IOException;

public class BlockList extends DataList<Block, Block> {

    public BlockList(String descName, String alias, String txtName) {
        super(txtName);
        initEditDataListCommand(new EditBlockListCommand("lang.command.ListBlock.description", descName, alias, this));
        initAbstractDataListCommand(new AbstractDataListCommand("lang.command.ListBlock.description", "lang.command.BlockList.message", descName, alias) {
            @Override
            public void sendAllList() {
                BlockList.this.sendAllList();
            }
        });
    }

    @Override
    protected void save(JsonObject jsonObject) {
        JsonArray jsonList = new JsonArray();
        valueNames.forEach(value -> jsonList.add(new JsonPrimitive(value)));
        jsonObject.add("values", jsonList);
    }

    @Override
    protected void load(JsonObject jsonObject) {
        if (!JsonUtils._null(jsonObject, "values")) {
            JsonArray jsonList = jsonObject.get("values").getAsJsonArray();
            jsonList.asList().forEach(jsonElement -> {
                String value = jsonElement.getAsString();
                Block block = BlockUtils.getBlockFromNameOrID(value);
                if (block != null) {
                    values.add(block);
                    valueNames.add(value);
                }
            });
        }
    }

    @Override
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

    @Override
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

    @Override
    public void clearList() {
        values.clear();
        valueNames.clear();
        try {
            saveInFile();
        } catch (IOException ignored) {}
        ChatUtils.sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.List.listCleared"), editDataListCommand.descName));
    }

    @Override
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
