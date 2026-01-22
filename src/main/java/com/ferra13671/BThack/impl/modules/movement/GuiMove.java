package com.ferra13671.BThack.impl.modules.movement;

import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.core.client.ModuleList;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.gui.screen.clickgui.ClickGuiScreen;
import com.ferra13671.BThack.api.imixin.ModifyKeyBinding;
import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.KeyBinding;

@ModuleInfo(name = "GuiMove", description = "lang.module.GuiMove", category = "MOVEMENT")
public class GuiMove extends Module {

    public final BooleanSetting chat = new BooleanSetting("Chat", this, false);
    public final BooleanSetting clickGui = new BooleanSetting("ClickGui", this, true);

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onTick(ClientTickEvent e) {
        if (mc.currentScreen == null || ModuleList.elytraFlight.isEnabled()) return;

        if (!chat.getValue() && mc.currentScreen instanceof ChatScreen) return;
        if (!clickGui.getValue() && mc.currentScreen instanceof ClickGuiScreen) return;

        KeyBinding[] keys = {mc.options.forwardKey, mc.options.backKey, mc.options.leftKey, mc.options.rightKey, mc.options.sprintKey, mc.options.sneakKey, mc.options.jumpKey};

        for (KeyBinding keyBinding : keys)
            keyBinding.setPressed(KeyboardUtils.isKeyDown(((ModifyKeyBinding) keyBinding)._getBoundKey().getCode()));
    }
}
