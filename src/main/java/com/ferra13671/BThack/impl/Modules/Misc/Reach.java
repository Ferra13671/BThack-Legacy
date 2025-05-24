package com.ferra13671.BThack.impl.Modules.Misc;

import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;

@ModuleInfo(name = "Reach", description = "lang.module.Reach", category = "MISC")
public class Reach extends Module {
    public final NumberSetting range = new NumberSetting("Range", this, 0.5, 0.1, 4, false);
}
