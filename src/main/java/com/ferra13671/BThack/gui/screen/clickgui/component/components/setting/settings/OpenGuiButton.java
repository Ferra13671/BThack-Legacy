package com.ferra13671.BThack.gui.screen.clickgui.component.components.setting.settings;

import com.ferra13671.BThack.core.client.ModuleList;
import com.ferra13671.BThack.core.render.BThackRender;
import com.ferra13671.BThack.gui.screen.clickgui.component.components.ModuleButton;
import com.ferra13671.BThack.gui.screen.clickgui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.utils.Mc;
import com.ferra13671.BThack.managers.impl.setting.Settings.GuiButtonSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.core.client.systems.gui.Screen.BThackScreen;
import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.impl.modules.client.ClickGui;

public class OpenGuiButton extends AbstractSetting<GuiButtonSetting> implements Mc {

    public OpenGuiButton(GuiButtonSetting setting, ModuleButton button, int offset, Module module) {
        super(offset, button, module, setting);
    }

    @Override
    public void renderComponent() {
        super.renderComponent();

        BThackRender.drawRect(getX(), getY(), getX() + Constants.CLICKGUI_FRAME_WIDTH, getY() + getHeight(), hovered ? ClickGui.BACKGROUND_HOVERED_COLOR : ClickGui.BACKGROUND_COLOR);

        BThackRender.drawString(setting.getName() + " ... ", getX() + 2, getY() + 4, ModuleList.clickGui.textColor.getValue().hashCode());
    }

    @Override
    public boolean updateComponent(int mouseX, int mouseY) {
        if (!getVisible()) return true;

        hovered = isMouseOnButton(mouseX, mouseY);

        return true;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (!getVisible()) return false;

        if (isMouseOnButton(mouseX, mouseY) && button == 0 && parent.open)
            ((BThackScreen) mc.currentScreen).actionAfterClicking(() -> mc.setScreen(setting.getValue().get()));

        return isMouseOnButton(mouseX, mouseY);
    }
}
