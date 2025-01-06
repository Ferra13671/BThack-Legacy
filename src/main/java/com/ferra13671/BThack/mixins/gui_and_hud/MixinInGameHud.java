package com.ferra13671.BThack.mixins.gui_and_hud;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.Render.RenderHudPostEvent;
import com.ferra13671.BThack.impl.Modules.RENDER.NoOverlay;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
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

	@Inject(method = "render", at = @At("TAIL"))
	public void modifyRenderPost(DrawContext drawContext, float tickDelta, CallbackInfo callbackInfo) {
		//RenderSystem.enableBlend();
		//RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		BThack.EVENT_BUS.activate(new RenderHudPostEvent(tickDelta));
		//RenderSystem.setShaderColor(1, 1, 1, 1);
		//RenderSystem.setShader(GameRenderer::getPositionColorProgram);
	}

	@Inject(method = "renderVignetteOverlay", at = @At("HEAD"), cancellable = true)
	public void modifyRenderVignetteOverlay(DrawContext context, Entity entity, CallbackInfo ci) {
		if (ModuleList.noOverlay.isEnabled() && NoOverlay.vignette.getValue()) ci.cancel();
	}

	@Inject(method = "renderOverlay", at = @At("HEAD"), cancellable = true)
	public void modifyRenderPumpkinBlurOverlay(DrawContext context, Identifier texture, float opacity, CallbackInfo ci) {
		if (texture == PUMPKIN_BLUR)
			if (ModuleList.noOverlay.isEnabled() && NoOverlay.pumpkin.getValue()) ci.cancel();
	}

	@Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
	public void modifyRenderHotbar(float tickDelta, DrawContext context, CallbackInfo ci) {
		if (ModuleList.noOverlay.isEnabled() && NoOverlay.hotbar.getValue()) ci.cancel();
	}

	@Inject(method = "renderMountJumpBar", at = @At("HEAD"), cancellable = true)
	public void modifyRenderMountJumpBar(JumpingMount mount, DrawContext context, int x, CallbackInfo ci) {
		if (ModuleList.noOverlay.isEnabled() && NoOverlay.jumpBar.getValue()) ci.cancel();
	}

	@Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
	public void modifyRenderExperienceBar(DrawContext context, int x, CallbackInfo ci) {
		if (ModuleList.noOverlay.isEnabled() && NoOverlay.experiense.getValue()) ci.cancel();
	}

	@Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"), cancellable = true)
	public void modifyRenderStatusEffectOverlay(DrawContext context, CallbackInfo ci) {
		if (ModuleList.noOverlay.isEnabled() && NoOverlay.effects.getValue()) ci.cancel();
	}

	@Inject(method = "renderScoreboardSidebar", at = @At("HEAD"), cancellable = true)
	public void modifyRenderScoreboardSidebar(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
		if (ModuleList.noOverlay.isEnabled() && NoOverlay.scoreBoard.getValue()) ci.cancel();
	}

	@Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
	public void modifyRenderCrosshair(DrawContext context, CallbackInfo ci) {
		if ((ModuleList.noOverlay.isEnabled() && NoOverlay.crosshair.getValue()) || ModuleList.csCrosshair.isEnabled()) ci.cancel();
	}

	@Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
	public void onRenderPortalOverlay(DrawContext context, float nauseaStrength, CallbackInfo ci) {
		if (ModuleList.noOverlay.isEnabled() && NoOverlay.portal.getValue()) ci.cancel();
	}
}