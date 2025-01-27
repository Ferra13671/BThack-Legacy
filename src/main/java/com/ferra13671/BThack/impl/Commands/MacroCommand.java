package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Command.AbstractCommand;
import com.ferra13671.BThack.api.Managers.managers.Command.Arguments;
import com.ferra13671.BThack.api.Managers.managers.Macros.Macro;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClientSettings;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public class MacroCommand extends AbstractCommand {
    public MacroCommand() {
        super("lang.command.Macro.description", "macro");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("add").then(arg("name", Arguments.STRING_ONE).then(arg("key", Arguments.KEY_BIND).then(arg("action", Arguments.GREEDY_STRING).executes(context -> {
            String macroName = context.getArgument("name", String.class);
            Managers.MACROS_MANAGER.addMacro(new Macro(macroName, context.getArgument("key", Integer.class), context.getArgument("action", String.class)));

            sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.Macro.added"), macroName));

            return SUCCESFUL;
        })))));
        builder.then(literal("remove").then(arg("macro", Arguments.MACRO).executes(context -> {
            Macro macro = context.getArgument("macro", Macro.class);
            Managers.MACROS_MANAGER.removeMacro(macro);

            sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.Macro.removed"), macro.getName()));

            return SUCCESFUL;
        })));
        builder.then(literal("list").executes(context -> {
            sendMessage(Formatting.AQUA + "|$#> " + ClientSettings.getFriendColor() + "Macros" + Formatting.AQUA + " <#&|");
            Managers.MACROS_MANAGER.forEach(macro -> sendMessage(Formatting.GRAY + " <" + Formatting.WHITE + macro.getName() + Formatting.GRAY + "> <" + Formatting.WHITE + macro.getKey() + Formatting.GRAY + "> <" + Formatting.WHITE + macro.getAction() + Formatting.GRAY + ">"));
            return SUCCESFUL;
        }));
        builder.then(literal("change").then(arg("macro", Arguments.MACRO)
                .then(literal("setName").then(arg("name", Arguments.STRING_ONE).executes(context -> {
                    Macro macro = context.getArgument("macro", Macro.class);
                    String oldName = macro.getName();
                    String newName = context.getArgument("name", String.class);
                    macro.setName(newName);

                    sendMessage(String.format(LanguageSystem.translate("lang.command.Macro.changedValue"), oldName, Formatting.AQUA, Formatting.WHITE + "Name: " + oldName + Formatting.AQUA, Formatting.WHITE + newName));

                    return SUCCESFUL;
                })))
                .then(literal("setKey").then(arg("key", Arguments.KEY_BIND).executes(context -> {
                    Macro macro = context.getArgument("macro", Macro.class);
                    int oldKey = macro.getKey();
                    int newKey = context.getArgument("key", Integer.class);
                    macro.setKey(newKey);

                    sendMessage(String.format(LanguageSystem.translate("lang.command.Macro.changedValue"), macro.getName(), Formatting.AQUA, Formatting.WHITE + "Key: " + oldKey + Formatting.AQUA, Formatting.WHITE + "" + newKey));

                    return SUCCESFUL;
                })))
                .then(literal("setAction").then(arg("action", Arguments.GREEDY_STRING).executes(context -> {
                    Macro macro = context.getArgument("macro", Macro.class);
                    String oldAction = macro.getAction();
                    String newAction = context.getArgument("action", String.class);

                    sendMessage(String.format(LanguageSystem.translate("lang.command.Macro.changedValue"), macro.getName(), Formatting.AQUA, Formatting.WHITE + "Action: <" + oldAction + ">" + Formatting.AQUA, Formatting.WHITE + "<" + newAction + ">"));

                    return SUCCESFUL;
                })))
        ));
    }
}
