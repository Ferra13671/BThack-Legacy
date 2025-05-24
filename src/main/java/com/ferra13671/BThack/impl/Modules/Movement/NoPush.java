package com.ferra13671.BThack.impl.Modules.Movement;

import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;

@ModuleInfo(name = "NoPush", description = "lang.module.NoPush", category = "MOVEMENT")
public class NoPush extends Module {
    public final BooleanSetting blocks = new BooleanSetting("Blocks", this, true);
    public final BooleanSetting entities = new BooleanSetting("Entities", this, false);
    public final BooleanSetting liquids = new BooleanSetting("Liquids", this, true);
}
