package com.ferra13671.BThack.Core.Render.Drawers;

import com.ferra13671.BThack.Core.Render.BThackMatrix;
import com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import org.joml.Matrix4f;

import static com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils.prepareToDraw;
import static com.ferra13671.BThack.Core.Render.Utils.ColorUtils.hashCodeToRGBA;

public class CustomColorsRectDrawer extends Drawer {

    public void begin() {
        buffer = prepareToDraw(GameRenderer::getPositionColorProgram).begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
    }

    public void draw(float x1, float y1, float x2, float y2, int x1y1Color, int x2y1Color, int x1y2Color, int x2y2Color, Matrix4f matrix4f) {
        float[] x1y1C = hashCodeToRGBA(x1y1Color);
        float[] x2y1C = hashCodeToRGBA(x2y1Color);
        float[] x1y2C = hashCodeToRGBA(x1y2Color);
        float[] x2y2C = hashCodeToRGBA(x2y2Color);

        buffer.vertex(matrix4f, x1, y1, 0).color(x1y1C[0], x1y1C[1], x1y1C[2], x1y1C[3]);
        buffer.vertex(matrix4f, x1, y2, 0).color(x1y2C[0], x1y2C[1], x1y2C[2], x1y2C[3]);
        buffer.vertex(matrix4f, x2, y2, 0).color(x2y2C[0], x2y2C[1], x2y2C[2], x2y2C[3]);
        buffer.vertex(matrix4f, x2, y1, 0).color(x2y1C[0], x2y1C[1], x2y1C[2], x2y1C[3]);
    }

    public void draw(float x1, float y1, float x2, float y2, int x1y1Color, int x2y1Color, int x1y2Color, int x2y2Color) {
        draw(x1, y1, x2, y2, x1y1Color, x2y1Color, x1y2Color, x2y2Color, BThackMatrix.peek().getPositionMatrix());
    }

    public void end() {
        BThackRenderUtils.draw(buffer.end());
        buffer = null;
    }
}
