package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.api.Managers.Command.AbstractCommand;
import com.ferra13671.BThack.api.Managers.Command.Arguments;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public class ModuleCommand extends AbstractCommand {
    public ModuleCommand() {
        super("lang.command.Module.description", "module");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(arg("module", Arguments.MODULE).then(literal("enable").executes(context -> {
            context.getArgument("module", Module.class).setToggled(true);
            return SUCCESFUL;
        })).then(literal("disable").executes(context -> {
            context.getArgument("module", Module.class).setToggled(false);
            return SUCCESFUL;
        })).then(literal("visible").then(arg("visible", Arguments.BOOLEAN).executes(context -> {
            Module module = context.getArgument("module", Module.class);
            if (!module.allowRemapVisible) error(String.format(LanguageSystem.translate("lang.command.Module.notAllowedRemapVisible"), module.getName()));
            else module.visible = context.getArgument("visible", Boolean.class);
            sendMessage(Formatting.AQUA + LanguageSystem.translate(module.visible ? "lang.command.Module.visible" : "lang.command.Module.notVisible"));
            return SUCCESFUL;
        }))).then(literal("reset").executes(context -> {
            final Module module = context.getArgument("module", Module.class);
            for (Setting setting : Managers.SETTINGS_MANAGER.getSettingsByMod(module)) {
                setting.toDefault();
            }
            sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.Module.reset"), module.getName()));

            return SUCCESFUL;
        })).then(literal("bind").then(literal("set").then(arg("key", Arguments.STRING_ONE).executes(context -> {
            Module module = context.getArgument("module", Module.class);

            if (!module.allowRemapKeyCode) error(String.format(LanguageSystem.translate("lang.command.Module.notAllowedRemapKeyCode"), module.getName()));
            else {

                String key = context.getArgument("key", String.class);
                module.setKey(KeyboardUtils.getKeyIndex(key));
                sendMessage(String.format(Formatting.AQUA + LanguageSystem.translate("lang.command.Bind.bound"), Formatting.WHITE + module.getName() + Formatting.AQUA, Formatting.WHITE + (module.getKey() == KeyboardUtils.RELEASE ? "NONE" : key.toUpperCase()) + Formatting.AQUA));
            }
            return SUCCESFUL;
        })))).then(literal("bind").then(literal("clear").executes(context -> {
            Module module = context.getArgument("module", Module.class);
            if (!module.allowRemapKeyCode) error(String.format(LanguageSystem.translate("lang.command.Module.notAllowedRemapKeyCode"), module.getName()));
            else {
                module.setKey(KeyboardUtils.RELEASE);
                sendMessage(String.format(Formatting.AQUA + LanguageSystem.translate("lang.command.Bind.bound"), Formatting.WHITE + module.getName() + Formatting.AQUA, Formatting.WHITE + "NONE" + Formatting.AQUA));
            }
            return SUCCESFUL;
        }))));
    }
}
