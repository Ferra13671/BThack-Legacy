package com.ferra13671.BThack.impl.Modules.Movement;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.gui.Screen.ClickGui.ClickGuiScreen;
import com.ferra13671.BThack.api.IMixin.ModifyKeyBinding;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.KeyBinding;

@ModuleInfo(name = "GuiMove", description = "lang.module.GuiMove", category = "MOVEMENT")
public class GuiMove extends Module {

    public final BooleanSetting chat = new BooleanSetting("Chat", this, false);
    public final BooleanSetting clickGui = new BooleanSetting("ClickGui", this, true);


    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (mc.currentScreen == null || ModuleList.elytraFlight.isEnabled()) return;

        if (!chat.getValue() && mc.currentScreen instanceof ChatScreen) return;
        if (!clickGui.getValue() && mc.currentScreen instanceof ClickGuiScreen) return;

        KeyBinding[] keys = {mc.options.forwardKey, mc.options.backKey, mc.options.leftKey, mc.options.rightKey, mc.options.sprintKey, mc.options.sneakKey, mc.options.jumpKey};

        for (KeyBinding keyBinding : keys) {
            keyBinding.setPressed(KeyboardUtils.isKeyDown(((ModifyKeyBinding) keyBinding)._getBoundKey().getCode()));
        }
    }
}
