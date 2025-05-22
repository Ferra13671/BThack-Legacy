package com.ferra13671.BThack.impl.Modules.Client;

import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;

@ModuleInfo(name = "BThackMainMenu", description = "lang.module.BThackMainMenu", category = "CLIENT", autoEnabled = true, visible = false, allowRemapVisible = false, allowRemapKeyCode = false)
public class BThackMainMenu extends Module {
    public final BooleanSetting screenChangeAnimation = new BooleanSetting("Screen Change Animation", this, true);
    public final NumberSetting animationSpeed = new NumberSetting("Anim. Speed", this, 1, 0.5, 3, false, screenChangeAnimation::getValue);
}
