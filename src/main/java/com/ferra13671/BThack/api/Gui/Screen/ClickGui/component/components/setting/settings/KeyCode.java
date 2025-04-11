package com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings;


import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.KeyCodeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.SoundSystem.SoundSystem;
import com.ferra13671.BThack.api.SoundSystem.Sounds;
import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class KeyCode extends AbstractSetting<KeyCodeSetting> {

    private boolean binding;

    public KeyCode(ModuleButton button, int offset, KeyCodeSetting setting, Module module) {
        super(offset, button, module, setting);
        x = button.parent.getX() + Constants.CLICKGUI_FRAME_WIDTH;
        y = button.parent.getY() + button.offset;
    }

    @Override
    public void renderComponent() {
        BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + Constants.CLICKGUI_FRAME_WIDTH, parent.parent.getY() + offset + 15, this.hovered ? ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundHoveredColor()), (int) (255 * Math.min(1, ModuleList.clickGui.opacity.getValue() + 0.13))) : ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundColor()), (int) (255 * Math.min(1, ModuleList.clickGui.opacity.getValue() + 0.13))));

        BThackRender.drawString(binding ? "< PRESS KEY >" : (setting.getName() + ": " + KeyboardUtils.getKeyName(setting.getValue())), parent.parent.getX() + 2, parent.parent.getY() + offset + 4, ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().moduleDisabledColor()));
    }

    @Override
    public boolean updateComponent(int mouseX, int mouseY) {
        if (!getVisible() || !parent.open) return true;

        hovered = isMouseOnButton(mouseX, mouseY);
        y = parent.parent.getY() + offset;
        x = parent.parent.getX();

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
                setting.module.onChangeSetting(setting);
                binding = false;
                SoundSystem.playSound(Sounds.GUI_TYPING);
            } else if (key != KeyboardUtils.KEY_ESCAPE) {
                setting.setValue(key);
                setting.module.onChangeSetting(setting);
                binding = false;
                SoundSystem.playSound(Sounds.GUI_TYPING);
            }
        }
    }
}
