package com.ferra13671.BThack.api.Gui.Screen;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackMatrix;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.GuiSystem.Screen.BThackScreen;
import com.ferra13671.BThack.mixins.accessor.IScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class TransitionScreen extends BThackScreen {
    public static final Animation STANDARD_FLIP_ANIMATION = new Animation(Easing.LINEAR, 500);

    private final Supplier<Screen> newScreen;
    private final Animation flipAnimation;

    private Screen currentScreen;
    private boolean invert = false;

    public TransitionScreen(Supplier<Screen> oldScreen, Supplier<Screen> newScreen, Animation flipAnimation) {
        super(Text.literal("Transition"));
        currentScreen = oldScreen.get();
        this.newScreen = newScreen;
        this.flipAnimation = flipAnimation.clone();
        this.flipAnimation.setMillis((int) (this.flipAnimation.getMillis() / ModuleList.bthackMainMenu.animationSpeed.getValue()));
        this.flipAnimation.reset();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected void init() {
        super.init();
        ((IScreen) currentScreen)._init();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        currentScreen.render(context, mouseX, mouseY, partialTicks);

        double animationDelta = !invert ? flipAnimation.getEase() : 1 - flipAnimation.getEase();

        BThackMatrix.push();
        BThackMatrix.translate(0, 0, 9000);
        BThackRender.drawRect(0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), ColorUtils.integrateAlpha(ColorUtils.BLACK, (int) (animationDelta * 255)));
        BThackMatrix.pop();
    }

    @Override
    public void tick() {
        if (flipAnimation.getEase() >= 1) {
            if (!invert) {
                invert = true;
                flipAnimation.reset();
                currentScreen = newScreen.get();
                currentScreen.init(mc, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
                ((IScreen) currentScreen)._init();
                currentScreen.onDisplayed();
            } else mc.setScreen(newScreen.get());
        }
    }
}
