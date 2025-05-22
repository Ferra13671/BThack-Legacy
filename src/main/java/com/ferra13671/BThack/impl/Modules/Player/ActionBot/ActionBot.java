package com.ferra13671.BThack.impl.Modules.Player.ActionBot;

import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.GuiButtonSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.gui.Screen.ActionBot.ActionBotConfigGui;

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
