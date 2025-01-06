package com.ferra13671.BThack.api.Gui.ClickGui.component;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Category.Category;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.settings.Slider;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Data;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Frame implements Mc {
	public static final int BAR_HEIGHT = 12;
	public static final int BAR_OUTLINE_COLOR = ColorUtils.fastRGBA(0, 0, 0, 100);
	private static final List<Frame> globalFrames = new ArrayList<>();

	public int id;
	public final ArrayList<ModuleButton> buttons;
	public final String frameName;
	private boolean open;
	private final int width = 100;
	private int y;
	private int x;
	private boolean isDragging;
	public int dragX;
	public int dragY;
	public int height;
	public boolean buttonHovered = false;

	public final Data<Slider> writingSlider;

	public Frame(String name, List<Module> modules, Data<Slider> writingSlider) {
		globalFrames.add(this);

		this.writingSlider = writingSlider;

		buttons = new ArrayList<>();
		frameName = name;
		x = 0;
		y = 60;
		dragX = 0;
		open = true;
		isDragging = false;
		int tY = BAR_HEIGHT;

		for(Module mod : modules) {
			ModuleButton button = new ModuleButton(mod, this, tY);
			buttons.add(button);
			tY += BAR_HEIGHT;
		}
	}
	
	public Frame(Category cat, Data<Slider> writingSlider) {
		this(cat.name(), Client.getModulesInCategory(cat), writingSlider);
	}
	
	public ArrayList<ModuleButton> getButtons() {
		return buttons;
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public void setDrag(boolean drag) {
		isDragging = drag;
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}

	public String getFrameName() {
		return this.frameName;
	}

	public void updateButtons(int mouseX, int mouseY) {
		for (ModuleButton button : getButtons()) {
			button.updateComponent(mouseX, mouseY);
		}
	}

	public void resetHovered() {
		for (ModuleButton button : getButtons()) {
			button.resetHovered();
		}
		buttonHovered = false;
	}

	/**
	 * @return - whether to continue the cycle
	 */
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public boolean updateClick(double mouseX, double mouseY, int mouseButton) {
		if(isWithinHeader((int) mouseX, (int) mouseY) && mouseButton == 0) {
			setDrag(true);
			dragX = (int) (mouseX / ClickGui.guiScale.getValue()) - getX();
			dragY = (int) (mouseY / ClickGui.guiScale.getValue()) - getY();
			return false;
		}
		if(isOpen()) {
			if(!getButtons().isEmpty()) {
				for(Component component : getButtons()) {
					component.mouseClicked((int) mouseX, (int) mouseY, mouseButton);
				}
			}
		}
		if(isWithinHeader((int) mouseX, (int) mouseY) && mouseButton == 1) {
			setOpen(!isOpen());
			if (isOpen()) refresh();
			else height = BAR_HEIGHT;
			return false;
		}
        return !isMouseOnFrame((int) mouseX, (int) mouseY);
    }

	public void updateRelease(int mouseX, int mouseY, int mouseButton) {
		if(isOpen()) {
			if(!getButtons().isEmpty()) {
				for(Component component : getButtons()) {
					component.mouseReleased(mouseX, mouseY, mouseButton);
				}
			}
		}
	}

	public Module getDescriptionModule(double mouseX, double mouseY) {
		if (isOpen()) {
			for (ModuleButton button : buttons) {
				if (button.isMouseOnButton((int) mouseX, (int) mouseY)) return button.module;
			}
		}
		return null;
	}
	
	public void renderFrame() {
		BThackRender.guiGraphics.getMatrices().translate(0,0, 1);

		if(open) {
			if(!buttons.isEmpty()) {
				for(Component component : buttons) {
					component.renderComponent();
				}
			}
		}
		if (ClickGui.rainbow.getValue()) {
			int type = (int) ClickGui.rainbowSpeed.getValue();
			BThackRender.drawHorizontalRainbowRect(x, y, x + width, y + BAR_HEIGHT, type);
		} else {
			BThackRender.drawRect(x, y, x + width, y + BAR_HEIGHT, ClickGui.customColor.getValue() ? new Color((int) ClickGui.redColor.getValue(), (int) ClickGui.greenColor.getValue(), (int) ClickGui.blueColor.getValue()).getRGB() : new Color(Client.clientInfo.getColorTheme().getFontColour()).hashCode());
		}
		if (ClickGui.frameOutline.getValue())
			BThackRender.drawOutlineRect(x, y, x + width, y + BAR_HEIGHT, 1, BAR_OUTLINE_COLOR);
		BThackRender.drawString(frameName, x + (width / 2f) - (mc.textRenderer.getWidth(frameName) / 2f), y + (BAR_HEIGHT / 2f) - (mc.textRenderer.fontHeight / 2f), Client.clientInfo.getColorTheme().getModuleDisabledColour());
	}
	
	public void refresh() {
		int off = BAR_HEIGHT;
		for(Component comp : buttons) {
			comp.setOff(off);
			off += comp.getHeight();
		}
		height = off;
	}

	public void updateDependencies() {
		int off = BAR_HEIGHT;
		for(Component comp : buttons) {
			comp.updateDependencies(off);
			off += comp.getHeight();
		}
		height = off;
	}

	public void tick() {
		for (ModuleButton button : buttons) {
			button.tick();
		}
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void updatePosition(int mouseX, int mouseY) {
		if(isDragging) {
			setX(mouseX - dragX);
			setY(mouseY - dragY);
		}
	}

	public void moveFrame(int keyCode) {
		switch (keyCode) {
			case KeyboardUtils.KEY_LEFT:
				x -= 5;
				break;
			case KeyboardUtils.KEY_RIGHT:
				x += 5;
				break;
			case KeyboardUtils.KEY_UP:
				y -= 5;
				break;
			case KeyboardUtils.KEY_DOWN:
				y += 5;
		}
	}

	public void moveFrame(double deltaX, double deltaY) {
		x += (int) (deltaX * 7);
		y += (int) (deltaY * 7);
	}
	
	public boolean isWithinHeader(int mouseX, int mouseY) {
        return mouseX >= ClickGui.applyGuiScale(x) && mouseX <= ClickGui.applyGuiScale(x + width) &&
				mouseY >= ClickGui.applyGuiScale(y) && mouseY <= ClickGui.applyGuiScale(y + BAR_HEIGHT);
    }

	public boolean isMouseOnFrame(int mouseX, int mouseY) {
		return mouseX >= ClickGui.applyGuiScale(x) && mouseX <= ClickGui.applyGuiScale(x + width) &&
				mouseY >= ClickGui.applyGuiScale(y) && mouseY <= ClickGui.applyGuiScale(y + height);
	}

	public static List<Frame> getGlobalFrames() {
		return new ArrayList<>(globalFrames);
	}
	
}
