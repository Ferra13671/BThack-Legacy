package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.api.Managers.managers.Command.AbstractCommand;
import com.ferra13671.BThack.api.Managers.managers.Command.Arguments;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public class BindCommand extends AbstractCommand {
    public BindCommand() {
        super("lang.command.Bind.description", "bind");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(arg("module", Arguments.MODULE).then(literal("set").then(arg("key", Arguments.STRING_ONE).executes(context -> {
            Module module = context.getArgument("module", Module.class);

            if (!module.allowRemapKeyCode) error(String.format(LanguageSystem.translate("lang.command.Module.notAllowedRemapKeyCode"), module.getName()));
            else {

                String key = context.getArgument("key", String.class);
                module.setKey(KeyboardUtils.getKeyIndex(key));
                sendMessage(String.format(Formatting.AQUA + LanguageSystem.translate("lang.command.Bind.bound"), Formatting.WHITE + module.getName() + Formatting.AQUA, Formatting.WHITE + (module.getKey() == KeyboardUtils.RELEASE ? "NONE" : key.toUpperCase()) + Formatting.AQUA));
            }
            return SUCCESFUL;
        }))).then(literal("clear").executes(context -> {
            Module module = context.getArgument("module", Module.class);
            if (!module.allowRemapKeyCode) error(String.format(LanguageSystem.translate("lang.command.Module.notAllowedRemapKeyCode"), module.getName()));
            else {
                module.setKey(KeyboardUtils.RELEASE);
                sendMessage(String.format(Formatting.AQUA + LanguageSystem.translate("lang.command.Bind.bound"), Formatting.WHITE + module.getName() + Formatting.AQUA, Formatting.WHITE + "NONE" + Formatting.AQUA));
            }
            return SUCCESFUL;
        })));
    }
}
