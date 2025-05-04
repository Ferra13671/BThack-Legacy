package com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.Component;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.Frame;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings.*;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.api.Utils.Data;
import com.ferra13671.BThack.api.Utils.Textures;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;

import java.awt.*;
import java.util.ArrayList;

public class ModuleButton extends Component implements Mc {

	public Module module;
	public Frame parent;
	public int offset;

	public boolean open = false;
	public boolean renderOpen = false;

	private boolean isHovered;
	private final ArrayList<AbstractSetting<?>> settings = new ArrayList<>();
	private final Animation settingColorAnimation = new Animation(Easing.LINEAR, 1300);
	private boolean scInvert = false;
	private Animation settingAnimation = new Animation(Easing.CIRC_OUT, 500);
	private final Animation toggleAnimation = new Animation(Easing.LINEAR, 250);
	private double lastAnimFactor = 0;
	private int animatedSettingsHeight = 0;

	public ModuleButton(Module module, Frame parent, int offset) {
		this.module = module;
		this.parent = parent;
		this.offset = offset;
		final Data<Integer> opY = new Data<>(offset + Constants.CLICKGUI_BUTTON_HEIGHT);
		final Data<AbstractSetting<?>> setting = new Data<>();
		Managers.SETTINGS_MANAGER.getSettingsByMod(module).forEach(s -> {
			if (s != null) {
				setting.set(s.asSettingButton(this, opY.get()));
				settings.add(setting.get());
				opY.set(opY.get() + setting.get().getHeight());
			}
		});

		int h = 0;
		if (module.allowRemapVisible) {
			setting.set(new Visible(this, opY.get(), module));
			h = setting.get().getHeight();
			settings.add(setting.get());
		}
		if (module.allowRemapKeyCode) {
			if (module.allowRemapVisible)
				opY.set(opY.get() + h);
			settings.add(new Keybind(this, opY.get()));
		}
	}

	public String getName() {
		return module.getName();
	}

	@Override
	public void refresh(int newOff) {
		this.offset = newOff;
		int opY = offset + Constants.CLICKGUI_BUTTON_HEIGHT;
		for(AbstractSetting<?> comp : settings) {
			comp.refresh(opY);
			if (comp.getVisible())
				opY += comp.getHeight();
		}
	}

	@Override
	public void renderComponent() {

		if (!module.isEnabled() || toggleAnimation.getEase() < 1) drawNormalBackground();
		if (module.isEnabled() || toggleAnimation.getEase() < 1) drawEnabledBackground();
		if (ModuleList.clickGui.moduleOutline.getValue())
			BThackRender.drawOutlineRect(parent.getX(), parent.getY() + offset, parent.getX() + Constants.CLICKGUI_FRAME_WIDTH, parent.getY() + Constants.CLICKGUI_BUTTON_HEIGHT + offset, 1, Constants.CLICKGUI_BUTTON_OUTLINE_COLOR);

		BThackRender.drawString(getName(), (parent.getX() + 5), (parent.getY() + offset + (Constants.CLICKGUI_BUTTON_HEIGHT / 2f) - (FontUtils.getTextHeight(module.getName())) / 2f), getModuleTextColor());

		if (ModuleList.clickGui.arrows.getValue() && !settings.isEmpty())
			BThackRender.drawTextureRect(open ? Textures.HIDE : Textures.SHOW, parent.getX() + Constants.CLICKGUI_FRAME_WIDTH - Constants.CLICKGUI_BUTTON_HEIGHT, parent.getY() + offset, parent.getX() + Constants.CLICKGUI_FRAME_WIDTH, parent.getY() + offset + Constants.CLICKGUI_BUTTON_HEIGHT);
		if(renderOpen || open) {
			if(!settings.isEmpty()) {
				BThackRender.enableScissor(ClickGui.applyGuiScale(parent.getX()), ClickGui.applyGuiScale(parent.getY() + offset), ClickGui.applyGuiScale(Constants.CLICKGUI_FRAME_WIDTH), ClickGui.applyGuiScale(animatedSettingsHeight + Constants.CLICKGUI_BUTTON_HEIGHT));
				for(AbstractSetting<?> set : settings)
					if (set.getVisible())
						set.renderComponent();
				BThackRender.disableScissor();
				if (ModuleList.clickGui.settingsOutline.getValue())
					BThackRender.drawOutlineRect(parent.getX(), parent.getY() + offset, parent.getX() + Constants.CLICKGUI_FRAME_WIDTH, parent.getY() + offset + animatedSettingsHeight + Constants.CLICKGUI_BUTTON_HEIGHT, 1, ColorUtils.fastRGBA(255, 255, 255, Math.max(1, (int) ((scInvert ? 1 - settingColorAnimation.getEase() : settingColorAnimation.getEase()) * 255))));
			}
		}
	}

	private int getModuleTextColor() {
		return ModuleList.clickGui.opacity.getValue() > 0.4 ? ModuleList.clickGui.textColor.getValue().hashCode() : (module.isEnabled() ? ClickGui.getClickGuiColor(true) : ModuleList.clickGui.textColor.getValue().hashCode());
	}

	protected void drawEnabledBackground() {
		float _alpha = (int) (ClickGui.INT_OPACITY * (module.isEnabled() ? toggleAnimation.getEase() : 1 - toggleAnimation.getEase())) / 255f;
		if (ModuleList.clickGui.isShaderEnabled()) {
			ModuleList.clickGui.prepareCurrentShader(_alpha, isHovered ? 0.9f : 0.7f);
			BThackRender.drawShader(ModuleList.clickGui.getCurrentShader(), parent.getX(), parent.getY() + offset, parent.getX() + Constants.CLICKGUI_FRAME_WIDTH, parent.getY() + Constants.CLICKGUI_BUTTON_HEIGHT + offset);
		} else {
			BThackRender.drawRect(parent.getX(), parent.getY() + offset, parent.getX() + Constants.CLICKGUI_FRAME_WIDTH, parent.getY() + Constants.CLICKGUI_BUTTON_HEIGHT + offset,
					ColorUtils.integrateAlpha(
							isHovered ?
									new Color(ClickGui.getClickGuiColor(true)).hashCode() :
									new Color(ClickGui.getClickGuiColor(true)).darker().hashCode(),
							(int) (_alpha * 255)
					)
			);
		}
	}

	protected void drawNormalBackground() {
		BThackRender.drawRect(parent.getX(), parent.getY() + offset, parent.getX() + Constants.CLICKGUI_FRAME_WIDTH, parent.getY() + Constants.CLICKGUI_BUTTON_HEIGHT + offset,
				ColorUtils.integrateAlpha(
						isHovered ?
								ModuleList.clickGui.backgroundColor.getBrighterValue().hashCode() :
								ModuleList.clickGui.backgroundColor.getValue().hashCode(),
						ClickGui.INT_OPACITY
				)
		);
	}



	@Override
	public int getHeight() {
		if(renderOpen || open) {
			int height = 0;
			for (AbstractSetting<?> component : settings)
				if (component.getVisible())
					height += component.getHeight();
			height = open ? (int) (lastAnimFactor * height) : (int) (height - (lastAnimFactor * height));
			animatedSettingsHeight = height;
			height += Constants.CLICKGUI_BUTTON_HEIGHT;
			return height;
		}
		return Constants.CLICKGUI_BUTTON_HEIGHT;
	}

	public void updateAnim() {
		if (settingAnimation.getPassedMillis() <= settingAnimation.getMillis() + 50) {
			lastAnimFactor = settingAnimation.getEase();
			parent.refresh();
		} else if (lastAnimFactor != 1) {
			lastAnimFactor = 1;
			parent.refresh();
		}
	}

	@Override
	public boolean updateComponent(int mouseX, int mouseY) {
		if (isMouseOnButton(mouseX, mouseY) && !isHovered) {
			if (parent.buttonHovered) parent.resetHovered();
			isHovered = true;
			parent.buttonHovered = true;
		}
		if(!settings.isEmpty())
			for(Component comp : settings)
				comp.updateComponent(mouseX, mouseY);
		return false;
	}

	public void tick() {
		if (settingAnimation.getPassedMillis() > settingAnimation.getMillis() + 50) if (renderOpen != open) renderOpen = open;
		if (renderOpen) {
			if (settingColorAnimation.getEase() >= 1) {
				scInvert = !scInvert;
				settingColorAnimation.reset();
			}
		}
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int button) {
		if (isMouseOnButton(mouseX, mouseY)) {
			if (button == 0) {
				module.toggle();
				toggleAnimation.reset();
			}
			if (button == 1) {
				if (renderOpen == open) {
					settingAnimation = new Animation(ClickGui.getCurrentEasing(), ModuleList.clickGui.animationTime.getValue().intValue());
					open = !open;
					settingAnimation.reset();
					lastAnimFactor = 0;
					parent.refresh();
				}
			}
		}
		if (open) {
			for (Component comp : settings)
				comp.mouseClicked(mouseX, mouseY, button);
			parent.refresh();
		}

		return isMouseOnButton(mouseX, mouseY);
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		for(Component comp : settings)
			comp.mouseReleased(mouseX, mouseY, mouseButton);
		if (open)
			parent.refresh();
	}

	@Override
	public void keyTyped(int key) {
		for(Component comp : settings)
			comp.keyTyped(key);
	}

	public boolean isMouseOnButton(int x, int y) {
        return x > ClickGui.applyGuiScale(parent.getX()) && x < ClickGui.applyGuiScale(parent.getX() + Constants.CLICKGUI_FRAME_WIDTH) &&
				y > ClickGui.applyGuiScale(parent.getY() + offset) && y < ClickGui.applyGuiScale(parent.getY() + Constants.CLICKGUI_BUTTON_HEIGHT + offset);
    }

	public void resetHovered() {
		isHovered = false;
	}
}
