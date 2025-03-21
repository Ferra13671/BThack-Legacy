package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils;
import com.ferra13671.BThack.api.IMixin.ModifyWorldRenderer;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer implements ModifyWorldRenderer {

    @Shadow @Final private BufferBuilderStorage bufferBuilders;

    @Shadow @Final private MinecraftClient client;

    @Shadow @Nullable private VertexBuffer starsBuffer;

    @Shadow protected abstract BuiltBuffer buildStarsBuffer(Tessellator tessellator);

    @Unique boolean allowShader = false;

    @Override
    public void generateStarsMap() {
        if (starsBuffer != null)
            starsBuffer.close();

        starsBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        starsBuffer.bind();
        starsBuffer.upload(ModuleList.worldElements.isEnabled() && ModuleList.worldElements.changeStars.getValue() ? ModuleList.worldElements.buildStarsBuffer() : buildStarsBuffer(Tessellator.getInstance()));
        VertexBuffer.unbind();
    }

    @Inject(method = "reload(Lnet/minecraft/resource/ResourceManager;)V", at = @At("TAIL"))
    public void modifyReload(ResourceManager manager, CallbackInfo ci) {
        ModuleList.shaders.shaderInited = false;
        ModuleList.shaders.reloadShader();
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void beforeRender(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
        BThackRenderUtils.lastWorldMatrix.set(positionMatrix);
        BThackRenderUtils.updateMatrixData();
    }

    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    public void modifyRenderWeather(LightmapTextureManager manager, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        if (ModuleList.noWeather.isEnabled())
            ci.cancel();
    }

    @Inject(method = "tickRainSplashing", at = @At("HEAD"), cancellable = true)
    public void modifyTickRainSplashing(Camera camera, CallbackInfo ci) {
        if (ModuleList.noWeather.isEnabled())
            ci.cancel();
    }



    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;hasOutline(Lnet/minecraft/entity/Entity;)Z"))
    public boolean modifyHasOutline(MinecraftClient instance, Entity entity) {
        if (ModuleList.shaders.isEnabled()) return false;
        else return instance.hasOutline(entity);
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderEntity(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V"), index = 6)
    public VertexConsumerProvider modifyArgRenderEntity(VertexConsumerProvider vertexConsumers,
                                                        @Local Entity entityLocalRef) {
        if (ModuleList.shaders.isEnabled() && hasAllowedEntity(entityLocalRef)) {
            allowShader = true;
            OutlineVertexConsumerProvider outlineVertexConsumerProvider = bufferBuilders.getOutlineVertexConsumers();
            int i = entityLocalRef.getTeamColorValue();
            outlineVertexConsumerProvider.setColor(ColorHelper.Argb.getRed(i), ColorHelper.Argb.getGreen(i), ColorHelper.Argb.getBlue(i), 255);
            return outlineVertexConsumerProvider;
        } else {
            allowShader = false;
            return vertexConsumers;
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/OutlineVertexConsumerProvider;draw()V", shift = At.Shift.AFTER))
    public void modifyRenderBeforeOutlineRender(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
        if (ModuleList.shaders.isEnabled()) {
            if (!ModuleList.shaders.shaderInited) ModuleList.shaders.reloadShader();
            MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
        }
    }

    @Unique
    public boolean hasAllowedEntity(Entity entity) {
        boolean value = false;
        if (ModuleList.shaders.players.getValue() && entity instanceof PlayerEntity && entity != client.player) value = true;
        if (ModuleList.shaders.self.getValue() && entity == client.player) value = true;
        if (ModuleList.shaders.items.getValue() && entity instanceof ItemEntity) value = true;
        if (ModuleList.shaders.hostiles.getValue() && KillAuraUtils.isHostile(entity)) value = true;
        if (ModuleList.shaders.golems.getValue() && KillAuraUtils.isGolem(entity)) value = true;
        if (ModuleList.shaders.passive.getValue() && KillAuraUtils.isPassive(entity)) value = true;
        if (ModuleList.shaders.crystals.getValue() && entity instanceof EndCrystalEntity) value = true;
        return value;
    }
}
