package com.ferra13671.BThack.api.Module;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.api.Category.Categories;
import com.ferra13671.BThack.api.Category.Category;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.SoundSystem.SoundSystem;
import com.ferra13671.BThack.api.SoundSystem.Sounds;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.impl.HudComponents.ArrayListComponent;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;

import java.lang.reflect.Field;

public class Module {
    private final ModuleInfo moduleInfo = getClass().getAnnotation(ModuleInfo.class);
    public static final MinecraftClient mc = MinecraftClient.getInstance();

    public final String name = moduleInfo.name();
    private final String description = moduleInfo.description();
    private final Category category = Categories.get(moduleInfo.category());
    private final boolean autoEnabled = moduleInfo.autoEnabled();
    private final boolean allowRemapVisible = moduleInfo.allowRemapVisible();
    private final boolean allowRemapKeyCode = moduleInfo.allowRemapKeyCode();
    public boolean visible = true;
    private int keyCode = moduleInfo.key();
    public boolean enabled;

    public String arrayListInfo = "";

    public static boolean nullCheck() {
        return mc.player == null || mc.world == null;
    }

    public void initSettings() {
        if (category == null) throw new IllegalStateException("Category equals null");
        for (Field field : getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                if (field.get(this) instanceof Setting<?> setting && !setting.isInCategory())
                    Managers.SETTINGS_MANAGER.addModuleSetting(setting);
            } catch (Exception e) {
                BThack.error(e.getMessage());
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public String getArrayListName() {
        return name + (arrayListInfo.isEmpty() ? "" : Formatting.GRAY + "[" + Formatting.WHITE +  arrayListInfo + Formatting.GRAY + "]");
    }

    public String getChatName() {
        return "[" + name + "]";
    }

    public String getDescription() {
        return description.startsWith("lang.") ? LanguageSystem.translate(description) : description;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isAutoEnabled() {
        return autoEnabled;
    }

    public boolean isAllowRemapVisible() {
        return allowRemapVisible;
    }

    public boolean isAllowRemapKeyCode() {
        return allowRemapKeyCode;
    }

    public int getKey() {
        return keyCode;
    }

    public void setKey(int key) {
        if (!isAllowRemapKeyCode()) return;
        this.keyCode = key;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        if (!isAllowRemapVisible()) return;
        this.visible = visible;
        if (this.visible && isEnabled())
            ArrayListComponent.addModule(this);
        else ArrayListComponent.removeModule(this);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (this.enabled) {
            sendToggleMessage();
            playOnSound();
            addToArrayList();
            onEnable();
        } else {
            sendToggleMessage();
            playOffSound();
            removeFromArrayList();
            onDisable();
        }
    }

    public void setEnabledQuietly(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (this.enabled) {
            addToArrayList();
            onEnable();
        } else {
            removeFromArrayList();
            onDisable();
        }
    }

    public void toggle() {
        enabled = !enabled;
        if (enabled) {
            sendToggleMessage();
            playOnSound();
            onEnable();
            addToArrayList();
        } else {
            sendToggleMessage();
            playOffSound();
            onDisable();
            removeFromArrayList();
        }
    }

    public void onEnable() {
        BThack.EVENT_BUS.register(this);
    }

    public void onDisable() {
        BThack.EVENT_BUS.unregister(this);
    }

    public void onChangeSetting(Setting<?> setting) {}

    public void playOnSound() {
        if (ModuleList.clientSettings.moduleToggleSound.getValue())
            SoundSystem.playSound(Sounds.MODULE_ON, ModuleList.clientSettings.soundVolume.getValue().floatValue());
    }

    public void playOffSound() {
        if (ModuleList.clientSettings.moduleToggleSound.getValue())
            SoundSystem.playSound(Sounds.MODULE_OFF, ModuleList.clientSettings.soundVolume.getValue().floatValue());
    }

    public void sendNotification(String text) {
        if (ModuleList.chatNotifications.isEnabled() && ModuleList.chatNotifications.moduleMessages.getValue())
            ChatUtils.sendMessage(getChatName() + Formatting.GRAY + " " + text);
    }

    protected void addToArrayList() {
        ArrayListComponent.addModule(this);
    }

    protected void removeFromArrayList() {
        ArrayListComponent.removeModule(this);
    }

    public void sendToggleMessage() {
        if (ModuleList.chatNotifications.isEnabled() && ModuleList.chatNotifications.moduleToggle.getValue())
            ChatUtils.sendMessage(getName() + ": " + (enabled ? Formatting.GREEN + "Enabled" : Formatting.RED + "Disabled"));
    }
}
