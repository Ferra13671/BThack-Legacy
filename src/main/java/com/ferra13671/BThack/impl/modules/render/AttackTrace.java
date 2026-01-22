package com.ferra13671.BThack.impl.modules.render;

import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.core.render.BThackRender;
import com.ferra13671.BThack.core.render.line.RenderLine;
import com.ferra13671.BThack.events.entity.AttackEntityEvent;
import com.ferra13671.BThack.events.render.RenderWorldLastEvent;
import com.ferra13671.BThack.managers.impl.setting.Settings.ColorSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.utils.Ticker;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;

import java.awt.*;
import java.util.List;

@ModuleInfo(name = "AttackTrace", description = "lang.module.AttackTrace", category = "RENDER")
public class AttackTrace extends Module {

    public final NumberSetting renderTime = new NumberSetting("Render Time", this, 20, 5, 50, false);
    public final ColorSetting color = new ColorSetting("Color", this, new Color(255, 127, 127)).withBlockedAlpha();

    private Entity attackEntity = null;
    private final Ticker ticker = new Ticker();

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onRender(RenderWorldLastEvent e) {
        if (nullCheck()) return;

        if (attackEntity != null && attackEntity.isAlive()) {
            if (mc.player.distanceTo(attackEntity) < 20) {
                BThackRender.lineRender.prepareLineRenderer();
                BThackRender.lineRender.renderLines(List.of(new RenderLine(attackEntity, color.getValue().getRed() / 255f, color.getValue().getGreen() / 255f, color.getValue().getBlue() / 255f, 1)));
                BThackRender.lineRender.stopLineRenderer();
            }
        }
        if (ticker.passed(renderTime.getValue() * 1000)) {
            attackEntity = null;
        }
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onAttack(AttackEntityEvent e) {
        if (nullCheck()) return;

        attackEntity = e.getEntity();
        ticker.reset();
    }
}
