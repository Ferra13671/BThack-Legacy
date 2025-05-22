package com.ferra13671.BThack.impl.Modules.Misc;

import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;

@ModuleInfo(name = "NoPacketKick", description = "lang.module.NoPacketKick", category = "MISC")
public class NoPacketKick extends Module {

    public final BooleanSetting chatNotify = new BooleanSetting("ChatNotify", this, false);
}
