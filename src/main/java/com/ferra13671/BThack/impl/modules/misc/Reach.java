package com.ferra13671.BThack.impl.modules.misc;

import com.ferra13671.BThack.managers.impl.setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;

@ModuleInfo(name = "Reach", description = "lang.module.Reach", category = "MISC")
public class Reach extends Module {
    public final NumberSetting range = new NumberSetting("Range", this, 0.5, 0.1, 4, false);
}
