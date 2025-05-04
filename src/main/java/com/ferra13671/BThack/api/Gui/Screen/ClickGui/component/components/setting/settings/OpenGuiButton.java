package com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings;


import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.GuiButtonSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.GuiSystem.Screen.BThackScreen;
import com.ferra13671.BThack.Constants;

public class OpenGuiButton extends AbstractSetting<GuiButtonSetting> implements Mc {

    public OpenGuiButton(GuiButtonSetting setting, ModuleButton button, int offset, Module module) {
        super(offset, button, module, setting);
    }

    @Override
    public void renderComponent() {
        super.renderComponent();

        BThackRender.drawRect(getX(), getY(), getX() + Constants.CLICKGUI_FRAME_WIDTH, getY() + getHeight(), hovered ? ColorUtils.integrateAlpha(ModuleList.clickGui.backgroundColor.getBrighterValue().hashCode(), (int) (255 * Math.min(1, ModuleList.clickGui.opacity.getValue() + 0.13))) : ColorUtils.integrateAlpha(ModuleList.clickGui.backgroundColor.getValue().hashCode(), (int) (255 * Math.min(1, ModuleList.clickGui.opacity.getValue() + 0.13))));

        BThackRender.drawString(setting.getName() + " ... ", getX() + 2, getY() + 4, ModuleList.clickGui.textColor.getValue().hashCode());
    }

    @Override
    public boolean updateComponent(int mouseX, int mouseY) {
        if (!getVisible()) return true;

        hovered = isMouseOnButton(mouseX, mouseY);

        return true;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (!getVisible()) return false;

        if (isMouseOnButton(mouseX, mouseY) && button == 0 && parent.open)
            ((BThackScreen) mc.currentScreen).actionAfterClicking(() -> mc.setScreen(setting.getValue().get()));

        return isMouseOnButton(mouseX, mouseY);
    }
}
