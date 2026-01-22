package com.ferra13671.BThack.impl.modules.misc;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.events.GuiOpenEvent;
import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.utils.ChatUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

@ModuleInfo(name = "OpenedGuiInfo", description = "lang.module.OpenedGuiInfo", category = "MISC")
public class OpenedGuiInfo extends Module {

    public final BooleanSetting aName = new BooleanSetting("Name", this, true);
    public final BooleanSetting aPath = new BooleanSetting("Path", this, true);
    public final BooleanSetting aShouldPause = new BooleanSetting("ShouldPause", this, true);


    @EventSubscriber
    @SuppressWarnings("unused")
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
