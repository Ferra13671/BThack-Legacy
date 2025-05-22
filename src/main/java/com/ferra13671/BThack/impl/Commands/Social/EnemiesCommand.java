package com.ferra13671.BThack.impl.Commands.Social;

import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.managers.Command.AbstractCommand;
import com.ferra13671.BThack.managers.managers.Command.Arguments;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public class EnemiesCommand extends AbstractCommand {
    public EnemiesCommand() {
        super("lang.command.Enemies.description", "enemies");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("add").then(arg("enemy", Arguments.SOCIAL_ADD(Managers.ENEMIES_MANAGER)).executes(context -> {
            Managers.ENEMIES_MANAGER.add(context.getArgument("enemy", String.class));
            sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Enemies.enemyAdded"));
            return SUCCESFUL;
        })));
        builder.then(literal("remove").then(arg("enemy", Arguments.SOCIAL_REMOVE(Managers.ENEMIES_MANAGER)).executes(context -> {
            Managers.ENEMIES_MANAGER.remove(context.getArgument("enemy", String.class));
            sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Enemies.enemyRemoved"));
            return SUCCESFUL;
        })));
    }
}
