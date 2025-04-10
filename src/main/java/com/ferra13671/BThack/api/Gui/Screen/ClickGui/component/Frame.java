package com.ferra13671.BThack.api.Gui.Screen.ClickGui.component;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackMatrix;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Category.Category;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings.Slider;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Shader.Shaders;
import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.api.Utils.Data;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

public class Frame implements Mc, Closeable {
	private static final List<Frame> GLOBAL_FRAMES = new ArrayList<>();

	public int id;
	public final ArrayList<ModuleButton> buttons = new ArrayList<>();
	public final String frameName;
	private boolean open = true;
	private int y = 0;
	private int x = 0;
	private boolean isDragging = false;
	public int dragX = 0;
	public int dragY = 0;
	public int height;
	public float renderHeight;
	public boolean buttonHovered = false;
	private final Animation frameAnimation = new Animation(Easing.CUBIC_OUT, 500);

	public final Data<Slider> writingSlider;

	public Frame(String name, List<Module> modules, Data<Slider> writingSlider) {
		GLOBAL_FRAMES.add(this);

		this.writingSlider = writingSlider;

		frameName = name;
		int tY = Constants.CLICKGUI_BAR_HEIGHT;

		for(Module mod : modules) {
			ModuleButton button = new ModuleButton(mod, this, tY);
			buttons.add(button);
			tY += Constants.CLICKGUI_BAR_HEIGHT;
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

	public void resetAnimationIgnoreOpen() {
		frameAnimation.reset();
	}

	public void resetAnimation() {
		if (isOpen())
			frameAnimation.reset();
	}

	/**
	 * @return - whether to continue the cycle
	 */
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public boolean updateClick(double mouseX, double mouseY, int mouseButton) {
		if(isWithinHeader((int) mouseX, (int) mouseY) && mouseButton == 0) {
			setDrag(true);
			dragX = (int) (mouseX / ModuleList.clickGui.guiScale.getValue()) - getX();
			dragY = (int) (mouseY / ModuleList.clickGui.guiScale.getValue()) - getY();
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
			resetAnimationIgnoreOpen();
			if (isOpen()) refresh();
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
		BThackMatrix.translate(0,0, 1);

		boolean needScissor = frameAnimation.getEase() < 1;
		renderHeight = needScissor ? (float) (height * (open ? frameAnimation.getEase() : 1 - frameAnimation.getEase())) : open ? height : 0;

		if (ModuleList.clickGui.rainbow.getValue()) {
			ModuleList.clickGui.prepareRainbowShader();
			BThackRender.drawShader(Shaders.INSTANCE.X_RAINBOW, x, y, x + Constants.CLICKGUI_FRAME_WIDTH, y + Constants.CLICKGUI_BAR_HEIGHT);
		} else
			BThackRender.drawRect(x, y, x + Constants.CLICKGUI_FRAME_WIDTH, y + Constants.CLICKGUI_BAR_HEIGHT, ModuleList.clickGui.customColor.getValue() ? ColorUtils.fastRGBA(ModuleList.clickGui.color.getValue().getRed(), ModuleList.clickGui.color.getValue().getGreen(), ModuleList.clickGui.color.getValue().getBlue(), 255) : ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().color()));
		if (ModuleList.clickGui.frameOutline.getValue()) {
			if (ModuleList.clickGui.rainbow.getValue()) {
				ModuleList.clickGui.prepareRainbowShader();
				BThackRender.drawShaderOutlineRect(Shaders.INSTANCE.X_RAINBOW, x - 1, y - 1, x + Constants.CLICKGUI_FRAME_WIDTH + 1, y + renderHeight + Constants.CLICKGUI_BAR_HEIGHT + 1, 1);
			} else {
				BThackRender.drawOutlineRect(x - 1, y - 1, x + Constants.CLICKGUI_FRAME_WIDTH + 1, y + renderHeight + Constants.CLICKGUI_BAR_HEIGHT + 1, 1, ClickGui.getClickGuiColor(true));
			}
		}

		BThackRender.drawString(frameName, x + (Constants.CLICKGUI_FRAME_WIDTH / 2f) - (FontUtils.getTextWidth(frameName) / 2f), y + (Constants.CLICKGUI_BAR_HEIGHT / 2f) - (FontUtils.getTextHeight(frameName) / 2f), ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().moduleDisabledColor()), true, FontRenderManager.DrawMode.NORMAL_BOLD);

		if(open || frameAnimation.getEase() < 1) {
			if(!buttons.isEmpty()) {
				if (needScissor)
					BThackRender.enableScissor(ClickGui.applyGuiScale(x), ClickGui.applyGuiScale(y + Constants.CLICKGUI_BAR_HEIGHT), ClickGui.applyGuiScale(Constants.CLICKGUI_FRAME_WIDTH), (int) ClickGui.applyGuiScale(renderHeight));
				for(Component component : buttons) {
					component.renderComponent();
				}
				BThackMatrix.translate(0, 0, -1);
				if (needScissor)
					BThackRender.disableScissor();
			}
		}
	}
	
	public void refresh() {
		int off = Constants.CLICKGUI_BAR_HEIGHT;
		for(Component comp : buttons) {
			comp.refresh(off);
			off += comp.getHeight();
		}
		height = off - Constants.CLICKGUI_BAR_HEIGHT;
	}

	public void tick() {
		for (ModuleButton button : buttons) {
			button.tick();
		}
	}

	@Override
	public void close() {
		for (Component comp : buttons)
			comp.mouseReleased(0, 0, 0);
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
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
        return mouseX >= ClickGui.applyGuiScale(x) && mouseX <= ClickGui.applyGuiScale(x + Constants.CLICKGUI_FRAME_WIDTH) &&
				mouseY >= ClickGui.applyGuiScale(y) && mouseY <= ClickGui.applyGuiScale(y + Constants.CLICKGUI_BAR_HEIGHT);
    }

	public boolean isMouseOnFrame(int mouseX, int mouseY) {
		return mouseX >= ClickGui.applyGuiScale(x) && mouseX <= ClickGui.applyGuiScale(x + Constants.CLICKGUI_FRAME_WIDTH) &&
				mouseY >= ClickGui.applyGuiScale(y) && mouseY <= ClickGui.applyGuiScale(y + renderHeight + Constants.CLICKGUI_BAR_HEIGHT);
	}

	public static List<Frame> getGlobalFrames() {
		return new ArrayList<>(GLOBAL_FRAMES);
	}
	
}
