package com.ferra13671.BThack.impl.Modules.Misc;

import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;

@ModuleInfo(name = "NoBreakDelay", description = "lang.module.NoBreakDelay", category = "MISC")
public class NoBreakDelay extends Module {

    public final BooleanSetting noInstant = new BooleanSetting("No Instant", this, true);
}
