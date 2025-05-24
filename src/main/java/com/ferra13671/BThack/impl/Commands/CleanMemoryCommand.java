package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.impl.Modules.Misc.CleanMemory;
import com.ferra13671.BThack.managers.managers.Command.AbstractCommand;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;

public class CleanMemoryCommand extends AbstractCommand {

    public CleanMemoryCommand() {
        super("lang.command.CleanMemory.description", "cleanmemory");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            CleanMemory.cleanMemory();
            return SUCCESFUL;
        });
    }
}
