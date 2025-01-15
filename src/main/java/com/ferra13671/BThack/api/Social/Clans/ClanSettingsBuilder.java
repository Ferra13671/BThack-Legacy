package com.ferra13671.BThack.api.Social.Clans;

import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;

import java.util.ArrayList;
import java.util.Arrays;

public final class ClanSettingsBuilder {
    public static final ArrayList<String> modulesWithClanManager = new ArrayList<>();

    public static BooleanSetting buildToggle(Module module) {
        return new BooleanSetting("Clan Manager", module, true);
    }

    public static ModeSetting buildStatusMode(Module module, BooleanSetting clanManager) {
        return new ModeSetting("Clan Mode", module, new ArrayList<>(Arrays.asList(
                "Only Enemy",
                "Neutral Also",
                "Target Clan",
                "All Clans"
        )), clanManager::getValue);
    }

    public static ModeSetting buildClanTargetMode(Module module, BooleanSetting clanManager, ModeSetting clanMode) {
        ArrayList<String> clanNames = new ArrayList<>();
        for (Clan clan : ClanManager.getClans()) {
            clanNames.add(clan.getName());
        }
        if (clanNames.isEmpty()) {
            clanNames.add("Null");
        }
        modulesWithClanManager.add(module.getName());
        return new ModeSetting("Target", module, clanNames, () -> clanManager.getValue()  && clanMode.equals("Target Clan"));
    }

    public static ArrayList<Setting> buildClanManager(Module module) {
        ArrayList<Setting> settings = new ArrayList<>();
        ArrayList<String> targetMode = new ArrayList<>(Arrays.asList(
                "Only Enemy",
                "Neutral Also",
                "Target Clan",
                "All Clans"
        ));
        ArrayList<String> clanNames = new ArrayList<>();
        for (Clan clan : ClanManager.getClans()) {
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

    public static void reloadSettings() {
        ArrayList<String> clanNames = new ArrayList<>();
        for (Clan clan : ClanManager.getClans()) {
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
