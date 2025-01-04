package com.ferra13671.BThack.mixins.world;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.SoundPlayEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class MixinClientWorld extends World {

    @Shadow @Final private MinecraftClient client;

    @Shadow @Nullable public abstract Entity getEntityById(int id);

    protected MixinClientWorld(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Inject(method = "getCloudsColor", at = @At("HEAD"), cancellable = true)
    public void modifyGetCloudColor(float p_getCloudColour_1_, CallbackInfoReturnable<Vec3d> cir) {
        if (ModuleList.cloudsColor.isEnabled())
            cir.setReturnValue(new Vec3d(ModuleList.cloudsColor.cloudsRed.getValue() / 255, ModuleList.cloudsColor.cloudsGreen.getValue() / 255, ModuleList.cloudsColor.cloudsBlue.getValue() / 255));
    }

    @Inject(method = "getSkyColor", at = @At("HEAD"), cancellable = true)
    public void modifyGetSkyColor(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
        if (ModuleList.skyColor.isEnabled())
            cir.setReturnValue(new Vec3d(ModuleList.skyColor.skyRed.getValue() / 255.0f, ModuleList.skyColor.skyGreen.getValue() / 255.0f, ModuleList.skyColor.skyBlue.getValue() / 255.0f));
    }

    @Inject(method = "getStarBrightness", at = @At("HEAD"), cancellable = true)
    public void modifyStarBrightness(float f, CallbackInfoReturnable<Float> cir) {
        if (ModuleList.worldElements.isEnabled())
            cir.setReturnValue((float) ModuleList.worldElements.starBrightness.getValue());
    }

    @Inject(method = "playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZJ)V", at = @At("HEAD"), cancellable = true)
    public void modifyPlaySound(double x, double y, double z, SoundEvent soundEvent, SoundCategory soundCategory, float volume, float pitch, boolean useDistance, long seed, CallbackInfo ci) {
        SoundPlayEvent event = new SoundPlayEvent(x, y, z, soundEvent, soundCategory, volume, pitch, useDistance);
        BThack.EVENT_BUS.activate(event);
        ci.cancel();
        if (event.isCancelled())
            return;
        double d = this.client.gameRenderer.getCamera().getPos().squaredDistanceTo(event.x, event.y, event.z);
        PositionedSoundInstance positionedSoundInstance = new PositionedSoundInstance(event.soundEvent, event.soundCategory, event.volume, event.pitch, Random.create(seed), event.x, event.y, event.z);
        if (event.useDistance && d > 100.0) {
            double e = Math.sqrt(d) / 40.0;
            this.client.getSoundManager().play(positionedSoundInstance, (int)(e * 20.0));
        } else {
            this.client.getSoundManager().play(positionedSoundInstance);
        }
    }

    @ModifyArgs(method = "setTimeOfDay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld$Properties;setTimeOfDay(J)V"))
    public void modifyArgsSetTimeOfDay(Args args) {
        if (ModuleList.customDayTime.isEnabled())
            args.set(0, ModuleList.customDayTime.time < 0 ? -ModuleList.customDayTime.time : ModuleList.customDayTime.time);
    }

    @Inject(method = "addEntity", at = @At("HEAD"))
    public void modifyAddEntity(Entity entity, CallbackInfo ci) {
        if (ModuleList.visualRange.isEnabled())
            ModuleList.visualRange.onAddEntity(entity);
    }

    @Inject(method = "removeEntity", at = @At("HEAD"))
    public void modifyRemoveEntity(int entityId, Entity.RemovalReason removalReason, CallbackInfo ci) {
        if (ModuleList.visualRange.isEnabled()) {
            Entity entity = getEntityById(entityId);
            if (entity == null) return;

            ModuleList.visualRange.onRemoveEntity(entity);
        }
    }
}
