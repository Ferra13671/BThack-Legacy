package com.ferra13671.BThack.impl.Modules.Client;

import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.DiscordUtils;

@ModuleInfo(name = "DiscordRPC", description = "lang.module.DiscordRPC", category = "CLIENT", autoEnabled = true, allowRemapKeyCode = false)
public class DiscordRPC extends Module {

    public final BooleanSetting secret = new BooleanSetting("Secret :3", this, false);

    public DiscordRPC() {
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
