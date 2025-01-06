package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import net.minecraft.util.Identifier;

public class BThackCape extends Module {
    public static final Identifier BThack_Cape = new Identifier("bthack", "bthack_cape.png");

    public BThackCape() {
        super("BThackCape",
                "lang.module.BThackCape",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                true
        );

        allowRemapKeyCode = false;
        visible = false;
    }
}
