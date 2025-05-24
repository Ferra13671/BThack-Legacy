package com.ferra13671.BThack.managers;

import com.ferra13671.BThack.managers.managers.*;
import com.ferra13671.BThack.managers.managers.Setting.SettingsManager;
import com.ferra13671.BThack.managers.managers.TravelChange.TravelChangeManager;
import com.ferra13671.BThack.managers.managers.Waypoint.WaypointManager;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.managers.managers.Account.AccountManager;
import com.ferra13671.BThack.managers.managers.Place.PlaceManager;
import com.ferra13671.BThack.managers.managers.Cape.CapeManager;
import com.ferra13671.BThack.managers.managers.Command.CommandManager;
import com.ferra13671.BThack.managers.managers.Break.BreakManager;
import com.ferra13671.BThack.managers.managers.Macros.MacrosManager;
import com.ferra13671.BThack.managers.managers.Clans.ClanManager;
import com.ferra13671.BThack.managers.managers.SocialManager;
import com.ferra13671.BThack.managers.managers.TwoFA.TwoFAManager;
import net.minecraft.util.Formatting;

@SuppressWarnings("unused")
public class Managers {
    public static final TPSManager TPS_MANAGER = new TPSManager();
    public static final BlockSearchManager BLOCK_SEARCH_MANAGER = new BlockSearchManager();
    public static final FireworkManager FIREWORK_MANAGER = new FireworkManager();
    public static final BreakManager DESTROY_MANAGER = new BreakManager();
    public static final NetworkManager NETWORK_MANAGER = new NetworkManager();
    public static final TickManager TICK_MANAGER = new TickManager();
    public static final MainMenuShaderManager MAIN_MENU_SHADER_MANAGER = new MainMenuShaderManager();
    public static final TotemPopManager TOTEM_POP_MANAGER = new TotemPopManager();
    public static final SettingsManager SETTINGS_MANAGER = new SettingsManager();
    public static final TravelChangeManager TRAVEL_CHANGE_MANAGER = new TravelChangeManager();
    public static final CommandManager COMMAND_MANAGER = new CommandManager();
    public static final EntityDeathManager ENTITY_DEATH_MANAGER = new EntityDeathManager();
    public static final WaypointManager WAYPOINT_MANAGER = new WaypointManager();
    public static final MacrosManager MACROS_MANAGER = new MacrosManager();
    public static final PlaceManager BUILD_MANAGER = new PlaceManager();
    public static final CapeManager CAPE_MANAGER = new CapeManager();
    public static final TwoFAManager TWOFA_MANAGER = new TwoFAManager();
    public static final AutoAuthManager AUTO_AUTH_MANAGER = new AutoAuthManager();
    public static final AccountManager ACCOUNT_MANAGER = new AccountManager();
    public static final SocialManager FRIENDS_MANAGER = new SocialManager("Friends/Friends.txt", "friend") {
        @Override
        public Formatting getColor() {
            return Formatting.valueOf(ModuleList.clientSettings.friendColor.getValue());
        }
    };
    public static final SocialManager ENEMIES_MANAGER = new SocialManager("Enemies/Enemies.txt", "enemy") {
        @Override
        public Formatting getColor() {
            return Formatting.valueOf(ModuleList.clientSettings.enemyColor.getValue());
        }
    };
    public static final ClanManager CLAN_MANAGER = new ClanManager();
    public static final FallDistanceManager FALL_DISTANCE_MANAGER = new FallDistanceManager();
}
