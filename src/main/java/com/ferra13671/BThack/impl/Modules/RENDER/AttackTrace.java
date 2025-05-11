package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Line.RenderLine;
import com.ferra13671.BThack.api.Events.Entity.AttackEntityEvent;
import com.ferra13671.BThack.api.Events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;

import java.awt.*;
import java.util.List;

public class AttackTrace extends Module {

    public final NumberSetting renderTime = new NumberSetting("Render Time", this, 20, 5, 50, false);
    public final ColorSetting color = new ColorSetting("Color", this, new Color(255, 127, 127)).withBlockedAlpha();

    public AttackTrace() {
        super("AttackTrace",
                "lang.module.AttackTrace",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );
    }

    private Entity attackEntity = null;
    private final Ticker ticker = new Ticker();

    @EventSubscriber
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
    public void onAttack(AttackEntityEvent e) {
        if (nullCheck()) return;

        attackEntity = e.getEntity();
        ticker.reset();
    }
}
