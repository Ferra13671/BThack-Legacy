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
import com.mojang.blaze3d.systems.RenderSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CategoryButton extends AbstractSetting<CategorySetting> {
    private final List<AbstractSetting<?>> subSettings = new ArrayList<>();
    private final List<AbstractSetting<?>> visibleSettings = new CopyOnWriteArrayList<>();
    private final Animation animation = new Animation(Easing.CIRC_OUT, 300);
    private boolean lastRefreshed = false;
    private boolean opened = false;
    private float height;
    private int renderHeight;

    public CategoryButton(CategorySetting setting, ModuleButton button, int offset, Module module) {
        super(offset, button, module, setting);
        Data<Integer> yOffset = new Data<>(offset + Constants.CLICKGUI_BUTTON_HEIGHT);
        setting.getValue().forEach(subSetting -> {
            AbstractSetting<?> component = subSetting.asSettingButton(button, yOffset.get());
            subSettings.add(component);
            yOffset.set(yOffset.get() + component.getHeight());
        });
        subSettings.forEach(subSetting -> {
            if (subSetting.getVisible()) visibleSettings.add(subSetting);
        });
        height = yOffset.get() - offset - Constants.CLICKGUI_BUTTON_HEIGHT;
    }

    @Override
    public int getHeight() {
        renderHeight = opened ? Constants.CLICKGUI_BUTTON_HEIGHT + (int) (height * animation.getEase()) : Constants.CLICKGUI_BUTTON_HEIGHT + (int) ((height - Constants.CLICKGUI_BUTTON_HEIGHT) * (1 - animation.getEase()));
        return renderHeight;
    }

    @Override
    public void refresh(int newOff) {
        super.refresh(newOff);
        Data<Integer> yOffset = new Data<>(newOff + Constants.CLICKGUI_BUTTON_HEIGHT);
        visibleSettings.clear();
        for (AbstractSetting<?> component : subSettings) {
            component.refresh(yOffset.get());
            if (component.getVisible()) {
                yOffset.set(yOffset.get() + component.getHeight());
                visibleSettings.add(component);
            }
        }
        height = yOffset.get() - newOff - Constants.CLICKGUI_BUTTON_HEIGHT;
    }

    @Override
    public void renderComponent() {
        super.renderComponent();

        BThackRender.drawRect(getX(), getY(), getX() + Constants.CLICKGUI_FRAME_WIDTH, getY() + Constants.CLICKGUI_BUTTON_HEIGHT, hovered ? ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundHoveredColor()), (int) (255 * Math.min(1, ModuleList.clickGui.opacity.getValue() + 0.13))) : ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundColor()), (int) (255 * Math.min(1, ModuleList.clickGui.opacity.getValue() + 0.13))));
        BThackRender.drawString(setting.getName(), getX() + 7, getY() + 4, ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().moduleDisabledColor()));
        RenderSystem.setShaderColor(0.7f, 0.7f, 0.7f, 1f);
        BThackRender.drawTextureRect(opened ? Textures.HIDE : Textures.SHOW, getX() + Constants.CLICKGUI_FRAME_WIDTH - Constants.CLICKGUI_BUTTON_HEIGHT, getY(), getX() + Constants.CLICKGUI_FRAME_WIDTH, getY() + Constants.CLICKGUI_BUTTON_HEIGHT);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        BThackRender.enableScissor(getX(), getY(), Constants.CLICKGUI_FRAME_WIDTH, renderHeight);
        if (opened || animation.getEase() < 1)
            for (Component component : visibleSettings)
                component.renderComponent();
        BThackRender.disableScissor();
    }

    @Override
    public boolean updateComponent(int mouseX, int mouseY) {
        if (!getVisible()) return true;
        if (animation.getEase() < 1) parent.parent.refresh();
        else if (!lastRefreshed) {
            parent.parent.refresh();
            lastRefreshed = true;
        }

        hovered = isMouseOnButton(mouseX, mouseY);
        if (opened)
            for (Component component : visibleSettings)
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
            lastRefreshed = false;
            return false;
        }
        if (opened)
            for (Component component : visibleSettings)
                component.mouseClicked(mouseX, mouseY, button);
        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (!getVisible()) return;

        if (opened)
            for (Component component : visibleSettings)
                component.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(int key) {
        if (!getVisible()) return;

        if (opened)
            for (Component component : visibleSettings)
                component.keyTyped(key);
    }
}
