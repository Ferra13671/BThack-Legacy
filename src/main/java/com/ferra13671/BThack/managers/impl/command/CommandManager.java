package com.ferra13671.BThack.managers.impl.command;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.core.DeviceSystem;
import com.ferra13671.BThack.api.utils.Initializable;
import com.ferra13671.BThack.api.plugin.Plugin;
import com.ferra13671.BThack.api.plugin.PluginSystem;
import com.ferra13671.BThack.api.plugin.PluginUtils;
import com.ferra13671.BThack.api.utils.datalist.DataLists;
import com.ferra13671.BThack.impl.commands.*;
import com.ferra13671.BThack.impl.commands.otherlist.*;
import com.ferra13671.BThack.impl.commands.social.clans.ClanMembersCommand;
import com.ferra13671.BThack.impl.commands.social.clans.ClanStatusCommand;
import com.ferra13671.BThack.impl.commands.social.clans.ClansCommand;
import com.ferra13671.BThack.impl.commands.social.EnemiesCommand;
import com.ferra13671.BThack.impl.commands.social.FriendsCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandSource;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements Initializable {

    private final CommandDispatcher<CommandSource> dispatcher = new CommandDispatcher<>();
    private final CommandSource source = new ClientCommandSource(null, MinecraftClient.getInstance());
    private final List<AbstractCommand> commands = new ArrayList<>();

    @Override
    public void init() {
        if (inited.get()) return;

        PluginSystem.getLoadedPlugins().forEach(Plugin::onInitCommands);

        register(new ClansCommand());
        register(new ClanStatusCommand());
        register(new ClanMembersCommand());

        DataLists.forEach(dataList -> {
            register(dataList.editDataListCommand);
            register(dataList.abstractDataListCommand);
        });

        register(new FriendsCommand());
        register(new EnemiesCommand());

        register(new CleanMemoryCommand());
        register(new ClearChatCommand());
        register(new SoundReloadCommand());
        register(new DisableAllCommand());
        register(new ModuleCommand());
        register(new BindCommand());
        register(new RotateCommand());
        register(new RefreshCommand());
        register(new PrefixCommand());
        register(new EntitiesNearbyCommand());
        register(new ConfigCommand());
        register(new ClientGamemodeCommand());
        register(new BuildCommand());
        register(new BreakCommand());
        register(new AutoAuthCommand());
        register(new HelpCommand());
        register(new CordsCopyCommand());
        if (DeviceSystem.getLaunchDevice() == DeviceSystem.LaunchDevice.PC) register(new CustomFontCommand());
        register(new WaypointCommand());
        register(new MacroCommand());
        register(new CustomCapeCommand());
        register(new OpenFolderCommand());
        register(new Auto2FACommand());
        register(new AutoAnvilEnchantCommand());

        register(new HClipCommand());
        register(new VClipCommand());


        register(new AutoAuthListCommand());
        register(new FriendListCommand());
        register(new EnemyListCommand());
        register(new ClansListCommand());
        register(new PluginListCommand());

        PluginUtils.getPluginsCommands().forEach(this::register);

        commands.forEach(command -> command.register(dispatcher));
        BThack.debug("Command Manager inited!");
        inited.set(true);
    }

    private void register(AbstractCommand command) {
        commands.add(command);
    }

    public List<AbstractCommand> getCommands() {
        return new ArrayList<>(commands);
    }

    public CommandSource getSource() {
        return source;
    }

    public CommandDispatcher<CommandSource> getDispatcher() {
        return dispatcher;
    }
}
