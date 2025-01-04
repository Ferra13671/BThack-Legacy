package com.ferra13671.BThack.api.CommandSystem;

import com.ferra13671.BThack.api.CommandSystem.command.ICommand;

import java.util.ArrayList;
import java.util.Arrays;

public final class CommandManager {

    public static final ArrayList<ICommand> commands = new ArrayList<>();
    public static boolean ignoresCases = true;

    public static ArrayList<ICommand> getCommands() { return commands; }

    public static void addCommands(ICommand... commands) { CommandManager.commands.addAll(Arrays.asList(commands)); }

    public static void parseCommand(String... lines) {
        for (String line : lines) {
            String[] split = line.split(" ");
            String cmdName = split[0];
            String[] args = Arrays.copyOfRange(split, 1, split.length);

            for (ICommand command : commands) {
                for (String alias : command.getAliases()) {
                    if (ignoresCases ? cmdName.equalsIgnoreCase(alias) : cmdName.equals(alias)) {
                        command.execute(args);
                    }
                }
            }
        }
    }
}