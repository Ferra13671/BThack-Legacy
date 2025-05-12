package com.ferra13671.BThack.impl.Modules.MOVEMENT;


import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;

@ModuleInfo(name = "NoRotate", description = "lang.module.NoRotate", category = "MOVEMENT")
public class NoRotate extends Module {

    public final BooleanSetting blockPitch = new BooleanSetting("BlockPitchRotate", this, true);
}
