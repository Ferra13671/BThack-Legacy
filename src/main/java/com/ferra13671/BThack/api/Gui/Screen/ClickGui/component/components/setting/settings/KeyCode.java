package com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings;


import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.KeyCodeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.SoundSystem.SoundSystem;
import com.ferra13671.BThack.api.SoundSystem.Sounds;
import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;

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
