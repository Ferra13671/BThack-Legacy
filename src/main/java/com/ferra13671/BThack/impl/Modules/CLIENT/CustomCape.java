package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Cape.Cape;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import net.minecraft.util.Identifier;

public class CustomCape extends Module {

    public CustomCape() {
        super("CustomCape",
                "lang.module.CustomCape",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                true
        );

        allowRemapKeyCode = false;
        allowRemapVisible = false;
        visible = false;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        Managers.CAPE_MANAGER.setEnabled(true);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Managers.CAPE_MANAGER.setEnabled(false);
    }
}
