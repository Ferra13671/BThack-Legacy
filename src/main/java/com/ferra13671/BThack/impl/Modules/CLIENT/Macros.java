package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Events.InputEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class Macros extends Module {

    public Macros() {
        super("Macros",
                "lang.module.Macros",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                true
        );

        allowRemapVisible = false;
        visible = false;
        allowRemapKeyCode = false;
    }

    @EventSubscriber
    public void onKey(InputEvent.KeyInputEvent e) {
        if (nullCheck() || mc.currentScreen != null) return;
        if (!Managers.MACROS_MANAGER.isEmpty()) {
            Managers.MACROS_MANAGER.forEach(macro -> {
                if (macro.getKey() == e.getKeyCode()) macro.run();
            });
        }
    }
}
