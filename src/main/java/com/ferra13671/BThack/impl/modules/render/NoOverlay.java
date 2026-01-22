package com.ferra13671.BThack.impl.modules.render;

import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;

@ModuleInfo(name = "NoOverlay", description = "lang.module.NoOverlay", category = "RENDER")
public class NoOverlay extends Module {
    public final BooleanSetting hurtCam = new BooleanSetting("Hurt Camera", this, true);
    public final BooleanSetting pumpkin = new BooleanSetting("Pumpkin", this, true);
    public final BooleanSetting portal = new BooleanSetting("Portal", this, true);
    public final BooleanSetting crosshair = new BooleanSetting("Crosshair", this, false);
    public final BooleanSetting hotbar = new BooleanSetting("Hotbar", this, false);
    public final BooleanSetting experiense = new BooleanSetting("Experience", this, false);
    public final BooleanSetting jumpBar = new BooleanSetting("JumpBar", this, false);
    public final BooleanSetting vignette = new BooleanSetting("Vignette", this, true);
    public final BooleanSetting effects = new BooleanSetting("Effects", this, true);
    public final BooleanSetting scoreBoard = new BooleanSetting("ScoreBoard", this, false);
}
