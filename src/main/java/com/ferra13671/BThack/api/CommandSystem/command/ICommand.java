package com.ferra13671.BThack.api.CommandSystem.command;

public interface ICommand {
    /**
     * @return all the aliases for the command
     */
    String[] getAliases();

    /**
     * @return the description for the command
     */
    String getDescription();

    /**
     * @return the usage for the command
     */
    String getUsage();

    /**
     * happens when a line that's being parsed starts with one of the {@link #getAliases()}
     * @param args the arguments for the command
     */
    void execute(String[] args);
}