package com.ferra13671.BThack.api.Managers.Command;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.api.Managers.Command.CustomArguments.ModeArgument;
import com.ferra13671.BThack.api.Managers.Command.CustomArguments.PacketListPacketArgument;
import com.ferra13671.BThack.api.Managers.Command.CustomArguments.SocialAddArgument;
import com.ferra13671.BThack.api.Managers.Command.CustomArguments.SocialRemoveArgument;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.Clans.Ally;
import com.ferra13671.BThack.api.Social.Clans.Clan;
import com.ferra13671.BThack.api.Social.Clans.ClansUtils;
import com.ferra13671.BThack.api.Social.SocialManager;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.BThack.api.Utils.DataList.PacketList;
import com.ferra13671.BThack.impl.Modules.MISC.AutoAuth;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.item.Item;
import net.minecraft.network.packet.Packet;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class Arguments {
    public static final Supplier<ArgumentType<Boolean>> BOOLEAN = BoolArgumentType::bool;
    public static final Supplier<ArgumentType<String>> STRING_ONE = StringArgumentType::word;
    public static final Supplier<ArgumentType<String>> GREEDY_STRING = StringArgumentType::greedyString;
    public static final Supplier<ArgumentType<Double>> DOUBLE = DoubleArgumentType::doubleArg;
    public static Supplier<ArgumentType<Double>> DOUBLE(double min) {
        return () -> DoubleArgumentType.doubleArg(min);
    }
    public static Supplier<ArgumentType<Double>> DOUBLE(double min, double max) {
        return () -> DoubleArgumentType.doubleArg(min, max);
    }
    public static final Supplier<ArgumentType<Float>> FLOAT = FloatArgumentType::floatArg;
    public static Supplier<ArgumentType<Float>> FLOAT(float min) {
        return () -> FloatArgumentType.floatArg(min);
    }
    public static Supplier<ArgumentType<Float>> FLOAT(float min, float max) {
        return () -> FloatArgumentType.floatArg(min, max);
    }
    public static final Supplier<ArgumentType<Integer>> INTEGER = IntegerArgumentType::integer;
    public static Supplier<ArgumentType<Integer>> INTEGER(int min) {
        return () -> IntegerArgumentType.integer(min);
    }
    public static Supplier<ArgumentType<Integer>> INTEGER(int min, int max) {
        return () -> IntegerArgumentType.integer(min, max);
    }
    public static final Supplier<ArgumentType<Long>> LONG = LongArgumentType::longArg;
    public static Supplier<ArgumentType<Long>> LONG(long min) {
        return () -> LongArgumentType.longArg(min);
    }
    public static Supplier<ArgumentType<Long>> LONG(long min, long max) {
        return () -> LongArgumentType.longArg(min, max);
    }
    public static final Supplier<ArgumentType<Block>> BLOCK = () -> new ArgumentType<>() {
        private static final Collection<String> examples = Registries.BLOCK.stream()
                .map(BlockUtils::getBlockName)
                .limit(10)
                .toList();

        @Override
        public Block parse(StringReader reader) throws CommandSyntaxException {
            Block block = BlockUtils.getBlockFromNameOrID(reader.readString());
            if (block == null) throw new DynamicCommandExceptionType(
                    name -> Text.literal(LanguageSystem.translate("lang.argument.Block.exception"))
            ).create(reader.readString());

            return block;
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return CommandSource.suggestMatching(Registries.BLOCK.stream().map(block -> block.getTranslationKey().replace("block.minecraft.", "")), builder);
        }

        @Override
        public Collection<String> getExamples() {
            return examples;
        }
    };
    public static final Supplier<ArgumentType<Item>> ITEM = () -> new ArgumentType<>() {
        private static final Collection<String> examples = Registries.ITEM.stream()
                .map(ItemUtils::getItemName)
                .limit(10)
                .toList();

        @Override
        public Item parse(StringReader reader) throws CommandSyntaxException {
            Item item = ItemUtils.getItemFromName(reader.readString());
            if (item == null) throw new DynamicCommandExceptionType(
                    name -> Text.literal(LanguageSystem.translate("lang.argument.Item.exception"))
            ).create(reader.readString());

            return item;
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return CommandSource.suggestMatching(Registries.ITEM.stream().map(block -> block.getTranslationKey().replace("item.minecraft.", "")), builder);
        }

        @Override
        public Collection<String> getExamples() {
            return examples;
        }
    };
    public static Supplier<ArgumentType<String>> MODE(String... modes) {
        return () -> new ModeArgument(modes);
    }
    public static final Supplier<ArgumentType<Module>> MODULE = () -> new ArgumentType<>() {
        private static final Collection<String> examples = Client.getAllModules().stream()
                .map(Module::getName)
                .limit(10)
                .toList();

        @Override
        public Module parse(StringReader reader) throws CommandSyntaxException {
            Module module = Client.getModuleByName(reader.readString());
            if (module == null) throw new DynamicCommandExceptionType(
                    name -> Text.literal(LanguageSystem.translate("lang.argument.Module.exception"))).create(reader.readString()
            );

            return module;
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return CommandSource.suggestMatching(Client.getAllModules().stream().map(Module::getName), builder);
        }

        @Override
        public Collection<String> getExamples() {
            return examples;
        }
    };
    public static final Supplier<ArgumentType<String>> AUTO_AUTH_PLAYERS = () -> new ArgumentType<>() {
        private static final Collection<String> examples = List.of("bebra_tyan", "player123");

        @Override
        public String parse(StringReader reader) throws CommandSyntaxException {
            String name = reader.readString();

            String player = AutoAuth.passwords.keySet().stream()
                    .filter(name::equals)
                    .findFirst()
                    .orElse(null);
            if (player == null)
                throw new DynamicCommandExceptionType(
                        n -> Text.literal(String.format(LanguageSystem.translate("lang.argument.AutoAuthPlayer.exception"), name))
                ).create(reader.readString());
            return player;
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return CommandSource.suggestMatching(AutoAuth.passwords.keySet(), builder);
        }

        @Override
        public Collection<String> getExamples() {
            return examples;
        }
    };
    public static Supplier<ArgumentType<String>> SOCIAL_ADD(SocialManager manager) {
        return () -> new SocialAddArgument(manager);
    }
    public static Supplier<ArgumentType<String>> SOCIAL_REMOVE(SocialManager manager) {
        return () -> new SocialRemoveArgument(manager);
    }
    public static final Supplier<ArgumentType<String>> CLAN_ADD = () -> new ArgumentType<>() {
        private static final Collection<String> examples = List.of("The Emperium", "HWU");

        @Override
        public String parse(StringReader reader) throws CommandSyntaxException {
            String name = reader.readString();

            Clan clan = ClansUtils.clans.stream()
                    .filter(cl -> cl.getName().equals(name))
                    .findFirst()
                    .orElse(null);
            if (clan != null)
                throw new DynamicCommandExceptionType(
                        n -> Text.literal(String.format(LanguageSystem.translate("lang.argument.ClanAdd.exception"), name))
                ).create(reader.readString());
            return name;
        }

        @Override
        public Collection<String> getExamples() {
            return examples;
        }
    };
    public static final Supplier<ArgumentType<String>> CLAN_REMOVE = () -> new ArgumentType<>() {
        private static final Collection<String> examples = List.of("The Emperium", "HWU");

        @Override
        public String parse(StringReader reader) throws CommandSyntaxException {
            String name = reader.readString();

            Clan clan = ClansUtils.clans.stream()
                    .filter(cl -> cl.getName().equals(name))
                    .findFirst()
                    .orElse(null);
            if (clan == null)
                throw new DynamicCommandExceptionType(
                        n -> Text.literal(String.format(LanguageSystem.translate("lang.argument.ClanRemove.exception"), name))
                ).create(reader.readString());
            return name;
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return CommandSource.suggestMatching(ClansUtils.clans.stream().map(Clan::getName), builder);
        }

        @Override
        public Collection<String> getExamples() {
            return examples;
        }
    };
    public static final Supplier<ArgumentType<Clan>> CLAN = () -> new ArgumentType<>() {
        private static final Collection<String> examples = List.of("The Emperium", "HWU");

        @Override
        public Clan parse(StringReader reader) throws CommandSyntaxException {
            String name = reader.readString();

            Clan clan = ClansUtils.clans.stream()
                    .filter(cl -> cl.getName().equals(name))
                    .findFirst()
                    .orElse(null);
            if (clan == null)
                throw new DynamicCommandExceptionType(
                        n -> Text.literal(String.format(LanguageSystem.translate("lang.argument.ClanRemove.exception"), name))
                ).create(reader.readString());
            return clan;
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return CommandSource.suggestMatching(ClansUtils.clans.stream().map(Clan::getName), builder);
        }

        @Override
        public Collection<String> getExamples() {
            return examples;
        }
    };
    public static final Supplier<ArgumentType<String>> CLAN_MEMBER = () -> new ArgumentType<>() {
        private static final Collection<String> examples = List.of("bebra_tyan", "player123");

        @Override
        public String parse(StringReader reader) throws CommandSyntaxException {
            return reader.readString();
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return CommandSource.suggestMatching(context.getArgument("clan name", Clan.class).members.stream().map(Ally::name), builder);
        }

        @Override
        public Collection<String> getExamples() {
            return examples;
        }
    };
    public static final Supplier<ArgumentType<String>> CLIENT_PACKET = () -> new ArgumentType<>() {
        private static final Collection<String> examples = List.of("PlayerInteractEntityC2SPacket", "PlayerActionC2SPacket");

        @Override
        public String parse(StringReader reader) throws CommandSyntaxException {
            String name = reader.readString();

            Class<? extends Packet<?>> packet = PacketList.CLIENT_PACKETS.getOrDefault(name, null);
            if (packet == null)
                throw new DynamicCommandExceptionType(
                        n -> Text.literal(String.format(LanguageSystem.translate("lang.argument.ClientPacket.exception"), name))
                ).create(reader.readString());
            return name;
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return CommandSource.suggestMatching(PacketList.CLIENT_PACKETS.keySet(), builder);
        }

        @Override
        public Collection<String> getExamples() {
            return examples;
        }
    };
    public static final Supplier<ArgumentType<String>> SERVER_PACKET = () -> new ArgumentType<>() {
        private static final Collection<String> examples = List.of("ChatMessageS2CPacket", "DifficultyS2CPacket");

        @Override
        public String parse(StringReader reader) throws CommandSyntaxException {
            String name = reader.readString();

            Class<? extends Packet<?>> packet = PacketList.SERVER_PACKETS.getOrDefault(name, null);
            if (packet == null)
                throw new DynamicCommandExceptionType(
                        n -> Text.literal(String.format(LanguageSystem.translate("lang.argument.ServerPacket.exception"), name))
                ).create(reader.readString());
            return name;
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return CommandSource.suggestMatching(PacketList.SERVER_PACKETS.keySet(), builder);
        }

        @Override
        public Collection<String> getExamples() {
            return examples;
        }
    };
    public static Supplier<ArgumentType<String>> PACKET_LIST_PACKET(PacketList packetList) {
        return () -> new PacketListPacketArgument(packetList);
    }



    private Arguments() {}
}
