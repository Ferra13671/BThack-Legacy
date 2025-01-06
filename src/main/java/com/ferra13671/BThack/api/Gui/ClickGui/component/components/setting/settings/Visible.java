package com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.settings;


import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;

public class Visible extends Checkbox {

    public Visible(ModuleButton parent, int offset, Module module) {
        super(null, parent, offset, module);

        x = parent.parent.getX() + parent.parent.getWidth();
        y = parent.parent.getY() + parent.offset;
    }

    @Override
    public void renderComponent() {
        BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 15, this.hovered ? ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().getBackgroundFontHoveredColour()), (int) (255 * Math.min(1, ClickGui.opacity.getValue() + 0.13))) : ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().getBackgroundFontColour()), (int) (255 * Math.min(1, ClickGui.opacity.getValue() + 0.13))));

        if (module.visible) {
            BThackRender.drawHorizontalGradientRect(parent.parent.getX() + 1, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth() / 2f), parent.parent.getY() + offset + 15, ClickGui.getClickGuiColor(true), ColorUtils.TRANSPARENT);
        }

        BThackRender.drawString("Visible", parent.parent.getX() + 7, parent.parent.getY() + offset + 4, Client.clientInfo.getColorTheme().getModuleDisabledColour());
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
        if (isMouseOnButton(mouseX, mouseY) && button == 0 && parent.open) {
            module.visible = !module.visible;
        }
        return isMouseOnButton(mouseX, mouseY);
    }
}
