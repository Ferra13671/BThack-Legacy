package com.ferra13671.BThack.mixins;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.GuiOpenEvent;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.TextureUtils.GLTextureSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.tick.TickManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient implements Mc {

    @Shadow
    public ClientPlayerEntity player;

    @Shadow @Nullable public Screen currentScreen;

    @Shadow @Final public Mouse mouse;

    @Shadow @Nullable public ClientWorld world;

    @Unique GuiOpenEvent lastEvent;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void modifyMinecraftInit(RunArgs args, CallbackInfo ci) {
        BThack.instance.onInitializePost();
    }

    @Inject(method = "getFramerateLimit", at = @At("HEAD"), cancellable = true)
    public void modifyGetFramerateLimit(CallbackInfoReturnable<Integer> cir) {
        if (ModuleList.fpsReducer.isEnabled()) {
            if (!mc.isWindowFocused()) {
                if (ModuleList.fpsReducer.lastFocusTicks <= 0)
                    cir.setReturnValue((int) ModuleList.fpsReducer.fpsLimit.getValue());
            }
        }
    }

    @Inject(method = "getTargetMillisPerTick", at = @At("HEAD"), cancellable = true)
    public void modifyGetTargetMillisPerTick(float millis, CallbackInfoReturnable<Float> cir) {
        if (this.world != null) {
            TickManager tickManager = this.world.getTickManager();
            if (tickManager.shouldTick()) {
                cir.setReturnValue(Math.max(millis, tickManager.getMillisPerTick()) * Managers.TICK_MANAGER.getTickModifier());
            }
        }

        cir.setReturnValue(millis * Managers.TICK_MANAGER.getTickModifier());
    }

    @ModifyVariable(method = "setScreen", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public Screen modifySetScreen1(Screen screen) {
        GuiOpenEvent event = new GuiOpenEvent(screen);
        BThack.EVENT_BUS.activate(event);
        lastEvent = event;
        return event.getScreen();
    }

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    public void modifySetScreen2(Screen screen, CallbackInfo ci) {
        if (lastEvent != null) {
            if (lastEvent.isCancelled()) ci.cancel();
            lastEvent = null;
        }
    }

    @Unique
    private static boolean closing = false;
    @Inject(method = "stop", at = @At("HEAD"))
    public void modifyStop(CallbackInfo ci) {
        if (closing) return;
        GLTextureSystem.close(); //Deletes all textures that were ever loaded into the GLTextureSystem at runtime.
        closing = true;
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void modifyStartTick(CallbackInfo info) {
        BThack.EVENT_BUS.activate(new ClientTickEvent());
    }
}
