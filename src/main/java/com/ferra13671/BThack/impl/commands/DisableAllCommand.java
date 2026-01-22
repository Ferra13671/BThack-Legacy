package com.ferra13671.BThack.impl.commands;

import com.ferra13671.BThack.core.client.Client;
import com.ferra13671.BThack.api.category.Categories;
import com.ferra13671.BThack.managers.impl.command.AbstractCommand;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public class DisableAllCommand extends AbstractCommand {

    public DisableAllCommand() {
        super("lang.command.DisableAll.description", "disableAll");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            for (Module module : Client.getAllModules()) {
                if (!module.getCategory().equals(Categories.CLIENT))
                    module.setEnabledQuietly(false);
            }
            sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.DisableAll.message"));
            return SUCCESFUL;
        });
    }
}
