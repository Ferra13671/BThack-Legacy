package com.ferra13671.BThack.mixins;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.GuiOpenEvent;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.impl.Modules.CLIENT.FPSReducer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.tick.TickManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient implements Mc {

    @Shadow
    public ClientPlayerEntity player;

    @Shadow @Nullable public Screen currentScreen;

    @Shadow @Final public Mouse mouse;

    @Shadow @Final private Window window;

    @Shadow public boolean skipGameRender;

    @Shadow @Final private SoundManager soundManager;

    @Shadow public abstract void updateWindowTitle();

    @Shadow private boolean disconnecting;

    @Shadow @Nullable public ClientWorld world;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void modifyMinecraftInit(RunArgs args, CallbackInfo ci) {
        BThack.instance.onInitializePost();
    }

    @Inject(method = "getFramerateLimit", at = @At("HEAD"), cancellable = true)
    public void modifyGetFramerateLimit(CallbackInfoReturnable<Integer> cir) {
        if (ModuleList.fpsReducer.isEnabled()) {
            if (!mc.isWindowFocused()) {
                if (FPSReducer.lastFocusTicks <= 0)
                    cir.setReturnValue((int) FPSReducer.fpsLimit.getValue());
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

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    public void modifySetScreen(Screen screen, CallbackInfo ci) {
        GuiOpenEvent event = new GuiOpenEvent(screen);
        BThack.EVENT_BUS.activate(event);
        if (event.isCancelled()) {
            ci.cancel();
            return;
        }
        screen = event.getScreen();

        if (currentScreen != null) {
            currentScreen.removed();
        }

        if (screen == null && disconnecting) {
            throw new IllegalStateException("Trying to return to in-game GUI during disconnection");
        } else {
            if (screen == null && this.world == null) {
                screen = new TitleScreen();
            } else if (screen == null && player.isDead()) {
                if (player.showsDeathScreen()) {
                    screen = new DeathScreen(null, world.getLevelProperties().isHardcore());
                } else {
                    player.requestRespawn();
                }
            }

            currentScreen = screen;
            if (currentScreen != null) {
                currentScreen.onDisplayed();
            }

            BufferRenderer.reset();
            if (screen != null) {
                mouse.unlockCursor();
                KeyBinding.unpressAll();
                screen.init(mc, window.getScaledWidth(), window.getScaledHeight());
                skipGameRender = false;
            } else {
                soundManager.resumeAll();
                mouse.lockCursor();
            }

            updateWindowTitle();
        }

        ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void modifyStartTick(CallbackInfo info) {
        BThack.EVENT_BUS.activate(new ClientTickEvent());
    }
}
