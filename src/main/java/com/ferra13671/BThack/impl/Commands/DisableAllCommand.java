package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.api.Category.Categories;
import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;

public class DisableAllCommand extends AbstractCommand {

    public DisableAllCommand() {
        super("lang.command.DisableAll.description", "disableAll", "disableAll"
        );
    }

    @Override
    public void execute(String[] args) {
        for (Module module : Client.getAllModules()) {
            if (!module.getCategory().equals(Categories.CLIENT))
                module.setQuietlyToggled(false);
        }
        ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.DisableAll.message"));
    }
}
