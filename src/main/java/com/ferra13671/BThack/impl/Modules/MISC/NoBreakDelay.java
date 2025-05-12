package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;

@ModuleInfo(name = "NoBreakDelay", description = "lang.module.NoBreakDelay", category = "MISC")
public class NoBreakDelay extends Module {

    public final BooleanSetting noInstant = new BooleanSetting("No Instant", this, true);
}
