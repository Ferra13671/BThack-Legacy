package com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings;


import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.SoundSystem.SoundSystem;
import com.ferra13671.BThack.api.SoundSystem.Sounds;

public class Visible extends Checkbox {

    public Visible(ModuleButton parent, int offset, Module module) {
        super(null, parent, offset, module);
    }

    @Override
    protected int getAlpha() {
        return (int) (255 * (module.isVisible() ? animation.getEase() : 1 - animation.getEase()));
    }

    @Override
    protected boolean needRenderPlate() {
        return module.isVisible() || animation.getEase() < 1;
    }

    @Override
    protected String getText() {
        return "Visible";
    }

    @Override
    public boolean updateComponent(int mouseX, int mouseY) {
        hovered = isMouseOnButton(mouseX, mouseY);

        return true;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOnButton(mouseX, mouseY) && button == 0) {
            module.setVisible(!module.isVisible());
            animation.reset();
            SoundSystem.playSound(module.isVisible() ? Sounds.GUI_CHECKBOX_ENABLE : Sounds.GUI_CHECKBOX_DISABLE);
        }
        return isMouseOnButton(mouseX, mouseY);
    }
}
