package com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components;

import com.ferra13671.BThack.Core.Client.Client;
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
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings.Checkbox;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.*;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Shader.Shaders;
import com.ferra13671.BThack.api.Utils.Textures;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;

import java.awt.*;
import java.util.ArrayList;

public class ModuleButton extends Component implements Mc {
	public static final int OUTLINE_COLOR = ColorUtils.fastRGBA(0, 0, 0, 100);
	public static final int BUTTON_HEIGHT = 14;

	public Module module;
	public Frame parent;
	public int offset;

	public boolean open = false;
	public boolean renderOpen = false;

	private boolean isHovered;
	private final ArrayList<AbstractSetting> settings = new ArrayList<>();
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
		int opY = offset + BUTTON_HEIGHT;
		AbstractSetting setting;
		if(Managers.SETTINGS_MANAGER.getSettingsByMod(module) != null) {
			for(Setting s : Managers.SETTINGS_MANAGER.getSettingsByMod(module)){
				setting =
						s instanceof ModeSetting set ? new ModeButton(set, this, opY, set.getIndex(), module) :
						s instanceof NumberSetting set ? new Slider(set, this, opY, module) :
						s instanceof BooleanSetting set ? new Checkbox(set, this, opY, module) :
						s instanceof KeyCodeSetting set ? new KeyCode(this, opY, set, module) :
						s instanceof GuiButtonSetting set ? new OpenGuiButton(set, this, opY, module) :
						s instanceof ColorSetting set ? new ColorPicker(set, this, opY, module) :
						null;
				settings.add(setting);
				opY += setting.getHeight();
			}
		}

		int h = 0;
		if (module.allowRemapVisible) {
			setting = new Visible(this, opY, module);
			h = setting.getHeight();
			settings.add(setting);
		}
		if (module.allowRemapKeyCode) {
			if (module.allowRemapVisible)
				opY += h;
			settings.add(new Keybind(this, opY));
		}
	}

	@Override
	public void setOff(int newOff) {
		setOffInternal(newOff);
	}

	@Override
	public void updateDependencies(int offset) {
		for (AbstractSetting comp : settings)
			comp.updateDependencies(0);

		setOffInternal(offset);
	}

	private void setOffInternal(int offset) {
		this.offset = offset;
		int opY = offset + BUTTON_HEIGHT;
		for(AbstractSetting comp : settings) {
			comp.setOff(opY);
			if (comp.getVisible())
				opY += comp.getHeight();
		}
	}

	@Override
	public void renderComponent() {
		if (settingAnimation.getPassedMillis() <= settingAnimation.getMillis() + 50) {
			lastAnimFactor = settingAnimation.getEase();
			parent.refresh();
		} else if (lastAnimFactor != 1) {
			lastAnimFactor = 1;
			parent.refresh();
		}

		int alpha = (int) (255 * ModuleList.clickGui.opacity.getValue());
		if (!module.isEnabled() || toggleAnimation.getEase() < 1) drawNormalBackground(alpha);
		if (module.isEnabled() || toggleAnimation.getEase() < 1) drawEnabledBackground(alpha);
		if (ModuleList.clickGui.moduleOutline.getValue())
			BThackRender.drawOutlineRect(parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + BUTTON_HEIGHT + offset, 1, OUTLINE_COLOR);

		BThackRender.drawString(module.getName(), (parent.getX() + 5), (parent.getY() + offset + (BUTTON_HEIGHT / 2f) - (FontUtils.getTextHeight(module.getName())) / 2f), getModuleTextColor());

		if (!settings.isEmpty())
			BThackRender.drawTextureRect(open ? Textures.HIDE : Textures.SHOW, parent.getX() + parent.getWidth() - BUTTON_HEIGHT, parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + offset + BUTTON_HEIGHT);
		if(renderOpen || open) {
			if(!settings.isEmpty()) {
				BThackRender.enableScissor(ClickGui.applyGuiScale(parent.getX()), ClickGui.applyGuiScale(parent.getY() + offset), ClickGui.applyGuiScale(parent.getWidth()), ClickGui.applyGuiScale(animatedSettingsHeight + BUTTON_HEIGHT));
				for(AbstractSetting set : settings) {
					if (set.getVisible())
						set.renderComponent();
				}
				BThackRender.disableScissor();
				if (ModuleList.clickGui.settingsOutline.getValue())
					BThackRender.drawOutlineRect(parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + offset + animatedSettingsHeight + BUTTON_HEIGHT, 1, ColorUtils.fastRGBA(255, 255, 255, Math.max(1, (int) ((scInvert ? 1 - settingColorAnimation.getEase() : settingColorAnimation.getEase()) * 255))));
			}
		}
	}

	private int getModuleTextColor() {
		return ModuleList.clickGui.opacity.getValue() > 0.4 ? ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().moduleDisabledColor()) : (module.isEnabled() ? ClickGui.getClickGuiColor(true) : ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().moduleDisabledColor()));
	}

	private void drawEnabledBackground(int alpha) {
		float _alpha = (int) (alpha * (module.isEnabled() ? toggleAnimation.getEase() : 1 - toggleAnimation.getEase())) / 255f;
		if (ModuleList.clickGui.rainbow.getValue()) {
			Shaders.INSTANCE.X_RAINBOW.setUniformValue("alpha", _alpha);
			Shaders.INSTANCE.X_RAINBOW.setUniformValue("brightness", isHovered ? 0.9f : 0.7f);
			ModuleList.clickGui.prepareRainbowShader();
			BThackRender.drawShader(Shaders.INSTANCE.X_RAINBOW, parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + BUTTON_HEIGHT + offset);
		} else {
			BThackRender.drawRect(parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + BUTTON_HEIGHT + offset,
					ColorUtils.integrateAlpha(
							isHovered ?
									new Color(ClickGui.getClickGuiColor(true)).hashCode() :
									new Color(ClickGui.getClickGuiColor(true)).darker().hashCode(),
							(int) (_alpha * 255)
					)
			);
		}
	}

	private void drawNormalBackground(int alpha) {
		BThackRender.drawRect(parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + BUTTON_HEIGHT + offset,
				ColorUtils.integrateAlpha(
						isHovered ?
								new Color(Client.clientInfo.getColorTheme().backgroundHoveredColor()).brighter().brighter().hashCode() :
								new Color(Client.clientInfo.getColorTheme().backgroundColor()).darker().darker().hashCode(),
						alpha
				)
		);
	}



	@Override
	public int getHeight() {
		if(renderOpen || open) {
			int height = 0;
			for (AbstractSetting component : settings) {
				if (component.getVisible())
					height += component.getHeight();
			}
			height = open ? (int) (lastAnimFactor * height) : (int) (height - (lastAnimFactor * height));
			animatedSettingsHeight = height;
			height += BUTTON_HEIGHT;
			return height;
		}
		return BUTTON_HEIGHT;
	}

	@Override
	public boolean updateComponent(int mouseX, int mouseY) {
		if (isMouseOnButton(mouseX, mouseY) && !isHovered) {
			if (parent.buttonHovered) parent.resetHovered();
			isHovered = true;
			parent.buttonHovered = true;
		}
		if(!settings.isEmpty()) {
			for(Component comp : settings) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
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
					settingAnimation = new Animation(ClickGui.getCurrentEasing(), (int) ModuleList.clickGui.animationTime.getValue());
					open = !open;
					settingAnimation.reset();
					parent.refresh();
				}
			}
		}
		if (open) {
			for (Component comp : settings) {
				comp.mouseClicked(mouseX, mouseY, button);
			}
		}
		if (open)
			parent.updateDependencies();

		return isMouseOnButton(mouseX, mouseY);
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		for(Component comp : settings) {
			comp.mouseReleased(mouseX, mouseY, mouseButton);
		}
		if (open)
			parent.updateDependencies();
	}

	@Override
	public void keyTyped(int key) {
		for(Component comp : this.settings) {
			comp.keyTyped(key);
		}
	}

	public boolean isMouseOnButton(int x, int y) {
        return x > ClickGui.applyGuiScale(parent.getX()) && x < ClickGui.applyGuiScale(parent.getX() + parent.getWidth()) &&
				y > ClickGui.applyGuiScale(parent.getY() + offset) && y < ClickGui.applyGuiScale(parent.getY() + BUTTON_HEIGHT + offset);
    }

	public void resetHovered() {
		isHovered = false;
	}
}
