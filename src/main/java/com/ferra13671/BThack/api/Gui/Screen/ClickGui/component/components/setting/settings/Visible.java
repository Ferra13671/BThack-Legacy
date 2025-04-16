package com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings;


import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.SoundSystem.SoundSystem;
import com.ferra13671.BThack.api.SoundSystem.Sounds;
import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;

public class Visible extends Checkbox {

    public Visible(ModuleButton parent, int offset, Module module) {
        super(null, parent, offset, module);
    }

    @Override
    public void renderComponent() {
        y = parent.parent.getY() + offset;
        x = parent.parent.getX();

        BThackRender.drawRect(x, y, x + Constants.CLICKGUI_FRAME_WIDTH, y + getHeight(), this.hovered ? ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundHoveredColor()), (int) (255 * Math.min(1, ModuleList.clickGui.opacity.getValue() + 0.13))) : ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundColor()), (int) (255 * Math.min(1, ModuleList.clickGui.opacity.getValue() + 0.13))));

        if (needRenderPlate()) {
            int alpha = (int) (255 * (module.isVisible() ? animation.getEase() : 1 - animation.getEase()));
            if (ModuleList.clickGui.isShaderEnabled()) {
                ModuleList.clickGui.prepareCurrentShader(alpha / 255f, 0.7f);
                BThackRender.drawShader(ModuleList.clickGui.getCurrentShader(), x + 1, y, x + Constants.CLICKGUI_FRAME_WIDTH - 1, y + getHeight());
            } else
                BThackRender.drawRect(x + 1, y, x + Constants.CLICKGUI_FRAME_WIDTH - 1, y + getHeight(), ColorUtils.integrateAlpha(ClickGui.getClickGuiColor(true), alpha));

            BThackRender.drawOutlineRect(x + 1, y, x + Constants.CLICKGUI_FRAME_WIDTH - 1, y + getHeight(), 1, Constants.CLICKGUI_BUTTON_OUTLINE_COLOR);
        }

        BThackRender.drawString(getText(), x + 7, y + 4, ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().moduleDisabledColor()));
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
