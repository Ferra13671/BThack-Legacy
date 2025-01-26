package com.ferra13671.BThack.Core.Render.Utils;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.api.Utils.Modules.StrafeUtils;
import com.ferra13671.BThack.api.Utils.RegionPos;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.*;
import net.minecraft.entity.player.PlayerEntity;
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

        if (mc.options.getBobView().getValue()) fixBobbing();
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
            float[] rots = AimBotUtils.rotations(pos);
            double[] newPoses = StrafeUtils.getMoveFactors(rots[0]);
            rots[1] = (float) Math.toRadians(rots[1]);
            newPoses = new double[]{newPoses[0] * Math.cos(rots[1]), -Math.sin(rots[1]), newPoses[1] * Math.cos(rots[1])}; //the new coordinates will be between 0 and 1
            deltaX = newPoses[0];
            deltaY = newPoses[1];
            deltaZ = newPoses[2];
        }

        Vector4f transformedCoordinates = new Vector4f((float) deltaX, (float) deltaY, (float) deltaZ, 1f).mul(lastWorldMatrix);
        Matrix4f matrixProj = new Matrix4f(lastProjMatrix);
        Matrix4f matrixModel = new Matrix4f(lastModViewMatrix);
        matrixProj.mul(matrixModel).project(transformedCoordinates.x(), transformedCoordinates.y(), transformedCoordinates.z(), viewport, target);

        return new Vec3d(target.x / mc.getWindow().getScaleFactor(), (displayHeight - target.y) / mc.getWindow().getScaleFactor(), target.z);
    }

    /*
        Corrects view bobbing actions by doing the same actions but in reverse.
        This fix does not change view bobbing when rendering the world and hands, but it does affect the correctness of
           coordinate transformations in the 'worldPosToScreenPos(Vec3d pos)' method.
     */
    private static void fixBobbing() {
        if (mc.getCameraEntity() instanceof PlayerEntity playerEntity) {
            float f = playerEntity.horizontalSpeed - playerEntity.prevHorizontalSpeed;
            float g = -(playerEntity.horizontalSpeed + f * mc.getRenderTickCounter().getTickDelta(true));
            float h = MathHelper.lerp(mc.getRenderTickCounter().getTickDelta(true), playerEntity.prevStrideDistance, playerEntity.strideDistance);
            lastModViewMatrix.translate(-(MathHelper.sin(g * 3.1415927F) * h * 0.5F), Math.abs(MathHelper.cos(g * 3.1415927F) * h), 0.0F);
            lastModViewMatrix.rotate(RotationAxis.NEGATIVE_Z.rotationDegrees(MathHelper.sin(g * 3.1415927F) * h * 3.0F));
            lastModViewMatrix.rotate(RotationAxis.NEGATIVE_X.rotationDegrees(Math.abs(MathHelper.cos(g * 3.1415927F - 0.2F) * h) * 5.0F));
        }
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
