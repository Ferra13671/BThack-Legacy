package com.ferra13671.BThack.Core.Render.Drawers;

import com.ferra13671.BThack.Core.Render.BThackMatrix;
import com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import org.joml.Matrix4f;

import static com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils.prepareToDraw;
import static com.ferra13671.BThack.Core.Render.Utils.ColorUtils.hashCodeToRGBA;

public class GradientRectDrawer extends Drawer {

    public void begin() {
        buffer = prepareToDraw(GameRenderer::getPositionColorProgram).begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
    }

    public void draw(float x1, float y1, float x2, float y2, int startColor, int endColor, Matrix4f matrix4f, GradientMode gradientMode) {
        float[] startC = hashCodeToRGBA(startColor);
        float[] endC = hashCodeToRGBA(endColor);

        if (gradientMode == GradientMode.VERTICAL) {
            buffer.vertex(matrix4f, x1, y1, 0).color(startC[0], startC[1], startC[2], startC[3]);
            buffer.vertex(matrix4f, x1, y2, 0).color(endC[0], endC[1], endC[2], endC[3]);
            buffer.vertex(matrix4f, x2, y2, 0).color(endC[0], endC[1], endC[2], endC[3]);
            buffer.vertex(matrix4f, x2, y1, 0).color(startC[0], startC[1], startC[2], startC[3]);
        } else {
            buffer.vertex(matrix4f, x1, y1, 0).color(startC[0], startC[1], startC[2], startC[3]);
            buffer.vertex(matrix4f, x1, y2, 0).color(startC[0], startC[1], startC[2], startC[3]);
            buffer.vertex(matrix4f, x2, y2, 0).color(endC[0], endC[1], endC[2], endC[3]);
            buffer.vertex(matrix4f,  x2, y1, 0).color(endC[0], endC[1], endC[2], endC[3]);
        }
    }

    public void draw(float x1, float y1, float x2, float y2, int startColor, int endColor, GradientMode gradientMode) {
        draw(x1, y1, x2, y2, startColor, endColor, BThackMatrix.peek().getPositionMatrix(), gradientMode);
    }

    public void end() {
        BThackRenderUtils.draw(buffer.end());
        buffer = null;
    }


    public enum GradientMode {
        VERTICAL,
        HORIZONTAL
    }
}
