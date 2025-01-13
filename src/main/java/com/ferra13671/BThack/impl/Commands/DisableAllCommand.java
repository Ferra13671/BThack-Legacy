package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.api.Category.Categories;
import com.ferra13671.BThack.api.Managers.Command.AbstractCommand;
import com.ferra13671.BThack.api.Module.Module;
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
                    module.setQuietlyToggled(false);
            }
            sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.DisableAll.message"));
            return SUCCESFUL;
        });
    }
}
