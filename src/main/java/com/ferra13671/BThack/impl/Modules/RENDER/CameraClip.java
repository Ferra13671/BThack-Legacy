package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class CameraClip extends Module {

    public CameraClip() {
        super("CameraClip",
                "lang.module.CameraClip",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );
    }
}
