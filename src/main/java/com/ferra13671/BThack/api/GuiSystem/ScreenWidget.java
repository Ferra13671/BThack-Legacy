package com.ferra13671.BThack.api.GuiSystem;

import com.ferra13671.BTbot.api.Utils.Generate.NumberGenerator;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.GuiSystem.Screen.BThackScreen;
import net.minecraft.text.Text;

public class ScreenWidget extends BThackScreen {
    public final float animationSpeed;
    private final float width;
    private final float height;

    protected BThackScreen parent;
    protected float xLeft = 0;
    protected float yUp = 0;
    protected float xRight = 0;
    protected float yDown = 0;

    public boolean needClose = false;

    public ScreenWidget(float width, float height, float animationSpeed) {
        super(Text.literal("Widget-" + NumberGenerator.generateInt(10000, 99999)));
        this.width = width;
        this.height = height;
        this.animationSpeed = animationSpeed;
    }

    public void setParent(BThackScreen parent) {
        this.parent = parent;
    }

    @Override
    public void init() {
        super.init();
        xLeft = (mc.getWindow().getScaledWidth() / 2f) - (width / 2);
        yUp = (mc.getWindow().getScaledHeight() / 2f) - (height / 2);
        xRight = (mc.getWindow().getScaledWidth() / 2f) + (width / 2);
        yDown = (mc.getWindow().getScaledHeight() / 2f) + (height / 2);
    }

    @Override
    public void close() {
        needClose = true;
    }

    @Override
    public final boolean shouldCloseOnEsc() {
        return false;
    }

    protected void drawPlate() {
        BThackRender.drawRect(xLeft, yUp, xRight, yDown, ColorUtils.fastRGBA(0, 0, 0, 150));
        BThackRender.drawVerticalGradientOutlineRect(xLeft, yUp, xRight, yDown, 1.5f, ColorUtils.WHITE, ColorUtils.fastRGBA(150, 150, 150, 255));
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
