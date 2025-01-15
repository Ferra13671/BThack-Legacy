package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.api.Managers.managers.Command.AbstractCommand;
import com.ferra13671.BThack.api.Managers.Managers;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;

public class CleanMemoryCommand extends AbstractCommand {

    public CleanMemoryCommand() {
        super("lang.command.CleanMemory.description", "cleanmemory");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            Managers.MEMORY_MANAGER.cleanMemory();
            return SUCCESFUL;
        });
    }
}
