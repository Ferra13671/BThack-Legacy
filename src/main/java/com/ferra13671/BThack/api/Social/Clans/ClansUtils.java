package com.ferra13671.BThack.api.Social.Clans;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigSystem;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import net.minecraft.entity.player.PlayerEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public final class ClansUtils {
    public static final ArrayList<Clan> clans = new ArrayList<>();

    public static Clan getClan(String clanName) {
        for (Clan clan : clans) {
            if (clan.getName().equals(clanName))
                return clan;
        }

        return null;
    }

    public static Clan getFirstClanFromMember(String memberName) {
        for (Clan clan : clans) {
            for (Ally ally : clan.members) {
                if (ally.name().equals(memberName)) {
                    return clan;
                }
            }
        }
        return null;
    }

    public static ArrayList<Clan> getClansFromMember(String memberName) {
        ArrayList<Clan> temp = new ArrayList<>();

        for (Clan clan : clans) {
            for (Ally ally : clan.members) {
                if (ally.name().equals(memberName)) {
                    temp.add(clan);
                }
            }
        }
        return temp;
    }

    public static boolean addClan(String clanName, float r, float g, float b) {
        ArrayList<String> clanNames = new ArrayList<>();

        for (Clan clan : clans) {
            clanNames.add(clan.getName());
        }

        if (!clanNames.contains(clanName)) {
            clans.add(new Clan(clanName, r, g, b));
            try {
                ConfigSystem.saveClans();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        } else {
            BThack.log("This clan already exists!");
        }
        return false;
    }

    public static boolean addClan(String clanName, String color) {
        ArrayList<String> clanNames = new ArrayList<>();

        for (Clan clan : clans) {
            clanNames.add(clan.getName());
        }

        if (!clanNames.contains(clanName)) {
            clans.add(new Clan(clanName, color));
            try {
                ConfigSystem.saveClans();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        } else {
            BThack.log("This clan already exists!");
        }
        return false;
    }

    public static boolean removeClan(String clanName) {
        for (Clan clan : clans) {
            if (clan.getName().equals(clanName)) {
                clans.remove(clan);
                Path path = Paths.get("BThack/Social/Clans/" + clan.getName() + ".json");
                if (Files.exists(path)) {
                    File file = new File(path.toUri());
                    file.delete();
                }
                try {
                    ConfigSystem.saveClans();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
        }
        BThack.log("This clan doesn't exist!");
        return false;
    }

    public static void reloadClans() {
        clans.clear();
        try {
            ConfigSystem.loadClans();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isAlly(PlayerEntity player) {
        String name = player.getDisplayName().getString();
        return isAlly(name);
    }

    public static boolean isAlly(String name) {
        for (Clan clan : clans) {
            for (Ally ally : clan.members) {
                if (ally.name().equals(name))
                    return true;
            }
        }
        return false;
    }

    public static boolean isClanMember(String targetClanName, String memberName) {
        Clan clan = getClan(targetClanName);

        if (clan == null) return false;

        for (Ally ally : clan.members) {
            if (ally.name().equals(memberName)) {
                return true;
            }
        }
        return false;
    }

    public static ClanStatus getClanStatus(String clanName) {
        for (Clan clan : clans) {
            if (clan.getName().equals(clanName)) {
                return switch (clan.getStatus()) {
                    case "Friendly" -> ClanStatus.FRIENDLY;
                    default -> ClanStatus.NEUTRAL;
                    case "Enemy" -> ClanStatus.ENEMY;
                };
            }
        }
        return null;
    }


    public static final ArrayList<String> modulesWithClanManager = new ArrayList<>();

    public static BooleanSetting getClanManagerSetting(Module module) {
        return new BooleanSetting("Clan Manager", module, true);
    }

    public static ModeSetting getClanModeSetting(Module module, BooleanSetting clanManager) {
        return new ModeSetting("Clan Mode", module, new ArrayList<>(Arrays.asList(
                "Only Enemy",
                "Neutral Also",
                "Target Clan",
                "All Clans"
        )), clanManager::getValue);
    }

    public static ModeSetting getClanTargetSetting(Module module, BooleanSetting clanManager, ModeSetting clanMode) {
        ArrayList<String> clanNames = new ArrayList<>();
        for (Clan clan : clans) {
            clanNames.add(clan.getName());
        }
        if (clanNames.isEmpty()) {
            clanNames.add("Null");
        }
        modulesWithClanManager.add(module.getName());
        return new ModeSetting("Target", module, clanNames, () -> clanManager.getValue()  && clanMode.equals("Target Clan"));
    }

    public static ArrayList<Setting> addClanManagerInModule(Module module) {
        ArrayList<Setting> settings = new ArrayList<>();
        ArrayList<String> targetMode = new ArrayList<>(Arrays.asList(
                "Only Enemy",
                "Neutral Also",
                "Target Clan",
                "All Clans"
        ));
        ArrayList<String> clanNames = new ArrayList<>();
        for (Clan clan : clans) {
            clanNames.add(clan.getName());
        }
        if (clanNames.isEmpty()) {
            clanNames.add("Null");
        }

        BooleanSetting clanManager = new BooleanSetting("Clan Manager", module, true);

        ModeSetting clanMode = new ModeSetting("Clan Mode", module, targetMode, clanManager::getValue);
        ModeSetting target = new ModeSetting("Target", module, clanNames, () -> clanMode.equals("Target Clan"));

        settings.add(clanManager);
        settings.add(clanMode);
        settings.add(target);

        modulesWithClanManager.add(module.getName());

        return settings;
    }

    public static void reloadClanListOnModules() {
        ArrayList<String> clanNames = new ArrayList<>();
        for (Clan clan : clans) {
            clanNames.add(clan.getName());
        }
        if (clanNames.isEmpty()) {
            clanNames.add("Null");
        }
        for (String moduleName : modulesWithClanManager) {
            ((ModeSetting) Managers.SETTINGS_MANAGER.getModuleSettingByName(moduleName, "Target")).setOptions(clanNames);
            ((ModeSetting) Managers.SETTINGS_MANAGER.getModuleSettingByName(moduleName, "Target")).setValue(((ModeSetting) Managers.SETTINGS_MANAGER.getModuleSettingByName(moduleName, "Target")).getOptions().get(((ModeSetting) Managers.SETTINGS_MANAGER.getModuleSettingByName(moduleName, "Target")).getIndex()));
        }
    }
}
