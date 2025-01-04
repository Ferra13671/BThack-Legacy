package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;

public class ResetModuleCommand extends AbstractCommand {

    public ResetModuleCommand() {
        super("lang.command.ResetModule.description", "resetModule [module_name]", "resetModule"
        );
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            invalidArgumentError();
            return;
        }
        Module module = Client.getModuleByName(args[0]);
        if (module == null) {
            ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.ResetModule.moduleNotFound"));
            return;
        }
        for (Setting setting : Managers.SETTINGS_MANAGER.getSettingsByMod(module)) {
            setting.toDefault();
        }
        ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.ResetModule.successfulResetting"));
    }
}
