package com.ferra13671.BThack.impl.Modules.Client;

import com.ferra13671.BThack.events.InputEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

@ModuleInfo(name = "Macros", description = "lang.module.Macros", category = "CLIENT", autoEnabled = true, visible = false, allowRemapVisible = false, allowRemapKeyCode = false)
public class Macros extends Module {

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onKey(InputEvent.KeyInputEvent e) {
        if (nullCheck() || mc.currentScreen != null) return;
        if (!Managers.MACROS_MANAGER.isEmpty()) {
            Managers.MACROS_MANAGER.forEach(macro -> {
                if (macro.getKey() == e.getKeyCode()) macro.run();
            });
        }
    }
}
