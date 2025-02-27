package com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings;


import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;

public class Visible extends Checkbox {

    public Visible(ModuleButton parent, int offset, Module module) {
        super(null, parent, offset, module);

        x = parent.parent.getX() + parent.parent.getWidth();
        y = parent.parent.getY() + parent.offset;
    }

    @Override
    protected boolean needRenderGradient() {
        return module.visible || animation.getEase() < 1;
    }

    @Override
    protected String getText() {
        return "Visible";
    }

    @Override
    protected int getGradientColor() {
        return ColorUtils.integrateAlpha(ClickGui.getClickGuiColor(true), (int) (255 * (module.visible ? animation.getEase() : 1 - animation.getEase())));
    }

    @Override
    public void updateDependencies(int offset) {
        //no action
    }

    @Override
    public boolean updateComponent(int mouseX, int mouseY) {
        hovered = isMouseOnButton(mouseX, mouseY);
        y = parent.parent.getY() + offset;
        x = parent.parent.getX();

        return true;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOnButton(mouseX, mouseY) && button == 0) {
            module.visible = !module.visible;
            animation.reset();
        }
        return isMouseOnButton(mouseX, mouseY);
    }
}
