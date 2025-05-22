package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.shaders.CoreShaderLoader;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.core.Render.Utils.BThackRenderUtils;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.*;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @Shadow @Final private BufferBuilderStorage bufferBuilders;

    @Shadow @Final private MinecraftClient client;

    @Unique boolean allowShader = false;

    @Inject(method = "reload(Lnet/minecraft/resource/ResourceManager;)V", at = @At("TAIL"))
    public void modifyReload(ResourceManager manager, CallbackInfo ci) {
        CoreShaderLoader.loadPrograms();
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void beforeRender(ObjectAllocator allocator, RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, Matrix4f positionMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
        BThackRenderUtils.lastWorldMatrix.set(positionMatrix);
        BThackRenderUtils.updateMatrixData();
    }

    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    public void modifyRenderWeather(FrameGraphBuilder frameGraphBuilder, Vec3d pos, float tickDelta, Fog fog, CallbackInfo ci) {
        if (ModuleList.noWeather.isEnabled() && (!ModuleList.ambience.isEnabled() || !ModuleList.ambience.customWeather.getValue()))
            ci.cancel();
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/PostEffectProcessor;render(Lnet/minecraft/client/render/FrameGraphBuilder;IILnet/minecraft/client/gl/PostEffectProcessor$FramebufferSet;)V", ordinal = 0))
    public void modifyRenderOutlinePostProcessor(PostEffectProcessor instance, FrameGraphBuilder builder, int textureWidth, int textureHeight, PostEffectProcessor.FramebufferSet framebufferSet) {
        if (!ModuleList.shaders.isEnabled())
            instance.render(builder, textureWidth, textureHeight, framebufferSet);
    }

    @Redirect(method = "method_62215", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getRainGradient(F)F"))
    public float modifyGetRainGradientInRenderSky(ClientWorld instance, float v) {
        return ModuleList.ambience.getRainGradient(instance.getRainGradient(v));
    }

    @Redirect(method = "getEntitiesToRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;hasOutline(Lnet/minecraft/entity/Entity;)Z"))
    public boolean modifyHasOutline(MinecraftClient instance, Entity entity) {
        if (ModuleList.shaders.isEnabled()) return true;
        else return instance.hasOutline(entity);
    }

    @ModifyArg(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderEntity(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V"), index = 6)
    public VertexConsumerProvider modifyArgRenderEntity(VertexConsumerProvider vertexConsumers,
                                                        @Local Entity entityLocalRef) {
        if (ModuleList.shaders.isEnabled() && hasAllowedEntity(entityLocalRef)) {
            allowShader = true;
            OutlineVertexConsumerProvider outlineVertexConsumerProvider = bufferBuilders.getOutlineVertexConsumers();
            int i = entityLocalRef.getTeamColorValue();
            outlineVertexConsumerProvider.setColor(ColorHelper.getRed(i), ColorHelper.getGreen(i), ColorHelper.getBlue(i), 255);
            return outlineVertexConsumerProvider;
        } else {
            allowShader = false;
            return vertexConsumers;
        }
    }


    @Inject(method = "canDrawEntityOutlines", at = @At("HEAD"), cancellable = true)
    public void modifyCanDrawEntityOutlines(CallbackInfoReturnable<Boolean> cir) {
        if (ModuleList.shaders.isEnabled()) cir.setReturnValue(true);
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
