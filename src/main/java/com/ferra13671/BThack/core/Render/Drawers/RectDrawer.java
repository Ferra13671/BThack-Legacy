package com.ferra13671.BThack.core.Render.Drawers;

import com.ferra13671.BThack.core.Render.BThackMatrix;
import com.ferra13671.BThack.core.Render.Utils.BThackRenderUtils;
import com.ferra13671.BThack.shaders.Shaders;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import org.joml.Matrix4f;

import static com.ferra13671.BThack.core.Render.Utils.BThackRenderUtils.prepareToDraw;
import static com.ferra13671.BThack.core.Render.Utils.ColorUtils.hashCodeToRGBA;

public class RectDrawer extends Drawer {

    public void begin(int color) {
        float[] c = hashCodeToRGBA(color);

        Shaders.INSTANCE.POSITION.use();
        Shaders.INSTANCE.POSITION.setUniformValue("color", c[0], c[1], c[2], c[3]);

        Tessellator tessellator = prepareToDraw();

        buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
    }

    public void beginBuffer(Tessellator tessellator) {
        buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
    }

    public void draw(float x1, float y1, float x2, float y2, Matrix4f matrix4f) {
        buffer.vertex(matrix4f, x1, y1, 0);
        buffer.vertex(matrix4f, x1, y2, 0);
        buffer.vertex(matrix4f, x2, y2, 0);
        buffer.vertex(matrix4f, x2, y1, 0);
    }

    public void draw(float x1, float y1, float x2, float y2) {
        draw(x1, y1, x2, y2, BThackMatrix.peek().getPositionMatrix());
    }

    public void vertex(Matrix4f matrix4f, float x, float y, float z) {
        buffer.vertex(matrix4f, x, y, z);
    }

    public void end() {
        BThackRenderUtils.draw(buffer.end());
        Shaders.INSTANCE.POSITION.release();
        buffer = null;
    }

    public void endNoReset() {
        BThackRenderUtils.drawNoReset(buffer.end());
        buffer = null;
    }
}
