package com.ferra13671.BThack.Core.Render.Box;

import net.minecraft.util.math.Box;

public class RenderBox {
    public final float boxRed;
    public final float boxGreen;
    public final float boxBlue;
    public final float boxAlpha;

    public final float linesRed;
    public final float linesGreen;
    public final float linesBlue;
    public final float linesAlpha;

    public final Box box;

    public RenderBox(Box box, float linesRed, float linesGreen, float linesBlue, float linesAlpha, float boxRed, float boxGreen, float boxBlue, float boxAlpha) {
        this.box = box;

        this.linesRed = linesRed;
        this.linesGreen = linesGreen;
        this.linesBlue = linesBlue;
        this.linesAlpha = linesAlpha;

        this.boxRed = boxRed;
        this.boxGreen = boxGreen;
        this.boxBlue = boxBlue;
        this.boxAlpha = boxAlpha;
    }
}
