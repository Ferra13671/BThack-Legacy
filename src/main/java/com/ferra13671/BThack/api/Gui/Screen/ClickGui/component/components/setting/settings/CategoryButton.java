package com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings;

import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.Component;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.CategorySetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Data;
import com.ferra13671.BThack.api.Utils.Textures;

import java.util.ArrayList;
import java.util.List;

public class CategoryButton extends AbstractSetting<CategorySetting> {
    private final List<Component> subSettings = new ArrayList<>();
    private final Animation animation = new Animation(Easing.CIRC_OUT, 300);
    private boolean opened = false;
    private float height = 0;

    public CategoryButton(CategorySetting setting, ModuleButton button, int offset, Module module) {
        super(offset, button, module, setting);
        Data<Integer> yOffset = new Data<>(offset + getHeight());
        setting.getValue().forEach(subSetting -> {
            Component component = subSetting.asSettingButton(button, yOffset.get());
            subSettings.add(component);
            yOffset.set(yOffset.get() + component.getHeight());
        });
        height = yOffset.get();
    }

    @Override
    public int getHeight() {
        return opened ? (int) (height * animation.getEase()) : Constants.CLICKGUI_BUTTON_HEIGHT + (int) ((height - Constants.CLICKGUI_BUTTON_HEIGHT) * (1 - animation.getEase()));
    }

    @Override
    public void refresh(int newOff) {
        super.refresh(newOff);
        Data<Integer> yOffset = new Data<>(newOff + getHeight());
        for (Component component : subSettings) {
            component.refresh(yOffset.get());
            yOffset.set(yOffset.get() + component.getHeight());
        }
    }

    @Override
    public void renderComponent() {
        super.renderComponent();

        BThackRender.drawRect(getX(), getY(), getX() + Constants.CLICKGUI_FRAME_WIDTH, getY() + Constants.CLICKGUI_BUTTON_HEIGHT, hovered ? ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundHoveredColor()), (int) (255 * Math.min(1, ModuleList.clickGui.opacity.getValue() + 0.13))) : ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundColor()), (int) (255 * Math.min(1, ModuleList.clickGui.opacity.getValue() + 0.13))));
        BThackRender.drawString(setting.getName(), getX() + 7, getY() + 4, ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().moduleDisabledColor()));
        BThackRender.drawTextureRect(opened ? Textures.HIDE : Textures.SHOW, getX() + Constants.CLICKGUI_FRAME_WIDTH - Constants.CLICKGUI_BUTTON_HEIGHT, getY(), getX() + Constants.CLICKGUI_FRAME_WIDTH, getY() + Constants.CLICKGUI_BUTTON_HEIGHT);

        if (opened)
            for (Component component : subSettings)
                component.renderComponent();
    }

    @Override
    public boolean updateComponent(int mouseX, int mouseY) {
        if (!getVisible()) return true;

        hovered = isMouseOnButton(mouseX, mouseY);
        if (opened)
            for (Component component : subSettings)
                component.updateComponent(mouseX, mouseY);
        return true;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (!getVisible()) return false;

        if (isMouseOnButton(mouseX, mouseY)) {
            opened = !opened;
            parent.parent.refresh();
            animation.reset();
            return false;
        }
        if (opened)
            for (Component component : subSettings)
                component.mouseClicked(mouseX, mouseY, button);
        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (!getVisible()) return;

        if (opened)
            for (Component component : subSettings)
                component.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(int key) {
        if (!getVisible()) return;

        if (opened)
            for (Component component : subSettings)
                component.keyTyped(key);
    }
}
