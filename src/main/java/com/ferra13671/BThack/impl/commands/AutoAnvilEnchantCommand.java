package com.ferra13671.BThack.impl.commands;

import com.ferra13671.BThack.managers.impl.command.AbstractCommand;
import com.ferra13671.BThack.managers.impl.command.Arguments;
import com.ferra13671.BThack.api.utils.ItemUtils;
import com.ferra13671.BThack.impl.modules.misc.AutoAnvilEnchant;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Formatting;

import java.util.HashMap;

public class AutoAnvilEnchantCommand extends AbstractCommand {

    public AutoAnvilEnchantCommand() {
        super("lang.command.AutoAnvilEnchant.description", "autoAnvilEnchant");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("add").then(arg("item", Arguments.ITEM).then(arg("enchantment", Arguments.ENCHANTMENT).then(arg("level", Arguments.INTEGER(0, 5)).executes(context -> {
            Item item = context.getArgument("item", Item.class);
            RegistryKey<Enchantment> enchantment = context.getArgument("enchantment", RegistryKey.class);
            int level = context.getArgument("level", Integer.class);
            if (!AutoAnvilEnchant.recipes.containsKey(item)) {
                HashMap<RegistryKey<Enchantment>, Integer> hashMap = new HashMap<>();
                hashMap.put(enchantment, level);
                AutoAnvilEnchant.recipes.put(item, hashMap);
            } else
                AutoAnvilEnchant.recipes.get(item).put(enchantment, level);

            sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.AutoAnvilEnchant.add"), enchantment.getValue().getPath(), level, ItemUtils.getItemName(item)));

            return SUCCESFUL;
        })))));
        builder.then(literal("remove").then(arg("item", Arguments.ITEM).then(arg("enchantment", Arguments.ENCHANTMENT).executes(context -> {
            Item item = context.getArgument("item", Item.class);
            RegistryKey<Enchantment> enchantment = context.getArgument("enchantment", RegistryKey.class);
            if (AutoAnvilEnchant.recipes.containsKey(item))
                AutoAnvilEnchant.recipes.get(item).remove(enchantment);

            sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.AutoAnvilEnchant.remove"), enchantment.getValue().getPath(), ItemUtils.getItemName(item)));

            return SUCCESFUL;
        }))));
        builder.then(literal("clear").then(arg("item", Arguments.ITEM).executes(context -> {
            Item item = context.getArgument("item", Item.class);
            AutoAnvilEnchant.recipes.remove(item);

            sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.AutoAnvilEnchant.clear"), ItemUtils.getItemName(item)));

            return SUCCESFUL;
        })));
        builder.then(literal("list").executes(context -> {
            sendMessage(Formatting.AQUA + "|$#> " + Formatting.RESET + "AutoAnvilEnchant" + Formatting.AQUA + " <#&|");
            AutoAnvilEnchant.recipes.forEach((item, enchants) -> {
                sendMessage(" " + ItemUtils.getItemName(item));
                enchants.forEach((enchant, level) -> sendMessage("  " + Formatting.GRAY + enchant.getValue().getPath() + "(" + level + ")"));
            });

            return SUCCESFUL;
        }));
    }
}
