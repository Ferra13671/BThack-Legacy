package com.ferra13671.BThack.core.Render.Utils;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.Modules.StrafeUtils;
import com.ferra13671.BThack.api.Utils.RegionPos;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.*;
import net.minecraft.util.math.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.util.function.Supplier;

public final class BThackRenderUtils implements Mc {
    public static final Matrix4f lastWorldMatrix = new Matrix4f();
    public static final Matrix4f lastProjMatrix = new Matrix4f();
    public static final Matrix4f lastModViewMatrix = new Matrix4f();

    public static void updateMatrixData() {
        lastProjMatrix.set(RenderSystem.getProjectionMatrix());
        lastModViewMatrix.set(RenderSystem.getModelViewMatrix());
    }

    public static float[] worldPosToScreenXY(Vec3d pos, boolean allowBehind) {
        Vec3d vector = worldPosToScreenPos(pos);
        if (!allowBehind)
            if (isBehind(vector)) return null;
        return new float[]{(float) vector.getX(), (float) vector.getY()};
    }

    public static Vec3d worldPosToScreenPos(Vec3d pos) {
        Camera camera = mc.getEntityRenderDispatcher().camera;
        int displayHeight = mc.getWindow().getHeight();
        int[] viewport = new int[4];
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport);
        Vector3f target = new Vector3f();

        double deltaX = pos.x - camera.getPos().x;
        double deltaY = pos.y - camera.getPos().y;
        double deltaZ = pos.z - camera.getPos().z;

        /*
            If the distance from the camera to the position is greater than +-700, the check if the coordinates are behind the player will be broken.
            To avoid this problem, the coordinates will be converted to a closer distance so that the method returns the correct values.
         */
        if (Math.abs(deltaX) > 700 || Math.abs(deltaY) > 700 || Math.abs(deltaZ) > 700) {
            float[] rots = RotateUtils.rotations(pos);
            double[] newPoses = StrafeUtils.getMoveFactors(rots[0]);
            rots[1] = (float) Math.toRadians(rots[1]);
            newPoses = new double[]{newPoses[0] * Math.cos(rots[1]), -Math.sin(rots[1]), newPoses[1] * Math.cos(rots[1])};
            deltaX = newPoses[0] * 300d;// <-
            deltaY = newPoses[1] * 300d;// <- the new coordinates will be between 0 and 300
            deltaZ = newPoses[2] * 300d;// <-
        }

        Vector4f transformedCoordinates = new Vector4f((float) deltaX, (float) deltaY, (float) deltaZ, 1f).mul(lastWorldMatrix);
        Matrix4f matrixProj = new Matrix4f(lastProjMatrix);
        Matrix4f matrixModel = new Matrix4f(lastModViewMatrix);
        matrixProj.mul(matrixModel).project(transformedCoordinates.x(), transformedCoordinates.y(), transformedCoordinates.z(), viewport, target);

        return new Vec3d(target.x / mc.getWindow().getScaleFactor(), (displayHeight - target.y) / mc.getWindow().getScaleFactor(), target.z);
    }

    public static boolean isBehind(Vec3d convertedPosition) {
        return !(convertedPosition.z > 0) || !(convertedPosition.z < 1);
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

    public static int getGuiScale() {
        int value = mc.options.getGuiScale().getValue();
        if (value <= 0) value = mc.getWindow().calculateScaleFactor(0, mc.forcesUnicodeFont());
        return value;
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
