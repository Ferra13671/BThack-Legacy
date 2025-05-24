package com.ferra13671.BThack.managers.managers.Command.CustomArguments;

import com.ferra13671.BThack.managers.managers.SocialManager;
import com.ferra13671.BThack.api.Utils.Mc;
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

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SocialAddArgument implements ArgumentType<String>, Mc {
    private static final Collection<String> examples = List.of("bebra_tyan", "player123");

    private final SocialManager socialManager;

    public SocialAddArgument(SocialManager manager) {
        socialManager = manager;
    }


    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String name = reader.readString();

        String player = socialManager.getPlayers().stream()
                .filter(name::equals)
                .findFirst()
                .orElse(null);
        if (player != null)
            throw new DynamicCommandExceptionType(
                    n -> Text.literal(String.format(LanguageSystem.translate("lang.argument.SocialAdd.exception"), player))
            ).create(reader.readString());
        return name;
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(mc.getNetworkHandler().getPlayerList().stream().map(p -> p.getProfile().getName()), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return examples;
    }
}
