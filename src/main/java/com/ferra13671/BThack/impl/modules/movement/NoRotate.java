package com.ferra13671.BThack.impl.modules.movement;


import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;

@ModuleInfo(name = "NoRotate", description = "lang.module.NoRotate", category = "MOVEMENT")
public class NoRotate extends Module {
    public final BooleanSetting blockPitch = new BooleanSetting("BlockPitchRotate", this, true);
}
