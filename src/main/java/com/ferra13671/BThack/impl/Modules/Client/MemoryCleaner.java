package com.ferra13671.BThack.impl.Modules.Client;

import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;

@ModuleInfo(name = "MemoryCleaner", description = "lang.module.MemoryCleaner", category = "CLIENT", autoEnabled = true, visible = false, allowRemapVisible = false, allowRemapKeyCode = false)
public class MemoryCleaner extends Module {
    public final BooleanSetting showMessages = new BooleanSetting("Show Messages", this, true);
}
