package com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting;

import com.ferra13671.BThack.api.Gui.ClickGui.component.Component;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;

public abstract class AbstractSetting extends Component {

    public int x;
    public int y;

    public int offset;

    public boolean hovered;

    private boolean visible = true;

    public final ModuleButton parent;
    public final Module module;
    public final Setting op;

    public AbstractSetting(int offset, ModuleButton button, Module module, Setting op) {
        this.offset = offset;
        this.parent = button;
        this.module = module;
        this.op = op;
    }

    @Override
    public abstract void renderComponent();

    @Override
    public void setOff(int newOff) {
        offset = newOff;
    }

    @Override
    public void updateDependencies(int offset) {
        if (op.dependence != null)
            setVisible(op.dependence.get());
    }

    @Override
    public int getHeight() {
        return 15;
    }

    @Override
    public abstract boolean updateComponent(int mouseX, int mouseY);

    @Override
    public abstract boolean mouseClicked(int mouseX, int mouseY, int button);

    public boolean isMouseOnButton(int x, int y) {
        return x > ClickGui.applyGuiScale(this.x) && x < ClickGui.applyGuiScale(this.x + 100) &&
                y > ClickGui.applyGuiScale(this.y) && y < ClickGui.applyGuiScale(this.y + 15);
    }

    public boolean getVisible() {
        return this.visible;
    }

    public void setVisible(boolean in) {
        this.visible = in;
    }
}
