package com.ferra13671.BThack.managers.impl.command.CustomArguments;

import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FileArgument implements ArgumentType<String> {
    private static final Collection<String> examples = List.of("image.png", "bruh.txt");

    private final File folder;

    public FileArgument(String folderPath) {
        folder = Paths.get(folderPath).toFile();
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String name = reader.readString();

        File file = null;
        File[] files = folder.listFiles();
        if (files != null)
            file = Arrays.stream(files).filter(file1 -> file1.getName().equals(name))
                    .filter(file1 -> checkFileName(file1.getName()))
                    .findFirst()
                    .orElse(null);
        if (file == null)
            throw new DynamicCommandExceptionType(
                    n -> Text.literal(String.format(LanguageSystem.translate("lang.argument.File.exception"), name))
            ).create(reader.readString());
        return name;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        File[] files = folder.listFiles();
        if (files == null) files = new File[]{};
        return CommandSource.suggestMatching(Arrays.stream(files).map(File::getName).filter(this::checkFileName), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return examples;
    }

    public boolean checkFileName(String name) {
        return true;
    }
}
