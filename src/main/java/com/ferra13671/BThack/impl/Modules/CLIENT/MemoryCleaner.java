package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;

@ModuleInfo(name = "MemoryCleaner", description = "lang.module.MemoryCleaner", category = "CLIENT", autoEnabled = true)
public class MemoryCleaner extends Module {

    public final BooleanSetting showMessages = new BooleanSetting("Show Messages", this, true);

    public MemoryCleaner() {
        allowRemapKeyCode = false;
        allowRemapVisible = false;
        setVisible(false);
    }
}
