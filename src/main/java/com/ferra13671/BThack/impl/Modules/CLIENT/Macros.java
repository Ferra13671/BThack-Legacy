package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Events.InputEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

@ModuleInfo(name = "Macros", description = "lang.module.Macros", category = "CLIENT", autoEnabled = true)
public class Macros extends Module {

    public Macros() {
        allowRemapVisible = false;
        allowRemapKeyCode = false;
        setVisible(false);
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
