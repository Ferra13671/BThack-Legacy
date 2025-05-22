package com.ferra13671.BThack.managers.managers.Clans;

import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;

import java.util.ArrayList;
import java.util.Arrays;

public final class ClanSettingsBuilder {
    public static final ArrayList<ModeSetting> clanManagersTargetSettings = new ArrayList<>();

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
        for (Clan clan : Managers.CLAN_MANAGER.getClans()) {
            clanNames.add(clan.getName());
        }
        if (clanNames.isEmpty()) {
            clanNames.add("Null");
        }
        ModeSetting setting = new ModeSetting("Target", module, clanNames, () -> clanManager.getValue()  && clanMode.equals("Target Clan"));
        clanManagersTargetSettings.add(setting);
        return setting;
    }

    public static ArrayList<Setting<?>> buildClanManager(Module module) {
        ArrayList<Setting<?>> settings = new ArrayList<>();
        ArrayList<String> targetMode = new ArrayList<>(Arrays.asList(
                "Only Enemy",
                "Neutral Also",
                "Target Clan",
                "All Clans"
        ));
        ArrayList<String> clanNames = new ArrayList<>();
        for (Clan clan : Managers.CLAN_MANAGER.getClans()) {
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

        clanManagersTargetSettings.add(target);

        return settings;
    }

    public static void reloadSettings() {
        ArrayList<String> clanNames = new ArrayList<>();
        for (Clan clan : Managers.CLAN_MANAGER.getClans()) {
            clanNames.add(clan.getName());
        }
        if (clanNames.isEmpty()) {
            clanNames.add("Null");
        }
        clanManagersTargetSettings.forEach(targetSetting -> {
            targetSetting.setOptions(clanNames);
            targetSetting.setValue(targetSetting.getOptions().get(targetSetting.getIndex()));
        });
    }
}
