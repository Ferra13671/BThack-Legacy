package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.Memory.MemoryManager;

public class CleanMemoryCommand extends AbstractCommand {

    public CleanMemoryCommand() {
        super("lang.command.CleanMemory.description", "cleanmemory", "cleanmemory"
        );
    }

    @Override
    public void execute(String[] args) {
        Managers.MEMORY_MANAGER.cleanMemory();
    }
}
