package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.client.option.KeyBinding;

public class GuiMove extends Module {

    public GuiMove() {
        super("GuiMove",
                "lang.module.GuiMove",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (mc.currentScreen == null || ModuleList.elytraFlight.isEnabled()) return;
        KeyBinding[] keys = {mc.options.forwardKey, mc.options.backKey, mc.options.leftKey, mc.options.rightKey, mc.options.sprintKey, mc.options.sneakKey, mc.options.jumpKey};

        for (KeyBinding keyBinding : keys) {
            keyBinding.setPressed(KeyboardUtils.isKeyDown(keyBinding.getDefaultKey().getCode()));
        }
    }
}
