package com.ferra13671.BThack.api.Managers;

import com.ferra13671.BThack.api.Managers.managers.*;
import com.ferra13671.BThack.api.Managers.managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.managers.Cape.CapeManager;
import com.ferra13671.BThack.api.Managers.managers.ColourTheme.ColorThemeManager;
import com.ferra13671.BThack.api.Managers.managers.Command.CommandManager;
import com.ferra13671.BThack.api.Managers.managers.Destroy.DestroyManager;
import com.ferra13671.BThack.api.Managers.managers.Macros.MacrosManager;
import com.ferra13671.BThack.api.Managers.managers.Memory.MemoryManager;
import com.ferra13671.BThack.api.Managers.managers.Setting.SettingsManager;
import com.ferra13671.BThack.api.Managers.managers.TravelChange.TravelChangeManager;
import com.ferra13671.BThack.api.Managers.managers.TwoFA.TwoFAManager;
import com.ferra13671.BThack.api.Managers.managers.Waypoint.WaypointManager;

public class Managers {
    public static final TPSManager TPS_MANAGER = new TPSManager();
    public static final BlockSearchManager BLOCK_SEARCH_MANAGER = new BlockSearchManager();
    public static final FireworkManager FIREWORK_MANAGER = new FireworkManager();
    public static final DestroyManager DESTROY_MANAGER = new DestroyManager();
    public static final NetworkManager NETWORK_MANAGER = new NetworkManager();
    public static final TickManager TICK_MANAGER = new TickManager();
    public static final MainMenuShaderManager MAIN_MENU_SHADER_MANAGER = new MainMenuShaderManager();
    public static final TotemPopManager TOTEM_POP_MANAGER = new TotemPopManager();
    public static final ColorThemeManager COLOR_THEME_MANAGER = new ColorThemeManager();
    public static final SettingsManager SETTINGS_MANAGER = new SettingsManager();
    public static final MemoryManager MEMORY_MANAGER = new MemoryManager();
    public static final TravelChangeManager TRAVEL_CHANGE_MANAGER = new TravelChangeManager();
    public static final CommandManager COMMAND_MANAGER = new CommandManager();
    public static final EntityDeathManager ENTITY_DEATH_MANAGER = new EntityDeathManager();
    public static final WaypointManager WAYPOINT_MANAGER = new WaypointManager();
    public static final MacrosManager MACROS_MANAGER = new MacrosManager();
    public static final BuildManager BUILD_MANAGER = new BuildManager();
    public static final CapeManager CAPE_MANAGER = new CapeManager();
    public static final TwoFAManager TWOFA_MANAGER = new TwoFAManager();
    public static final AutoAuthManager AUTO_AUTH_MANAGER = new AutoAuthManager();
}
