package com.ferra13671.BThack.core.Render;

import net.minecraft.client.util.math.MatrixStack;
import org.joml.Quaternionf;

import static com.ferra13671.BThack.core.Render.BThackRender.guiGraphics;

public class BThackMatrix {

    public static void push() {
        guiGraphics.getMatrices().push();
    }

    public static void pop() {
        guiGraphics.getMatrices().pop();
    }

    public static MatrixStack.Entry peek() {
        return guiGraphics.getMatrices().peek();
    }

    public static void translate(float x, float y, float z) {
        guiGraphics.getMatrices().translate(x, y, z);
    }

    public static void scale(float x, float y, float z) {
        guiGraphics.getMatrices().scale(x, y, z);
    }

    public static void multiply(Quaternionf quaternion) {
        guiGraphics.getMatrices().multiply(quaternion);
    }
}
