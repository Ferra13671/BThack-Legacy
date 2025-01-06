package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.DiscordUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class DiscordRPC extends Module {

    public static BooleanSetting secret;

    public DiscordRPC() {
        super("DiscordRPC",
                "lang.module.DiscordRPC",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                true
        );
        allowRemapKeyCode = false;

        secret = new BooleanSetting("Secret :3", this, false);

        initSettings(secret);

        DiscordUtils.init();
    }

    @Override
    public void onEnable() {
        DiscordUtils.startup();
    }

    @Override
    public void onDisable() {
        DiscordUtils.shutdown();
    }
}
