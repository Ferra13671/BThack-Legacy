package com.ferra13671.BThack.impl.modules.client;

import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.core.client.ModuleList;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.ModeSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Arrays;

@ModuleInfo(name = "ClientSettings", description = "lang.module.ClientSettings", category = "CLIENT", autoEnabled = true, visible = false, allowRemapVisible = false, allowRemapKeyCode = false)
public class ClientSettings extends Module {

    public final BooleanSetting startSound = new BooleanSetting("Start Sound", this, true);
    public final BooleanSetting moduleToggleSound = new BooleanSetting("Module Toggle Sound", this, true);
    public final NumberSetting soundVolume = new NumberSetting("Sound Volume", this, 1, 0.3, 2, false, moduleToggleSound::getValue);
    public final ModeSetting language = new ModeSetting("Language", this, new ArrayList<>(LanguageSystem.getLoadedLangs()));
    public final ModeSetting friendColor = new ModeSetting("Friend Color", this, Arrays.asList(
            "GREEN",
            "YELLOW",
            "BLUE",
            "DARK_BLUE",
            "AQUA",
            "DARK_AQUA",
            "LIGHT_PURPLE",
            "DARK_PURPLE",
            "GOLD"
    ));
    public final ModeSetting enemyColor = new ModeSetting("Enemy Color", this, Arrays.asList(
            "RED",
            "YELLOW",
            "BLUE",
            "DARK_BLUE",
            "AQUA",
            "DARK_AQUA",
            "LIGHT_PURPLE",
            "DARK_PURPLE",
            "GOLD"
    ));
    public final ModeSetting ownColor = new ModeSetting("Own Color", this, Arrays.asList(
            "AQUA",
            "DARK_AQUA",
            "YELLOW",
            "BLUE",
            "DARK_BLUE",
            "LIGHT_PURPLE",
            "DARK_PURPLE",
            "GOLD"
    ));


    @Override
    protected void addToArrayList() {}

    @Override
    protected void removeFromArrayList() {}

    @Override
    public void sendToggleMessage() {}

    @Override
    public void playOnSound() {}

    @Override
    public void playOffSound() {}

    @Override
    public void onDisable() {
        setEnabled(true);
    }

    public static Formatting getFriendColor() {
        return Managers.FRIENDS_MANAGER.getColor();
    }

    public static Formatting getEnemyColor() {
        return Managers.ENEMIES_MANAGER.getColor();
    }

    public static Formatting getOwnColor() {
        return Formatting.valueOf(ModuleList.clientSettings.ownColor.getValue());
    }
}
