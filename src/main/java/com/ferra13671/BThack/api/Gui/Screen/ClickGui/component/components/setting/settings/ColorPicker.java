package com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings;

import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Drawers.Drawers;
import com.ferra13671.BThack.core.Render.Drawers.GradientRectDrawer;
import com.ferra13671.BThack.core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.core.Render.Utils.BThackRenderUtils;
import com.ferra13671.BThack.core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Shaders.Shaders;
import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;
import net.minecraft.client.render.Tessellator;

import java.awt.*;

public class ColorPicker extends AbstractSetting<ColorSetting> {
    private final ColorObject colorRect = new ColorObject() {
        @Override
        protected float getStartX() {
            return getX() + 5;
        }

        @Override
        protected float getStartY() {
            return getY() + 15;
        }

        @Override
        protected float getEndX() {
            return getX() + 5 + getWidth();
        }

        @Override
        protected float getEndY() {
            return getY() + 15 + getHeight();
        }

        @Override
        protected float getWidth() {
            return 51;
        }

        @Override
        protected float getHeight() {
            return 51;
        }
    };
    private final ColorObject hueRect = new ColorObject() {
        @Override
        protected float getStartX() {
            return getX() + 60;
        }

        @Override
        protected float getStartY() {
            return getY() + 15;
        }

        @Override
        protected float getEndX() {
            return getX() + 60 + getWidth();
        }

        @Override
        protected float getEndY() {
            return getY() + 15 + getHeight();
        }

        @Override
        protected float getWidth() {
            return 10;
        }

        @Override
        protected float getHeight() {
            return 51;
        }
    };
    private final ColorObject alphaRect = new ColorObject() {
        @Override
        protected float getStartX() {
            return getX() + 74;
        }

        @Override
        protected float getStartY() {
            return getY() + 15;
        }

        @Override
        protected float getEndX() {
            return getX() + 74 + getWidth();
        }

        @Override
        protected float getEndY() {
            return getY() + 15 + getHeight();
        }

        @Override
        protected float getWidth() {
            return 10;
        }

        @Override
        protected float getHeight() {
            return 51;
        }
    };

    private Color rgbColor;
    private float[] hsbColor;
    private float hue;
    private float alpha;

    private boolean opened = false;
    private final Animation animation = new Animation(Easing.CIRC_OUT, 300);

    public ColorPicker(ColorSetting setting, ModuleButton button , int offset, Module module) {
        super(offset, button, module, setting);

        updateColors();
    }

    private void updateColors() {
        rgbColor = setting.getValue();
        alpha = rgbColor.getAlpha() / 255f;
        hsbColor = Color.RGBtoHSB(rgbColor.getRed(), rgbColor.getGreen(), rgbColor.getBlue(), null);
        hue = hsbColor[0];
        colorRect.crosshairX = hsbColor[1] * colorRect.getWidth();
        colorRect.crosshairY = (1 - hsbColor[2]) * colorRect.getHeight();
        hueRect.crosshairY = hue * hueRect.getHeight();
        alphaRect.crosshairY = (1 - alpha) * alphaRect.getHeight();
    }

    @Override
    public int getHeight() {
        return opened ? (int) (80 * animation.getEase()) : Constants.CLICKGUI_BUTTON_HEIGHT + (int) (66 * (1 - animation.getEase()));
    }

    @Override
    public void renderComponent() {
        super.renderComponent();

        BThackRender.drawRect(getX(), getY(), getX() + Constants.CLICKGUI_FRAME_WIDTH, getY() + getHeight(), ClickGui.BACKGROUND_COLOR);
        if (opened || animation.getEase() < 1) {
            BThackRender.enableScissor(ClickGui.applyGuiScale(getX()), ClickGui.applyGuiScale(getY()), ClickGui.applyGuiScale(Constants.CLICKGUI_FRAME_WIDTH), ClickGui.applyGuiScale(getHeight()));
            Drawers.GRADIENT_RECT.begin();
            Drawers.GRADIENT_RECT.draw(colorRect.getStartX(), colorRect.getStartY(), colorRect.getEndX(), colorRect.getEndY(), ColorUtils.WHITE, new Color(Color.HSBtoRGB(hue, 1f, 1f)).hashCode(), GradientRectDrawer.GradientMode.HORIZONTAL);
            Drawers.GRADIENT_RECT.draw(colorRect.getStartX(), colorRect.getStartY(), colorRect.getEndX(), colorRect.getEndY(), ColorUtils.TRANSPARENT, ColorUtils.BLACK, GradientRectDrawer.GradientMode.VERTICAL);

            //Alpha Rect
            if (!setting.isBlockedAlpha())
                Drawers.GRADIENT_RECT.draw(alphaRect.getStartX(), alphaRect.getStartY(), alphaRect.getEndX(), alphaRect.getEndY(), new Color(rgbColor.getRed(), rgbColor.getGreen(), rgbColor.getBlue()).hashCode(), ColorUtils.WHITE, GradientRectDrawer.GradientMode.VERTICAL);
            Drawers.GRADIENT_RECT.end();

            //Hue Rect
            Tessellator tessellator = BThackRenderUtils.prepareToDraw();
            Shaders.INSTANCE.POSITION.use();
            float hue = 0;
            float hueFactor = 1 / 26f;
            float hueY = 0;
            for (int i = 0; i < 26; i++) {
                Color hueColor = Color.getHSBColor(hue, 1f, 1f);
                Drawers.RECT.beginBuffer(tessellator);
                Shaders.INSTANCE.POSITION.setUniformValue("color", hueColor.getRed() / 255f, hueColor.getGreen() / 255f, hueColor.getBlue() / 255f, hueColor.getAlpha() / 255f);
                Drawers.RECT.draw(hueRect.getStartX(), hueRect.getStartY() + hueY, hueRect.getEndX(), hueRect.getStartY() + (hueY + 2));
                Drawers.RECT.endNoReset();
                hue += hueFactor;
                hueY += 2;
            }
            Shaders.INSTANCE.POSITION.release();

            drawColorCrosshair();
            drawHueCrosshair();
            if (!setting.isBlockedAlpha())
                drawAlphaCrosshair();

            BThackRender.drawString("R:" + rgbColor.getRed() + " G:" + rgbColor.getGreen() + " B:" + rgbColor.getBlue() + " A:" + rgbColor.getAlpha(), getX() + 2, colorRect.getEndY() + 4, -1, true, FontRenderManager.DrawMode.SMALL);
            BThackRender.disableScissor();
        }
        BThackRender.drawString(setting.getName(), getX() + 2, getY() + 2, ColorUtils.WHITE);
        BThackRender.drawRect(getX() + Constants.CLICKGUI_FRAME_WIDTH - 12, getY() + 2, getX() + Constants.CLICKGUI_FRAME_WIDTH - 2, getY() + 12, rgbColor.hashCode());
    }

    private void drawColorCrosshair() {
        BThackRender.drawRect(colorRect.getStartX() + (colorRect.crosshairX - 1.5f), colorRect.getStartY() + (colorRect.crosshairY - 1.5f), colorRect.getStartX() + (colorRect.crosshairX + 1.5f), colorRect.getStartY() + (colorRect.crosshairY + 1.5f), ColorUtils.WHITE);
        BThackRender.drawOutlineRect(colorRect.getStartX() + (colorRect.crosshairX - 1.5f), colorRect.getStartY() + (colorRect.crosshairY - 1.5f), colorRect.getStartX() + (colorRect.crosshairX + 1.5f), colorRect.getStartY() + (colorRect.crosshairY + 1.5f), 1, ColorUtils.fastRGBA(0, 0, 0, 100));
    }

    private void drawHueCrosshair() {
        BThackRender.drawRect(hueRect.getStartX() - 1, hueRect.getStartY() + hueRect.crosshairY - 1, hueRect.getEndX() + 1, hueRect.getStartY() + hueRect.crosshairY + 2, ColorUtils.WHITE);
        BThackRender.drawOutlineRect(hueRect.getStartX() - 1, hueRect.getStartY() + hueRect.crosshairY - 1, hueRect.getEndX() + 1, hueRect.getStartY() + hueRect.crosshairY + 2, 1, ColorUtils.fastRGBA(0, 0, 0, 100));
    }

    private void drawAlphaCrosshair() {
        BThackRender.drawRect(alphaRect.getStartX() - 1, alphaRect.getStartY() + alphaRect.crosshairY - 1, alphaRect.getEndX() + 1, alphaRect.getStartY() + alphaRect.crosshairY + 2, ColorUtils.WHITE);
        BThackRender.drawOutlineRect(alphaRect.getStartX() - 1, alphaRect.getStartY() + alphaRect.crosshairY - 1, alphaRect.getEndX() + 1, alphaRect.getStartY() + alphaRect.crosshairY + 2, 1, ColorUtils.fastRGBA(0, 0, 0, 100));
    }

    @Override
    public boolean updateComponent(int mouseX, int mouseY) {
        if (!getVisible()) return true;
        if (animation.getEase() < 1) parent.parent.refresh();

        if (!setting.getValue().equals(rgbColor)) updateColors();

        if (colorRect.hovered) {
            float width = ClickGui.applyGuiScale(colorRect.getWidth());
            float height = ClickGui.applyGuiScale(colorRect.getHeight());
            float xF = MathUtils.applyRange(mouseX - ClickGui.applyGuiScale(colorRect.getStartX()), 0, width);
            float yF = MathUtils.applyRange(mouseY - ClickGui.applyGuiScale(colorRect.getStartY()), 0, height);
            xF = (xF / width) * colorRect.getWidth();
            yF = (yF / height) * colorRect.getHeight();

            Color color = new Color(Color.HSBtoRGB(hue, xF / colorRect.getWidth(), 1f - (yF / colorRect.getHeight())));
            setting.setValue(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * 255)));
            updateColors();
        }
        if (hueRect.hovered) {
            float height = ClickGui.applyGuiScale(hueRect.getHeight());
            float yF = MathUtils.applyRange(mouseY - ClickGui.applyGuiScale(hueRect.getStartY()), 0, height);
            yF = (yF / height) * hueRect.getHeight();

            hue = yF / colorRect.getHeight();
            Color color = new Color(Color.HSBtoRGB(hue, hsbColor[1], hsbColor[2]));
            setting.setValue(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * 255)));
            updateColors();
        }
        if (!setting.isBlockedAlpha()) {
            if (alphaRect.hovered) {
                float height = ClickGui.applyGuiScale(alphaRect.getHeight());
                float yF = MathUtils.applyRange(mouseY - ClickGui.applyGuiScale(alphaRect.getStartY()), 0, height);
                yF = (yF / height) * alphaRect.getHeight();

                Color color = new Color(Color.HSBtoRGB(hue, hsbColor[1], hsbColor[2]));
                setting.setValue(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) ((1 - (yF / alphaRect.getHeight())) * 255)));
                updateColors();
            }
        }

        return false;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (!this.getVisible()) return false;

        if (isMouseOnButton(mouseX, mouseY)) {
            opened = !opened;
            parent.parent.refresh();
            animation.reset();
            return false;
        }
        if (opened && button == 0) {
            if (colorRect.isMouseOnObject(mouseX, mouseY)) {
                colorRect.hovered = true;
                return false;
            }
            if (hueRect.isMouseOnObject(mouseX, mouseY)) {
                hueRect.hovered = true;
                return false;
            }
            if (!setting.isBlockedAlpha()) {
                if (alphaRect.isMouseOnObject(mouseX, mouseY)) {
                    alphaRect.hovered = true;
                    return false;
                }
            }
        }

        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        colorRect.hovered = false;
        hueRect.hovered = false;
        alphaRect.hovered = false;
    }


    private static abstract class ColorObject {
        protected boolean hovered = false;
        protected float crosshairX = 0;
        protected float crosshairY = 0;

        protected abstract float getStartX();

        protected abstract float getStartY();

        protected abstract float getEndX();

        protected abstract float getEndY();

        protected abstract float getWidth();

        protected abstract float getHeight();

        protected boolean isMouseOnObject(double mouseX, double mouseY) {
            return mouseX > ClickGui.applyGuiScale(getStartX()) && mouseX < ClickGui.applyGuiScale(getEndX()) &&
                    mouseY > ClickGui.applyGuiScale(getStartY()) && mouseY < ClickGui.applyGuiScale(getEndY());
        }
    }
}
