package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;

@ModuleInfo(name = "NoSlow", description = "lang.module.NoSlow", category = "MOVEMENT")
public class NoSlow extends Module {

    public final BooleanSetting useItems = new BooleanSetting("Use Items", this, true);
    public final BooleanSetting grim = new BooleanSetting("Grim", this, true, useItems::getValue);

    public final BooleanSetting soulSand = new BooleanSetting("SoulSand", this, false);
    public final BooleanSetting slime = new BooleanSetting("Slime", this, false);
    public final BooleanSetting honey = new BooleanSetting("Honey", this, false);
}
