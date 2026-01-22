package com.ferra13671.BThack.core.client.systems.gui.Screen;

import com.ferra13671.BThack.core.client.ModuleList;
import com.ferra13671.BThack.core.render.BThackRender;
import com.ferra13671.BThack.core.render.utils.ColorUtils;
import com.ferra13671.BThack.api.animation.Animation;
import com.ferra13671.BThack.gui.screen.TransitionScreen;
import com.ferra13671.BThack.api.utils.Mc;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.core.client.systems.gui.buttons.Button;
import com.ferra13671.BThack.Constants;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.function.Supplier;

public class BThackScreen extends Screen implements Mc {
    public ArrayList<Button> buttons = new ArrayList<>();
    public final WidgetManage widgetManage = new WidgetManage(this);

    public Button activeButton = Button.of(Integer.MIN_VALUE, -100, -100, 1, 1, "nullButton");

    private boolean actionAfterClicking = false;
    private Runnable afterClickAction;

    protected BThackScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        widgetManage.init();
    }

    @Override
    public void tick() {
        widgetManage.tick();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        for (Button button : buttons) {
            if (!button.isHided()) {
                if (widgetManage.widgets.isEmpty())
                    button.updateButton(mouseX, mouseY);

                button.renderButton();
            }
        }
        widgetManage.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (widgetManage.widgets.isEmpty()) {
            activeButton = Button.of(Integer.MIN_VALUE, -100, -100, 1, 1, "nullButton");

            for (Button button : buttons) {
                if (!button.isHided()) {
                    button.mouseClicked((int) mouseX, (int) mouseY, mouseButton);
                    if (button.isMouseOnButton((int) mouseX, (int) mouseY) && mouseButton == 0) {
                        activeButton = button;
                        button.clickAction((int) mouseX, (int) mouseY, mouseButton);
                    }
                }
            }
            checkCloseAfterClicking();
        }
        widgetManage.mouseClicked(mouseX, mouseY, mouseButton);
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void checkCloseAfterClicking() {
        if (actionAfterClicking) {
            actionAfterClicking = false;
            afterClickAction.run();
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        for (Button button : buttons)
            button.mouseReleased((int) mouseX, (int) mouseY, mouseButton);
        widgetManage.mouseReleased(mouseX, mouseY, mouseButton);
        return super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        widgetManage.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        return false;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (widgetManage.widgets.isEmpty())
            for (Button button : buttons)
                if (!button.isHided())
                    button.charTyped(chr);

        widgetManage.charTyped(chr, modifiers);
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int shift) {
        if (widgetManage.widgets.isEmpty())
            for (Button button : buttons)
                if (!button.isHided())
                    button.keyTyped(keyCode);

        widgetManage.keyPressed(keyCode, scanCode, shift);
        return super.keyPressed(keyCode, scanCode, shift);
    }

    public Button getButtonFromId(int id) {
        for (Button button : buttons)
            if (button.getId() == id)
                return button;
        return null;
    }

    public void actionAfterClicking(Runnable action) {
        actionAfterClicking = true;
        afterClickAction = action;
    }

    public void drawBackGround(int mouseX, int mouseY) {
        if (Module.nullCheck()) drawMainMenuWallpaper(mouseX, mouseY);
        BThackRender.draw4ColorRect( 0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), Constants.SCREEN_BACKGROUND_TABLE_COLOR, Constants.SCREEN_BACKGROUND_TABLE_COLOR, ColorUtils.fastRGBA(161,0, 255, 255), ColorUtils.fastRGBA(255, 0, 0, 255));
    }

    public void drawMainMenuWallpaper(float mouseX, float mouseY) {
        if (ModuleList.menuShader.isEnabled()) {
            float width = mc.getWindow().getScaledWidth();
            float height = mc.getWindow().getScaledHeight();

            Managers.MAIN_MENU_SHADER_MANAGER.update();

            Managers.MAIN_MENU_SHADER_MANAGER.getMainMenuShader().use();
            Managers.MAIN_MENU_SHADER_MANAGER.getMainMenuShader().setParameters(mouseX, mouseY, width, height, Managers.MAIN_MENU_SHADER_MANAGER.getShaderTime());
            BThackRender.drawShader(Managers.MAIN_MENU_SHADER_MANAGER.getMainMenuShader(), 0, 0, width, height);
        } else
            ROTATING_PANORAMA_RENDERER.render(BThackRender.getGuiGraphics(), this.width, this.height, 1.0F, 0.25f);
    }

    public int getX100P() {
        return mc.getWindow().getScaledWidth() / 100;
    }

    public void changeScreen(Supplier<Screen> screen, Animation animation) {
        changeScreen(this, screen, animation);
    }

    public static void changeScreen(Screen currentScreen, Supplier<Screen> nextScreen, Animation animation) {
        mc.setScreen(ModuleList.bthackMainMenu.screenChangeAnimation.getValue() ?
                new TransitionScreen(() -> currentScreen, nextScreen, animation) :
                nextScreen.get()
        );

    }

    public void changeScreen(Supplier<Screen> screen) {
        changeScreen(this, screen);
    }

    public static void changeScreen(Screen currentScreen, Supplier<Screen> nextScreen) {
        mc.setScreen(ModuleList.bthackMainMenu.screenChangeAnimation.getValue() ?
                new TransitionScreen(() -> currentScreen, nextScreen, Constants.STANDARD_FLIP_ANIMATION) :
                nextScreen.get()
        );
    }
}
