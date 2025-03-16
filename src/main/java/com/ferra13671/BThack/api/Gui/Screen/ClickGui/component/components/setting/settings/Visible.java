package com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings;


import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Shader.Shaders;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;

public class Visible extends Checkbox {

    public Visible(ModuleButton parent, int offset, Module module) {
        super(null, parent, offset, module);

        x = parent.parent.getX() + parent.parent.getWidth();
        y = parent.parent.getY() + parent.offset;
    }

    @Override
    public void renderComponent() {
        BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 15, this.hovered ? ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundHoveredColor()), (int) (255 * Math.min(1, ModuleList.clickGui.opacity.getValue() + 0.13))) : ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundColor()), (int) (255 * Math.min(1, ModuleList.clickGui.opacity.getValue() + 0.13))));

        if (needRenderPlate()) {
            int alpha = (int) (255 * (module.isVisible() ? animation.getEase() : 1 - animation.getEase()));
            if (ModuleList.clickGui.rainbow.getValue()) {
                Shaders.INSTANCE.X_RAINBOW.setUniformValue("alpha", alpha / 255f);
                Shaders.INSTANCE.X_RAINBOW.setUniformValue("brightness", 0.7f);
                ModuleList.clickGui.prepareRainbowShader();
                BThackRender.drawShader(Shaders.INSTANCE.X_RAINBOW, parent.parent.getX() + 1, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() - 1, parent.parent.getY() + offset + 15);
            } else
                BThackRender.drawRect(parent.parent.getX() + 1, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() - 1, parent.parent.getY() + offset + 15, ColorUtils.integrateAlpha(ClickGui.getClickGuiColor(true), alpha));

            BThackRender.drawOutlineRect(parent.parent.getX() + 1, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() - 1, parent.parent.getY() + offset + 15, 1, ModuleButton.OUTLINE_COLOR);
        }

        BThackRender.drawString(getText(), parent.parent.getX() + 7, parent.parent.getY() + offset + 4, ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().moduleDisabledColor()));
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
            module.setVisible(!module.isVisible());
            animation.reset();
        }
        return isMouseOnButton(mouseX, mouseY);
    }
}
