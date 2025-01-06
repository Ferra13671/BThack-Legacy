package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Events.GuiOpenEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class OpenedGuiInfo extends Module {

    public static BooleanSetting aName;
    public static BooleanSetting aPath;
    public static BooleanSetting aShouldPause;

    public OpenedGuiInfo() {
        super("OpenedGuiInfo",
                "lang.module.OpenedGuiInfo",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        aName = new BooleanSetting("Name", this, true);
        aPath = new BooleanSetting("Path", this, true);
        aShouldPause = new BooleanSetting("ShouldPause", this, true);

        initSettings(
                aName,
                aPath,
                aShouldPause
        );
    }

    @EventSubscriber
    public void onGuiOpen(GuiOpenEvent e) {
        if (e.getScreen() == null) return;

        String text = "";
        if (aName.getValue())
            text += "  Name: " + e.getScreen().getTitle().getString();
        if (aPath.getValue())
            text += "  Path: " + e.getScreen();
        if (aShouldPause.getValue())
            text += "  Should Pause: " + e.getScreen().shouldPause();

        ChatUtils.sendMessage(text);
        BThack.log(text);
    }
}
