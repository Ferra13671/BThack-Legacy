package com.ferra13671.BThack.Core.Render;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.DeviceSystem;
import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigUtils;
import com.ferra13671.BThack.Core.Render.Box.BThackBoxRender;
import com.ferra13671.BThack.Core.Render.Drawers.*;
import com.ferra13671.BThack.Core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.Core.Render.Line.BThackLineRender;
import com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.Core.Render.Utils.ScissorStack;
import com.ferra13671.BThack.api.Shader.ShaderProgram;
import com.ferra13671.BThack.api.Shader.Shaders;
import com.ferra13671.BThack.api.Utils.RegionPos;
import com.ferra13671.BThack.impl.HudComponents.ArrayListComponent;
import com.ferra13671.TextureUtils.GlTex;
import com.ferra13671.TextureUtils.PathMode;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.render.*;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import com.ferra13671.BThack.api.Interfaces.Mc;
import org.joml.Vector3f;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils.*;

public final class BThackRender implements Mc {

    public static final VertexConsumerProvider.Immediate bufferSource = mc.getBufferBuilders().getEntityVertexConsumers();
    protected static final DrawContext guiGraphics = new DrawContext(mc, bufferSource);
    public static MatrixStack worldMatrixStack = new MatrixStack();
    public static final BThackBoxRender boxRender = new BThackBoxRender();
    public static final BThackLineRender lineRender = new BThackLineRender();
    public static Font defaultFont;

    public static FontRenderManager fontRenderManager;

    private static final ScissorStack scissorStack = new ScissorStack();

    private static boolean inited = false;

    public static void init() {
        if (inited) return;
        boxRender.init();
        RenderSystem.recordRenderCall(() -> Shaders.INSTANCE = new Shaders());
        if (DeviceSystem.getLaunchDevice() == DeviceSystem.LaunchDevice.PC) {
            try {
                defaultFont = FontUtils.createFontNoThrow(ConfigUtils.newInputStream("assets/bthack/fonts/defaultFont.ttf", PathMode.INSIDEJAR), 17);
                reloadFontRenderManager();
            } catch (Exception e) {
                BThack.error(e.getMessage());
            }
        }
        inited = true;
    }

    public static DrawContext getGuiGraphics() {
        return guiGraphics;
    }

    public static void reloadFontRenderManager() throws Exception {
        if (fontRenderManager != null)
            fontRenderManager.close();
        if (Client.clientInfo.getFont().equals("default")) fontRenderManager = new FontRenderManager(defaultFont);
        else if (Files.exists(Paths.get("BThack/Fonts/" + Client.clientInfo.getFont()))) fontRenderManager = new FontRenderManager(FontUtils.createFont(ConfigUtils.newInputStream("BThack/Fonts/" + Client.clientInfo.getFont(), PathMode.OUTSIDEJAR), 17));
        ArrayListComponent.updateSizes();
    }

    public static void trace(Vec3d vec3d, Matrix4f matrix, Vec3d start, float red, float green, float blue, float alpha, Vec3d regionVec) {
        Shaders.INSTANCE.POSITION.use();
        Shaders.INSTANCE.POSITION.setUniformValue("color", red, green, blue, alpha);

        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);

        Vec3d end = vec3d.subtract(new Vec3d(regionVec.x, regionVec.y, regionVec.z));
        bufferBuilder.vertex(matrix, (float)start.x, (float)start.y, (float)start.z);
        bufferBuilder.vertex(matrix, (float)end.x, (float)end.y, (float)end.z);
        draw(bufferBuilder.end());
    }

    public static void drawRect(float x1, float y1, float x2, float y2, int color) {
        Drawers.RECT.begin(color);
        Drawers.RECT.draw(x1, y1, x2, y2);
        Drawers.RECT.end();
    }

    public static void drawRect(float x1, float y1, float x2, float y2, int color, Matrix4f matrix4f) {
        Drawers.RECT.begin(color);
        Drawers.RECT.draw(x1, y1, x2, y2, matrix4f);
        Drawers.RECT.end();
    }

    public static void drawRoundedRect(float x1, float y1, float x2, float y2, float radius, int color) {
        BufferBuilder buffer = BThackRenderUtils.prepareToDraw(() -> Shaders.INSTANCE.ROUNDED_RECT.shader.getProgram()).begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

        Matrix4f matrix4f = BThackMatrix.peek().getPositionMatrix();
        Vector3f startPos = matrix4f.transformPosition(x1, y1, 0, new Vector3f());
        Vector3f endPos = matrix4f.transformPosition(x2, y2, 0, new Vector3f());

        Shaders.INSTANCE.ROUNDED_RECT.setUniformValue("resolution", (float) mc.getWindow().getWidth(), (float) mc.getWindow().getHeight());
        float scale = mc.getWindow().getScaledHeight() / (float) mc.getWindow().getHeight();
        Shaders.INSTANCE.ROUNDED_RECT.setUniformValue("position", startPos.x / scale, startPos.y / scale);
        Shaders.INSTANCE.ROUNDED_RECT.setUniformValue("size", (endPos.x - startPos.x) / scale, (endPos.y - startPos.y) / scale);
        Shaders.INSTANCE.ROUNDED_RECT.setUniformValue("radius", radius / scale);
        float[] rgba1 = ColorUtils.hashCodeToRGBA(color);
        Shaders.INSTANCE.ROUNDED_RECT.setUniformValue("color", rgba1[0], rgba1[1], rgba1[2], rgba1[3]);

        buffer.vertex(matrix4f, x1 - 1, y1 - 1, 0);
        buffer.vertex(matrix4f, x1 - 1, y2 + 1, 0);
        buffer.vertex(matrix4f, x2 + 1, y2 + 1, 0);
        buffer.vertex(matrix4f, x2 + 1, y1 - 1, 0);

        BThackRenderUtils.draw(buffer.end());
    }

    public static void drawRoundedRectOld(float x1, float y1, float x2, float y2, float radius, int color) {
        float[] rgba = ColorUtils.hashCodeToRGBA(color);
        Matrix4f matrix = BThackMatrix.peek().getPositionMatrix();
        BThackRenderUtils.resetShader();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        float[][] map = new float[][]{new float[]{x2 - radius, y2 - radius, radius}, new float[]{x2 - radius, y1 + radius, radius}, new float[]{x1 + radius, y1 + radius, radius}, new float[]{x1 + radius, y2 - radius, radius}};
        for (int i = 0; i < 4; i++) {
            float[] current = map[i];
            double rad = current[2];
            for (double r = i * 90d; r < (360 / 4d + i * 90d); r += (90 / 10f)) {
                float rad1 = (float) Math.toRadians(r);
                float sin = (float) (Math.sin(rad1) * rad);
                float cos = (float) (Math.cos(rad1) * rad);
                bufferBuilder.vertex(matrix, current[0] + sin, current[1] + cos, 0.0F).color(rgba[0], rgba[1], rgba[2], rgba[3]);
            }
            float rad1 = (float) Math.toRadians((360 / 4d + i * 90d));
            float sin = (float) (Math.sin(rad1) * rad);
            float cos = (float) (Math.cos(rad1) * rad);
            bufferBuilder.vertex(matrix, current[0] + sin, current[1] + cos, 0.0F).color(rgba[0], rgba[1], rgba[2], rgba[3]);
        }
        BThackRenderUtils.drawNoReset(bufferBuilder.end());
    }

    public static void drawRoundedRectWithOutline(float x1, float y1, float x2, float y2, float radius, int color, int outlineColor, float depth) {
        BufferBuilder buffer = BThackRenderUtils.prepareToDraw(() -> Shaders.INSTANCE.ROUNDED_RECT_WITH_OUTLINE.shader.getProgram()).begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

        Matrix4f matrix4f = BThackMatrix.peek().getPositionMatrix();
        Vector3f startPos = matrix4f.transformPosition(x1, y1, 0, new Vector3f());
        Vector3f endPos = matrix4f.transformPosition(x2, y2, 0, new Vector3f());

        Shaders.INSTANCE.ROUNDED_RECT_WITH_OUTLINE.setUniformValue("resolution", (float) mc.getWindow().getWidth(), (float) mc.getWindow().getHeight());
        float scale = mc.getWindow().getScaledHeight() / (float) mc.getWindow().getHeight();
        Shaders.INSTANCE.ROUNDED_RECT_WITH_OUTLINE.setUniformValue("position", startPos.x / scale, startPos.y / scale);
        Shaders.INSTANCE.ROUNDED_RECT_WITH_OUTLINE.setUniformValue("size", (endPos.x - startPos.x) / scale, (endPos.y - startPos.y) / scale);
        Shaders.INSTANCE.ROUNDED_RECT_WITH_OUTLINE.setUniformValue("radius", radius / scale);
        float[] rgba1 = ColorUtils.hashCodeToRGBA(color);
        float[] rgba2 = ColorUtils.hashCodeToRGBA(outlineColor);
        Shaders.INSTANCE.ROUNDED_RECT_WITH_OUTLINE.setUniformValue("color", rgba1[0], rgba1[1], rgba1[2], rgba1[3]);
        Shaders.INSTANCE.ROUNDED_RECT_WITH_OUTLINE.setUniformValue("outlineColor", rgba2[0], rgba2[1], rgba2[2], rgba2[3]);
        Shaders.INSTANCE.ROUNDED_RECT_WITH_OUTLINE.setUniformValue("depth", depth / scale);

        buffer.vertex(matrix4f, x1 - 1, y1 - 1, 0);
        buffer.vertex(matrix4f, x1 - 1, y2 + 1, 0);
        buffer.vertex(matrix4f, x2 + 1, y2 + 1, 0);
        buffer.vertex(matrix4f, x2 + 1, y1 - 1, 0);

        BThackRenderUtils.draw(buffer.end());
    }

    public static void drawGradientRoundedRectWithOutline(float x1, float y1, float x2, float y2, float radius, int color, int outlineColor1, int outlineColor2, float depth, float scale, float speed) {
        Shaders.INSTANCE.XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE.use();
        BufferBuilder buffer = BThackRenderUtils.prepareToDraw().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

        Matrix4f matrix4f = BThackMatrix.peek().getPositionMatrix();
        Vector3f startPos = matrix4f.transformPosition(x1, y1, 0, new Vector3f());
        Vector3f endPos = matrix4f.transformPosition(x2, y2, 0, new Vector3f());

        Shaders.INSTANCE.XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE.setUniformValue("resolution", (float) mc.getWindow().getWidth(), (float) mc.getWindow().getHeight());
        float _scale = mc.getWindow().getScaledHeight() / (float) mc.getWindow().getHeight();
        Shaders.INSTANCE.XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE.setUniformValue("position", startPos.x / _scale, startPos.y / _scale);
        Shaders.INSTANCE.XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE.setUniformValue("size", (endPos.x - startPos.x) / _scale, (endPos.y - startPos.y) / _scale);
        Shaders.INSTANCE.XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE.setUniformValue("radius", radius / _scale);
        float[] rgba1 = ColorUtils.hashCodeToRGBA(color);
        float[] rgba2 = ColorUtils.hashCodeToRGBA(outlineColor1);
        float[] rgba3 = ColorUtils.hashCodeToRGBA(outlineColor2);
        Shaders.INSTANCE.XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE.setUniformValue("color", rgba1[0], rgba1[1], rgba1[2], rgba1[3]);
        Shaders.INSTANCE.XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE.setUniformValue("outlineColor1", rgba2[0], rgba2[1], rgba2[2], rgba2[3]);
        Shaders.INSTANCE.XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE.setUniformValue("outlineColor2", rgba3[0], rgba3[1], rgba3[2], rgba3[3]);
        Shaders.INSTANCE.XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE.setUniformValue("depth", depth / _scale);
        Shaders.INSTANCE.XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE.setUniformValue("scale", scale);
        Shaders.INSTANCE.XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE.setUniformValue("speed", speed);

        buffer.vertex(matrix4f, x1 - 1, y1 - 1, 0);
        buffer.vertex(matrix4f, x1 - 1, y2 + 1, 0);
        buffer.vertex(matrix4f, x2 + 1, y2 + 1, 0);
        buffer.vertex(matrix4f, x2 + 1, y1 - 1, 0);

        BThackRenderUtils.draw(buffer.end());
    }

    public static void drawHudPlate(float x1, float y1, float x2, float y2) {
        ModuleList.HUD.hudStyle.draw(x1, y1, x2, y2);
    }

    public static void drawLine(float x1, float y1, float x2, float y2, float width, int color) {
        Matrix4f matrix4f = BThackMatrix.peek().getPositionMatrix();

        width = width / 2;

        Drawers.RECT.begin(color);
        Drawers.RECT.vertex(matrix4f, x2 + width, y2 + width, 0);
        Drawers.RECT.vertex(matrix4f, x2 + width, y2 - width, 0);
        Drawers.RECT.vertex(matrix4f, x1 - width, y1 - width, 0);
        Drawers.RECT.vertex(matrix4f, x1 - width, y1 + width, 0);
        Drawers.RECT.end();
    }

    public static void drawVerticalGradientRect(float x1, float y1, float x2, float y2, int startColor, int endColor) {
        Drawers.GRADIENT_RECT.begin();
        Drawers.GRADIENT_RECT.draw(x1, y1, x2, y2, startColor, endColor, GradientRectDrawer.GradientMode.VERTICAL);
        Drawers.GRADIENT_RECT.end();
    }

    public static void drawHorizontalGradientRect(float x1, float y1, float x2, float y2, int startColor, int endColor) {
        Drawers.GRADIENT_RECT.begin();
        Drawers.GRADIENT_RECT.draw(x1, y1, x2, y2, startColor, endColor, GradientRectDrawer.GradientMode.HORIZONTAL);
        Drawers.GRADIENT_RECT.end();
    }

    public static void draw4ColorRect(float x1, float y1, float x2, float y2, int x1y1Color, int x2y1Color, int x1y2Color, int x2y2Color) {
        Drawers.CUSTOM_COLORS_RECT.begin();
        Drawers.CUSTOM_COLORS_RECT.draw(x1, y1, x2, y2, x1y1Color, x2y1Color, x1y2Color, x2y2Color);
        Drawers.CUSTOM_COLORS_RECT.end();
    }

    public static void drawOutlineRect(float x1, float y1, float x2, float y2, float depth, int color) {
        Drawers.RECT.begin(color);
        Drawers.RECT.draw(x1,y1, x1 + depth, y2); //left
        Drawers.RECT.draw(x1 + depth, y2 - depth, x2, y2); //down
        Drawers.RECT.draw(x2, y2 - depth, x2 - depth, y1); //right
        Drawers.RECT.draw(x1 + depth, y1, x2 - depth, y1 + depth); //up
        Drawers.RECT.end();
    }

    public static void drawVerticalGradientOutlineRect(float x1, float y1, float x2, float y2, float depth, int upColor, int downColor) {
        BThackRender.drawRect(x1 + depth, y1, x2 - depth, y1 + depth, upColor); //up
        BThackRender.drawRect(x1 + depth, y2 - depth, x2, y2, downColor); //down
        Drawers.GRADIENT_RECT.begin();
        Drawers.GRADIENT_RECT.draw(x1,y1, x1 + depth, y2, upColor, downColor, GradientRectDrawer.GradientMode.VERTICAL); //left
        Drawers.GRADIENT_RECT.draw(x2 - depth, y1, x2, y2 - depth, upColor, downColor, GradientRectDrawer.GradientMode.VERTICAL); //down
        Drawers.GRADIENT_RECT.end();
    }

    public static void drawShaderOutlineRect(ShaderProgram shaderProgram, float x1, float y1, float x2, float y2, float depth) {
        Drawers.SHADER_DRAWER.begin(shaderProgram);
        Drawers.SHADER_DRAWER.draw(x1,y1, x1 + depth, y2); //left
        Drawers.SHADER_DRAWER.draw(x1 + depth, y2 - depth, x2, y2); //down
        Drawers.SHADER_DRAWER.draw(x2, y2 - depth, x2 - depth, y1); //right
        Drawers.SHADER_DRAWER.draw(x1 + depth, y1, x2 - depth, y1 + depth); //up
        Drawers.SHADER_DRAWER.end();
    }

    public static void drawSquare(float x1, float y1, float size, int color) {
        drawRect(x1 - size, y1 - size, x1 + size, y1 + size, color);
    }


    public static void drawTriangle(float x, float y, float size, float theta, int color) {
        Matrix4f matrix4f = BThackMatrix.peek().getPositionMatrix();

        double radians = Math.toRadians(theta);

        float xA = -size;
        double newXA = xA * Math.cos(radians) + size * Math.sin(radians);
        double newYA = size * Math.cos(radians) - xA * Math.sin(radians);

        float xB = 0;
        float yB = -(size * 2);
        double newXB = xB * Math.cos(radians) + yB * Math.sin(radians);
        double newYB = yB * Math.cos(radians) - xB * Math.sin(radians);

        double newXC = size * Math.cos(radians) + size * Math.sin(radians);
        double newYC = size * Math.cos(radians) - size * Math.sin(radians);

        Drawers.RECT.begin(color);
        Drawers.RECT.vertex(matrix4f, (float)(x + newXB), (float)(y + newYB), 0);
        Drawers.RECT.vertex(matrix4f, (float)(x + newXA), (float)(y + newYA), 0);
        Drawers.RECT.vertex(matrix4f, (float)(x + newXC), (float)(y + newYC), 0);
        Drawers.RECT.vertex(matrix4f, (float)(x + newXB), (float)(y + newYB), 0);
        Drawers.RECT.end();
    }

    public static void drawString(String text, float x, float y, int color, boolean shadow, FontRenderManager.DrawMode drawMode) {

        if (text == null || text.isEmpty()) return;

        if (ModuleList.customFont == null || !ModuleList.customFont.isEnabled()) {
            BThackMatrix.push();
            float size = drawMode.getSize();
            if (size != 1f)
                BThackMatrix.scale(size, size, size);
            mc.textRenderer.draw(text, x * (1 / size), y * (1 / size), color, shadow, BThackMatrix.peek().getPositionMatrix(), bufferSource, TextRenderer.TextLayerType.NORMAL, 0, 15728880, mc.textRenderer.isRightToLeft());
            guiGraphics.draw();
            resetShader();
            BThackMatrix.pop();
        } else {
            RenderSystem.enableDepthTest();
            fontRenderManager.draw(text, x, y, color, shadow, drawMode);
        }
    }

    public static void drawString(String text, float x1, float y1, int color, boolean shadow) {
        drawString(text, x1, y1, color, shadow, FontRenderManager.DrawMode.NORMAL);
    }

    public static void drawString(String text, float x1, float y1, int color) {
        drawString(text, x1, y1, color, true);
    }

    public static void drawCenteredString(String text, float x1, float y1, int color) {
        drawCenteredString(text, x1, y1, color, FontRenderManager.DrawMode.NORMAL);
    }

    public static void drawCenteredString(String text, float x1, float y1, int color, FontRenderManager.DrawMode drawMode) {
        drawString(text, (x1 - (FontUtils.getTextWidth(text, drawMode) / (drawMode == FontRenderManager.DrawMode.NORMAL || drawMode == FontRenderManager.DrawMode.NORMAL_BOLD || (ModuleList.customFont != null && ModuleList.customFont.isEnabled()) ? 2f : 2.85714f))), y1, color, true, drawMode);
    }

    /**
     * This is shit, don't use it please, use another renderer on my texture system.
     */
    @Deprecated
    public static void drawTextureRect(Identifier texture, float x1, float y1, float x2, float y2) {
        Drawers.TEXTURE_RECT.begin(texture);
        Drawers.TEXTURE_RECT.draw(x1, y1, x2, y2);
        Drawers.TEXTURE_RECT.end();
    }

    public static void drawTextureRect(GlTex texture, float x1, float y1, float x2, float y2) {
        Drawers.TEXTURE_RECT.begin(texture);
        Drawers.TEXTURE_RECT.draw(x1, y1, x2, y2);
        Drawers.TEXTURE_RECT.end();
    }

    public static void drawShader(ShaderProgram shaderProgram, float x1, float y1, float x2, float y2) {
        Drawers.SHADER_DRAWER.begin(shaderProgram);
        Drawers.SHADER_DRAWER.draw(x1, y1, x2, y2);
        Drawers.SHADER_DRAWER.end();
    }

    public static void drawItem(ItemStack stack, int x, int y, String amountText, boolean onSlot) {
        drawItem(stack, x, y, amountText, onSlot, 1);
    }

    public static void drawItem(ItemStack stack, int x, int y, String amountText, boolean onSlot, float size) {
        BThackMatrix.push();
        BThackMatrix.scale(size, size, 1);
        guiGraphics.drawItem(stack, x, y);
        if (onSlot)
            guiGraphics.drawItemInSlot(mc.textRenderer, stack, x, y, amountText);
        BThackMatrix.pop();
    }

    public static void enableScissor(int x, int y, int width, int height) {
        setScissor(scissorStack.push(new ScreenRect(x, y, width, height)));
    }

    public static void disableScissor() {
        setScissor(scissorStack.pop());
    }

    private static void setScissor(ScreenRect rect) {
        if (rect != null) {
            Window window = mc.getWindow();
            int i = window.getFramebufferHeight();
            double d = window.getScaleFactor();
            double e = (double) rect.getLeft() * d;
            double f = (double) i - (double) rect.getBottom() * d;
            double g = (double) rect.width() * d;
            double h = (double) rect.height() * d;
            RenderSystem.enableScissor((int) e, (int) f, Math.max(0, (int) g), Math.max(0, (int) h));
        } else {
            RenderSystem.disableScissor();
        }
    }

    public static void applyRegionalRenderOffset(MatrixStack matrixStack) {
        applyRegionalRenderOffset(matrixStack, getCameraRegion());
    }

    public static void applyRegionalRenderOffset(MatrixStack matrixStack, RegionPos region) {
        Vec3d offset = region.toVec3d().subtract(getCameraPos());
        matrixStack.translate(offset.x, offset.y, offset.z);
    }
}
