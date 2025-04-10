package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.SocialManagers;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Arrays;

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

    public ClientSettings() {
        super("ClientSettings",
                "lang.module.ClientSettings",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                true
        );

        allowRemapKeyCode = false;
        allowRemapVisible = false;
        setVisible(false);

        initSettings(
                startSound,
                moduleToggleSound,
                soundVolume,
                language,
                friendColor,
                enemyColor,
                ownColor
        );
    }

    @Override
    protected void addToArrayList() {}

    @Override
    protected void removeFromArrayList() {}

    @Override
    public void sendToggleMessage() {
        //no action
    }

    @Override
    public void playOnSound() {
        //no action
    }

    @Override
    public void playOffSound() {
        //no action
    }

    @Override
    public void onDisable() {
        setToggled(true);
    }

    public static Formatting getFriendColor() {
        return SocialManagers.FRIENDS.getColor();
    }

    public static Formatting getEnemyColor() {
        return SocialManagers.ENEMIES.getColor();
    }

    public static Formatting getOwnColor() {
        return Formatting.valueOf(ModuleList.clientSettings.ownColor.getValue());
    }
}
