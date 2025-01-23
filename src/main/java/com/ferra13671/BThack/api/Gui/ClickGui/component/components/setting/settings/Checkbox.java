package com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.settings;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;

public class Checkbox extends AbstractSetting {

	private final BooleanSetting set;

	protected final Animation animation = new Animation(Easing.LINEAR, 250);
	
	public Checkbox(BooleanSetting option, ModuleButton button, int offset, Module module) {
		super(offset, button, module, option);
		set = option;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;

		animation.reset();
	}

	@Override
	public void renderComponent() {
		BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 15, this.hovered ? ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundHoveredColor()), (int) (255 * Math.min(1, ModuleList.clickGui.opacity.getValue() + 0.13))) : ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundColor()), (int) (255 * Math.min(1, ModuleList.clickGui.opacity.getValue() + 0.13))));

		if (needRenderGradient()) {
			BThackRender.drawHorizontalGradientRect(parent.parent.getX() + 1, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth() / 2f), parent.parent.getY() + offset + 15, getGradientColor(), ColorUtils.TRANSPARENT);
		}

		BThackRender.drawString(getText(), parent.parent.getX() + 7, parent.parent.getY() + offset + 4, ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().moduleDisabledColor()));
	}

	protected boolean needRenderGradient() {
		return set.getValue() || animation.getEase() < 1;
	}

	protected String getText() {
		return op.getName();
	}

	protected int getGradientColor() {
		return ColorUtils.integrateAlpha(ClickGui.getClickGuiColor(true), (int) (255 * (set.getValue() ? animation.getEase() : 1 - animation.getEase())));
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
			set.setValue(!set.getValue());
			set.module.onChangeSetting(set);
			animation.reset();
		}

		return isMouseOnButton(mouseX, mouseY);
	}
}
