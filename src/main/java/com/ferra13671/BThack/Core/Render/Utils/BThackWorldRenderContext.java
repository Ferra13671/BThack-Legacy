package com.ferra13671.BThack.Core.Render.Utils;

import org.joml.Matrix4f;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;

public final class BThackWorldRenderContext  {
	private WorldRenderer worldRenderer;
	private MatrixStack matrixStack;
	private float tickDelta;
	private long limitTime;
	private boolean blockOutlines;
	private Camera camera;
	private Frustum frustum;
	private GameRenderer gameRenderer;
	private LightmapTextureManager lightmapTextureManager;
	private Matrix4f projectionMatrix;
	private VertexConsumerProvider consumers;
	private Profiler profiler;
	private boolean advancedTranslucency;
	private ClientWorld world;

	private Entity entity;
	private double cameraX;
	private double cameraY;
	private double cameraZ;
	private BlockPos blockPos;
	private BlockState blockState;

	public void prepare(WorldRenderer worldRenderer, MatrixStack matrixStack, float tickDelta, long limitTime, boolean blockOutlines, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, VertexConsumerProvider consumers, Profiler profiler, boolean advancedTranslucency, ClientWorld world) {
		this.worldRenderer = worldRenderer;
		this.matrixStack = matrixStack;
		this.tickDelta = tickDelta;
		this.limitTime = limitTime;
		this.blockOutlines = blockOutlines;
		this.camera = camera;
		this.gameRenderer = gameRenderer;
		this.lightmapTextureManager = lightmapTextureManager;
		this.projectionMatrix = projectionMatrix;
		this.consumers = consumers;
		this.profiler = profiler;
		this.advancedTranslucency = advancedTranslucency;
		this.world = world;
	}

	public void setFrustum(Frustum frustum) {
		this.frustum = frustum;
	}

	public void prepareBlockOutline(Entity entity, double cameraX, double cameraY, double cameraZ, BlockPos blockPos, BlockState blockState) {
		this.entity = entity;
		this.cameraX = cameraX;
		this.cameraY = cameraY;
		this.cameraZ = cameraZ;
		this.blockPos = blockPos;
		this.blockState = blockState;
	}


	public WorldRenderer worldRenderer() {
		return worldRenderer;
	}

	public MatrixStack matrixStack() {
		return matrixStack;
	}

	public float tickDelta() {
		return tickDelta;
	}

	public long limitTime() {
		return limitTime;
	}

	public boolean blockOutlines() {
		return blockOutlines;
	}

	public Camera camera() {
		return camera;
	}

	public Matrix4f projectionMatrix() {
		return projectionMatrix;
	}

	public ClientWorld world() {
		return world;
	}

	public Frustum frustum() {
		return frustum;
	}

	public VertexConsumerProvider consumers() {
		return consumers;
	}

	public GameRenderer gameRenderer() {
		return gameRenderer;
	}

	public LightmapTextureManager lightmapTextureManager() {
		return lightmapTextureManager;
	}

	public Profiler profiler() {
		return profiler;
	}

	public boolean advancedTranslucency() {
		return advancedTranslucency;
	}

	public VertexConsumer vertexConsumer() {
		return consumers.getBuffer(RenderLayer.getLines());
	}

	public Entity entity() {
		return entity;
	}

	public double cameraX() {
		return cameraX;
	}

	public double cameraY() {
		return cameraY;
	}

	public double cameraZ() {
		return cameraZ;
	}

	public BlockPos blockPos() {
		return blockPos;
	}

	public BlockState blockState() {
		return blockState;
	}
}
