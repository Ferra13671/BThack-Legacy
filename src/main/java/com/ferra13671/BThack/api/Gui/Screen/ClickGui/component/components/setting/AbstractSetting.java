package com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting;

import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.Component;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;
import org.joml.Vector2i;

public abstract class AbstractSetting<T extends Setting<?>> extends Component {
    public final Vector2i position = new Vector2i(0, 0);
    public int offset;
    public boolean hovered;
    private boolean visible = true;

    public final ModuleButton parent;
    public final Module module;
    public final T setting;

    public AbstractSetting(int offset, ModuleButton button, Module module, T setting) {
        this.offset = offset;
        this.parent = button;
        this.module = module;
        this.setting = setting;
    }

    @Override
    public void renderComponent() {
        position.set(parent.parent.getX(), parent.parent.getY() + offset);
    }

    @Override
    public void refresh(int newOff) {
        offset = newOff;
        if (setting != null && setting.dependence != null)
            setVisible(setting.dependence.get());
    }

    @Override
    public int getHeight() {
        return 15;
    }

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }

    @Override
    public abstract boolean updateComponent(int mouseX, int mouseY);

    @Override
    public abstract boolean mouseClicked(int mouseX, int mouseY, int button);

    public boolean isMouseOnButton(int x, int y) {
        return x > ClickGui.applyGuiScale(getX()) && x < ClickGui.applyGuiScale(getX() + 100) &&
                y > ClickGui.applyGuiScale(getY()) && y < ClickGui.applyGuiScale(getY() + 15);
    }

    public boolean getVisible() {
        return this.visible;
    }

    public void setVisible(boolean in) {
        this.visible = in;
    }
}
