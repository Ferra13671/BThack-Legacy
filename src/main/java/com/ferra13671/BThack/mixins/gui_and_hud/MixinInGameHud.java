package com.ferra13671.BThack.mixins.gui_and_hud;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.Render.RenderHudPreEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.JumpingMount;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud {

	@Shadow @Final private static Identifier PUMPKIN_BLUR;

	/*
	The blur renderer doesn't like the font renderer, so to invoke the event should be at the
	 very beginning of the hud renderer to avoid render bugs.
	 */
	@Inject(method = "render", at = @At("HEAD"))
	public void modifyRenderPre(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		BThack.EVENT_BUS.activate(new RenderHudPreEvent(tickCounter.getTickDelta(true)));
	}

	@Inject(method = "renderVignetteOverlay", at = @At("HEAD"), cancellable = true)
	public void modifyRenderVignetteOverlay(DrawContext context, Entity entity, CallbackInfo ci) {
		if (ModuleList.noOverlay.isEnabled() && ModuleList.noOverlay.vignette.getValue()) {
			ci.cancel();
		}
	}

	@Inject(method = "renderOverlay", at = @At("HEAD"), cancellable = true)
	public void modifyRenderPumpkinBlurOverlay(DrawContext context, Identifier texture, float opacity, CallbackInfo ci) {
		if (texture == PUMPKIN_BLUR)
			if (ModuleList.noOverlay.isEnabled() && ModuleList.noOverlay.pumpkin.getValue()) ci.cancel();
	}

	@Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
	public void modifyRenderHotbar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		if (ModuleList.noOverlay.isEnabled() && ModuleList.noOverlay.hotbar.getValue()) ci.cancel();
	}

	@Inject(method = "renderMountJumpBar", at = @At("HEAD"), cancellable = true)
	public void modifyRenderMountJumpBar(JumpingMount mount, DrawContext context, int x, CallbackInfo ci) {
		if (ModuleList.noOverlay.isEnabled() && ModuleList.noOverlay.jumpBar.getValue()) ci.cancel();
	}

	@Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
	public void modifyRenderExperienceBar(DrawContext context, int x, CallbackInfo ci) {
		if (ModuleList.noOverlay.isEnabled() && ModuleList.noOverlay.experiense.getValue()) ci.cancel();
	}

	@Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"), cancellable = true)
	public void modifyRenderStatusEffectOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		if (ModuleList.noOverlay.isEnabled() && ModuleList.noOverlay.effects.getValue()) ci.cancel();
	}

	@Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At("HEAD"), cancellable = true)
	public void modifyRenderScoreboardSidebar(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
		if (ModuleList.noOverlay.isEnabled() && ModuleList.noOverlay.scoreBoard.getValue()) ci.cancel();
	}

	@Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
	public void modifyRenderCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		if ((ModuleList.noOverlay.isEnabled() && ModuleList.noOverlay.crosshair.getValue()) || ModuleList.csCrosshair.isEnabled()) ci.cancel();
	}

	@Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
	public void onRenderPortalOverlay(DrawContext context, float nauseaStrength, CallbackInfo ci) {
		if (ModuleList.noOverlay.isEnabled() && ModuleList.noOverlay.portal.getValue()) ci.cancel();
	}
}