package com.ferra13671.BThack.api.GuiSystem.Screen;

import com.ferra13671.BThack.Core.Render.BThackMatrix;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.GuiSystem.ScreenWidget;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.SoundSystem.SoundSystem;
import com.ferra13671.BThack.api.SoundSystem.Sounds;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class WidgetManage implements Mc {
    private static final int WIDGET_ANIMATION_TIME = 1000;

    public final List<WidgetInfo> widgets = new ArrayList<>();
    private final BThackScreen parent;

    public WidgetManage(BThackScreen parent) {
        this.parent = parent;
    }

    public void addWidget(ScreenWidget screenWidget) {
        widgets.add(new WidgetInfo(screenWidget));
        screenWidget.setParent(parent);
        screenWidget.init();
    }

    public void removeWidget(ScreenWidget screenWidget) {
        for (WidgetInfo widgetInfo : widgets) {
            if (widgetInfo.screenWidget.equals(screenWidget)) {
                if (widgetInfo.status == WidgetStatus.NOT_OPENED) {
                    widgets.remove(widgetInfo);
                    return;
                } else widgetInfo.setStatus(WidgetStatus.CLOSED);
            }
        }
    }

    public void init() {
        if (!widgets.isEmpty())
            widgets.forEach(widgetInfo -> widgetInfo.screenWidget.init());
    }

    public void tick() {
        if (!widgets.isEmpty()) {
            WidgetInfo widget = widgets.getFirst();
            if (widget.screenWidget.needClose) {
                widget.setStatus(WidgetStatus.CLOSED);
                widget.soundPlayed = false;
                widget.screenWidget.needClose = false;
                return;
            }
            switch (widget.status) {
                case NOT_OPENED -> {
                    widget.setStatus(WidgetStatus.OPENED);
                    widget.screenWidget.onDisplayed();
                    return;
                }
                case OPENED -> {
                    if (!widget.soundPlayed && ((double) widget.widgetAnimation.getPassedMillis() / widget.animTime) > 0.3) {
                        SoundSystem.playSound(Sounds.GUI_WIDGET_SHOW, 0.3f);
                        widget.soundPlayed = true;
                    }
                    if (widget.widgetAnimation.getPassedMillis() >= widget.animTime)
                        if (!widget.allowUpdate)
                            widget.setAllowUpdate(true);
                }
                case CLOSED -> {
                    if (!widget.soundPlayed && ((double) widget.widgetAnimation.getPassedMillis() / widget.animTime) > 0.2) {
                        SoundSystem.playSound(Sounds.GUI_WIDGET_HIDE, 0.3f);
                        widget.soundPlayed = true;
                    }
                    if (widget.widgetAnimation.getPassedMillis() >= widget.animTime) {
                        widgets.remove(widget);
                        return;
                    }
                }
            }

            if (widget.allowUpdate) widget.screenWidget.tick();
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (!widgets.isEmpty()) {
            WidgetInfo widget = widgets.getFirst();
            if (widget.allowUpdate) widget.screenWidget.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
        if (!widgets.isEmpty()) {
            WidgetInfo widget = widgets.getFirst();
            if (widget.allowUpdate) widget.screenWidget.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    public void charTyped(char chr, int modifiers) {
        if (!widgets.isEmpty()) {
            WidgetInfo widget = widgets.getFirst();
            if (widget.allowUpdate) widget.screenWidget.charTyped(chr, modifiers);
        }
    }

    public void keyPressed(int keyCode, int scanCode, int shift) {
        if (!widgets.isEmpty()) {
            WidgetInfo widget = widgets.getFirst();
            if (widget.allowUpdate) widget.screenWidget.keyPressed(keyCode, scanCode, shift);
        }
    }

    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (!widgets.isEmpty()) {
            WidgetInfo widget = widgets.getFirst();
            if (widget.allowUpdate) widget.screenWidget.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }
    }

    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        if (!widgets.isEmpty() && widgets.getFirst().status != WidgetStatus.NOT_OPENED) {
            BThackMatrix.push();
            WidgetInfo widget = widgets.getFirst();
            BThackMatrix.translate(0, 0, 3);
            BThackRender.drawVerticalGradientRect(0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), ColorUtils.TRANSPARENT, ColorUtils.fastRGBA(0, 0, 0, (int) (180 * (1 -widget.getEase(widget.backgroundAnimation)))));
            BThackMatrix.translate(0, (float) (widget.getEase(widget.widgetAnimation) * ((mc.getWindow().getScaledHeight() / 2d) + widget.screenWidget.getHeight())), 0);
            widget.screenWidget.render(context, mouseX, mouseY, partialTicks);
            BThackMatrix.pop();
        }
    }


    public static class WidgetInfo {
        private final int animTime;
        private final ScreenWidget screenWidget;
        private final Animation widgetAnimation;
        private final Animation backgroundAnimation;
        private WidgetStatus status = WidgetStatus.NOT_OPENED;
        private boolean allowUpdate = false;
        private boolean soundPlayed = false;

        public WidgetInfo(ScreenWidget screenWidget) {
            this.screenWidget = screenWidget;
            animTime = (int) (WIDGET_ANIMATION_TIME * screenWidget.animationSpeed);
            widgetAnimation = new Animation(Easing.BACK_IN_OUT, animTime);
            backgroundAnimation = new Animation(Easing.LINEAR, animTime);
        }

        public void setStatus(WidgetStatus status) {
            if (status == WidgetStatus.CLOSED) setAllowUpdate(false);
            widgetAnimation.reset();
            backgroundAnimation.reset();
            this.status = status;
        }

        public double getEase(Animation animation) {
            if (status == WidgetStatus.CLOSED) return animation.getEase();
            else return 1 - animation.getEase();
        }

        public void setAllowUpdate(boolean value) {
            screenWidget.buttons.forEach(button -> button.setAllowUpdate(value));
            allowUpdate = value;
        }
    }
    public enum WidgetStatus {
        NOT_OPENED,
        OPENED,
        CLOSED
    }
}
