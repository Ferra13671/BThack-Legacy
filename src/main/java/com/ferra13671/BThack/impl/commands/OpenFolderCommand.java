package com.ferra13671.BThack.impl.commands;

import com.ferra13671.BThack.managers.impl.command.AbstractCommand;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Util;

import java.nio.file.Paths;

public class OpenFolderCommand extends AbstractCommand {
    public OpenFolderCommand() {
        super("lang.command.OpenFolder.description", "openFolder");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            Util.getOperatingSystem().open(Paths.get("BThack"));
            return SUCCESFUL;
        });
    }
}
