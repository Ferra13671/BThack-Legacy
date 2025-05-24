package com.ferra13671.BThack.api.Module;

import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import net.minecraft.util.Formatting;

public class OneActionModule extends Module {

    @Override
    public void sendToggleMessage() {
        if (enabled && ModuleList.chatNotifications.isEnabled() && ModuleList.chatNotifications.moduleToggle.getValue())
            ChatUtils.sendMessage(this.getName() + ": " + Formatting.YELLOW + "Toggled");
    }

    @Override
    public void playOffSound() {}

    @Override
    public void toggle() {
        enabled = !enabled;
        if (enabled) {
            sendToggleMessage();
            playOnSound();
            onEnable();
            onDisable();
            enabled = false;
        } else
            onDisable();
    }

    @Override
    protected final void addToArrayList() {}

    @Override
    protected final void removeFromArrayList() {}

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (this.enabled) {
            sendToggleMessage();
            playOnSound();
            onEnable();
            onDisable();
            this.enabled = false;
        } else
            onDisable();
    }

    @Override
    public void setEnabledQuietly(boolean enabled) {
        this.enabled = enabled;
        if (this.enabled) {
            onEnable();
            onDisable();
            this.enabled = false;
        } else
            onDisable();
    }
}
