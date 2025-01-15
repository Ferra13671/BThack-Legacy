package com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.settings;


import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.GuiButtonSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.System.BThackScreen;

public class OpenGuiButton extends AbstractSetting implements Mc {

    private final GuiButtonSetting set;

    public OpenGuiButton(GuiButtonSetting option, ModuleButton button, int offset, Module module) {
        super(offset, button, module, option);
        set = option;
        x = button.parent.getX() + button.parent.getWidth();
        y = button.parent.getY() + button.offset;
    }

    @Override
    public void renderComponent() {
        BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 15, this.hovered ? ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundFontHoveredColor()), (int) (255 * Math.min(1, ModuleList.clickGui.opacity.getValue() + 0.13))) : ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundFontColor()), (int) (255 * Math.min(1, ModuleList.clickGui.opacity.getValue() + 0.13))));

        BThackRender.drawString(this.op.getName() + " ... ", parent.parent.getX() + 2, parent.parent.getY() + offset + 4, Client.clientInfo.getColorTheme().moduleDisabledColor());
    }

    @Override
    public boolean updateComponent(int mouseX, int mouseY) {

        if (!getVisible()) return true;

        hovered = isMouseOnButton(mouseX, mouseY);
        y = parent.parent.getY() + offset;
        x = parent.parent.getX();

        return true;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (!getVisible()) return false;

        if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
            ((BThackScreen) mc.currentScreen).closeAfterClicking(() -> mc.setScreen(set.getValue()));
        }

        return isMouseOnButton(mouseX, mouseY);
    }
}
