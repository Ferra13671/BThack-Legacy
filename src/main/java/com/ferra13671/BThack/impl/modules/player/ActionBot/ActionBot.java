package com.ferra13671.BThack.impl.modules.player.ActionBot;

import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.GuiButtonSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.gui.screen.actionbot.ActionBotConfigGui;

@ModuleInfo(name = "ActionBot", description = "lang.module.ActionBot", category = "PLAYER")
public class ActionBot extends Module {

    public final BooleanSetting repeat = new BooleanSetting("Repeat", this, false);
    @SuppressWarnings("unused")
    public final GuiButtonSetting openConfig = new GuiButtonSetting("Open Config", this, ActionBotConfigGui::new);


    private ActionBotRunTimeThread thread;

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        super.onEnable();

        thread = new ActionBotRunTimeThread();
        thread.start();
    }

    @Override
    public void onDisable() {
        if (nullCheck() || thread == null) {
            super.onDisable();
            return;
        }

        thread.closeThread();
    }
}
