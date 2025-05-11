package com.ferra13671.BThack.api.Module;

import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.api.Category.Category;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import net.minecraft.util.Formatting;

public class OneActionModule extends Module {

    public OneActionModule(String name, String description, int key, MCategory c, boolean autoEnabled) {
        super(name, description, key, c, autoEnabled);
    }

    public OneActionModule(String name, String description, int key, Category c, boolean autoEnabled) {
        super(name, description, key, c, autoEnabled);
    }

    @Override
    public void sendToggleMessage() {
        if (toggled && ModuleList.chatNotifications.isEnabled() && ModuleList.chatNotifications.moduleToggle.getValue()) {
            ChatUtils.sendMessage(this.getName() + ": " + Formatting.YELLOW + "Toggled");
        }
    }

    @Override
    public void playOffSound() {
        //No Action
    }

    @Override
    public void toggle() {
        toggled = !toggled;
        if (toggled) {
            sendToggleMessage();
            playOnSound();
            onEnable();
            onDisable();
            toggled = false;
        } else {
            onDisable();
        }
    }

    @Override
    protected final void addToArrayList() {}

    @Override
    protected final void removeFromArrayList() {}

    @Override
    public void setToggled(boolean toggled) {
        this.toggled = toggled;
        if (this.toggled) {
            sendToggleMessage();
            playOnSound();
            onEnable();
            onDisable();
            this.toggled = false;
        } else {
            onDisable();
        }
    }

    @Override
    public void setQuietlyToggled(boolean toggled) {
        this.toggled = toggled;
        if (this.toggled) {
            onEnable();
            onDisable();
            this.toggled = false;
        } else {
            onDisable();
        }
    }
}
