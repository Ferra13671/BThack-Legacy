package com.ferra13671.BThack.impl.modules.misc;

import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;

@ModuleInfo(name = "NoBreakDelay", description = "lang.module.NoBreakDelay", category = "MISC")
public class NoBreakDelay extends Module {

    public final BooleanSetting noInstant = new BooleanSetting("No Instant", this, true);
}
