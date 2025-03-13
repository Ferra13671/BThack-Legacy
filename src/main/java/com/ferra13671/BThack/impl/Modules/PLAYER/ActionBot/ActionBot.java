package com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot;


import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.GuiButtonSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Gui.Screen.ActionBot.ActionBotConfigGui;

public class ActionBot extends Module {

    public final BooleanSetting repeat = new BooleanSetting("Repeat", this, false);
    public final GuiButtonSetting openConfig = new GuiButtonSetting("Open Config", this, ActionBotConfigGui::new);

    public ActionBot() {
        super("ActionBot",
                "lang.module.ActionBot",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        initSettings(
                repeat,
                openConfig
        );
    }

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
