package com.ferra13671.BThack.gui.screen.clickgui.component.components.setting.settings;

import com.ferra13671.BThack.core.client.ModuleList;
import com.ferra13671.BThack.core.render.BThackRender;
import com.ferra13671.BThack.gui.screen.clickgui.component.components.ModuleButton;
import com.ferra13671.BThack.gui.screen.clickgui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.KeyCodeSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.core.client.systems.sound.SoundSystem;
import com.ferra13671.BThack.core.client.systems.sound.Sounds;
import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.api.utils.KeyboardUtils;
import com.ferra13671.BThack.impl.modules.client.ClickGui;

public class KeyCode extends AbstractSetting<KeyCodeSetting> {

    private boolean binding;

    public KeyCode(ModuleButton button, int offset, KeyCodeSetting setting, Module module) {
        super(offset, button, module, setting);
    }

    @Override
    public void renderComponent() {
        super.renderComponent();

        BThackRender.drawRect(getX(), getY(), getX() + Constants.CLICKGUI_FRAME_WIDTH, getY() + getHeight(), hovered ? ClickGui.BACKGROUND_HOVERED_COLOR : ClickGui.BACKGROUND_COLOR);

        BThackRender.drawString(binding ? "< PRESS KEY >" : (setting.getName() + ": " + KeyboardUtils.getKeyName(setting.getValue())), getX() + 2, getY() + 4, ModuleList.clickGui.textColor.getValue().hashCode());
    }

    @Override
    public boolean updateComponent(int mouseX, int mouseY) {
        if (!getVisible() || !parent.open) return true;

        hovered = isMouseOnButton(mouseX, mouseY);

        return true;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (!getVisible()) return false;

        if(isMouseOnButton(mouseX, mouseY) && button == 0) {
            SoundSystem.playSound(Sounds.GUI_TYPING);
            binding = !binding;
        }

        return isMouseOnButton(mouseX, mouseY);
    }

    @Override
    public void keyTyped(int key) {
        if (!getVisible()) return;

        if (binding) {
            if (key == KeyboardUtils.KEY_DELETE) {
                setting.setValue(0);
                binding = false;
                SoundSystem.playSound(Sounds.GUI_TYPING);
            } else if (key != KeyboardUtils.KEY_ESCAPE) {
                setting.setValue(key);
                binding = false;
                SoundSystem.playSound(Sounds.GUI_TYPING);
            }
        }
    }
}
