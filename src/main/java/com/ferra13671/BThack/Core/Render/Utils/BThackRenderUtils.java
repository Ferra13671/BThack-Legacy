package com.ferra13671.BThack.Core.Render.Utils;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.RegionPos;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.util.function.Supplier;

public final class BThackRenderUtils implements Mc {

    public static final Matrix4f lastWorldMatrix = new Matrix4f();
    public static final Matrix4f lastProjMatrix = new Matrix4f();
    public static final Matrix4f lastModViewMatrix = new Matrix4f();

    public static Vec3d worldCordsToScreenCords(Vec3d pos) {
        Camera camera = mc.getEntityRenderDispatcher().camera;
        int displayHeight = mc.getWindow().getHeight();
        int[] viewport = new int[4];
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport);
        Vector3f target = new Vector3f();

        double deltaX = pos.x - camera.getPos().x;
        double deltaY = pos.y - camera.getPos().y;
        double deltaZ = pos.z - camera.getPos().z;

        Vector4f transformedCoordinates = new Vector4f((float) deltaX, (float) deltaY, (float) deltaZ, 1f).mul(lastWorldMatrix);
        Matrix4f matrixProj = new Matrix4f(lastProjMatrix);
        Matrix4f matrixModel = new Matrix4f(lastModViewMatrix);
        matrixProj.mul(matrixModel).project(transformedCoordinates.x(), transformedCoordinates.y(), transformedCoordinates.z(), viewport, target);

        return new Vec3d(target.x / mc.getWindow().getScaleFactor(), (displayHeight - target.y) / mc.getWindow().getScaleFactor(), target.z);
    }

    public static float[] hashCodeToRGBA(int hashCode) {
        return new float[]{
                (float) ColorHelper.Argb.getRed(hashCode) / 255.0F,
                (float) ColorHelper.Argb.getGreen(hashCode) / 255.0F,
                (float) ColorHelper.Argb.getBlue(hashCode) / 255.0F,
                (float) ColorHelper.Argb.getAlpha(hashCode) / 255.0F
        };
    }

    public static float[] hashCodeToRGB(int hashCode) {
        return new float[]{
                (float) ColorHelper.Argb.getRed(hashCode) / 255.0F,
                (float) ColorHelper.Argb.getGreen(hashCode) / 255.0F,
                (float) ColorHelper.Argb.getBlue(hashCode) / 255.0F
        };
    }

    public static Tessellator prepareToDraw(Supplier<ShaderProgram> shader) {
        Tessellator tessellator = Tessellator.getInstance();
        RenderSystem.setShader(shader);

        applyBlend();

        return tessellator;
    }

    public static Tessellator prepareToDraw() {
        Tessellator tessellator = Tessellator.getInstance();

        applyBlend();

        return tessellator;
    }

    public static void draw(BuiltBuffer buffer) {
        BufferRenderer.drawWithGlobalProgram(buffer);
        resetShader();
    }

    public static void drawNoReset(BuiltBuffer buffer) {
        BufferRenderer.drawWithGlobalProgram(buffer);
    }

    public static void resetShader() {
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
    }

    public static void applyBlend() {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
    }

    public static BlockPos getCameraBlockPos() {
        Camera camera = mc.getBlockEntityRenderDispatcher().camera;
        if(camera == null)
            return BlockPos.ORIGIN;

        return camera.getBlockPos();
    }

    public static Vec3d getCameraPos() {
        Camera camera = mc.getBlockEntityRenderDispatcher().camera;
        if(camera == null)
            return Vec3d.ZERO;

        return camera.getPos();
    }

    public static RegionPos getCameraRegion() {
        return RegionPos.of(getCameraBlockPos());
    }
}
