package com.ferra13671.BThack.impl.Modules.Render;


import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Render.BThackMatrix;
import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.events.Entity.AttackEntityEvent;
import com.ferra13671.BThack.events.Render.RenderHudPreEvent;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.client.util.Window;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

@ModuleInfo(name = "CS_Crosshair", description = "lang.module.CS_Crosshair", category = "RENDER")
public class CS_Crosshair extends Module {

    public final NumberSetting width = new NumberSetting("Width", this, 4, 1, 50, false);
    public final NumberSetting height = new NumberSetting("Height", this, 1, 1, 10, false);
    public final NumberSetting distance = new NumberSetting("Distance", this, 3, 2, 30, true);
    public final BooleanSetting movable = new BooleanSetting("Movable", this, true);
    public final NumberSetting scatterLimit = new NumberSetting("Scatter Limit", this, 15, 6, 30, true, movable::getValue);
    public final NumberSetting scatterSpeed = new NumberSetting("Scatter Speed", this, 0.15, 0.05, 0.3, false, movable::getValue);

    public final BooleanSetting leftRect = new BooleanSetting("Left Rect", this, true);
    public final BooleanSetting rightRect = new BooleanSetting("Right Rect", this, true);
    public final BooleanSetting upRect = new BooleanSetting("Up Rect", this, true);
    public final BooleanSetting downRect = new BooleanSetting("Down Rect", this, true);
    public final BooleanSetting centerRect = new BooleanSetting("Center Rect", this, true);

    public final BooleanSetting rainbow = new BooleanSetting("Rainbow", this, false);

    public final ColorSetting colorSet = new ColorSetting("Color", this, new Color(0, 255, 0), () -> !rainbow.getValue()).withBlockedAlpha();

    public final NumberSetting rotate = new NumberSetting("Rotate", this, 0, 0, 90, true);


    private float currentSpread = 0;
    private float prevSpread = 0;

    @EventSubscriber
    public void onOverlay(RenderHudPreEvent e) {
        if (nullCheck()) return;

        if (!movable.getValue())
            currentSpread = 0;

        BThackMatrix.push();

        Window window = mc.getWindow();

        Color color = rainbow.getValue() ? new Color(ColorUtils.rainbow()) : colorSet.getValue();

        BThackMatrix.translate(window.getScaledWidth() / 2f, window.getScaledHeight() / 2f, 0);
        BThackMatrix.peek().getPositionMatrix().rotate((float) Math.toRadians(rotate.getValue()), 0, 0, 1);

        float spread = MathHelper.lerp(e.getPartialTicks(), prevSpread, currentSpread);

        if (centerRect.getValue())
            BThackRender.drawRect(-height.getValue().floatValue(), -height.getValue().floatValue(), height.getValue().intValue(), height.getValue().intValue(), color.hashCode());

        if (upRect.getValue())
            BThackRender.drawRect(0 -height.getValue().floatValue(), 0 - height.getValue().floatValue() - distance.getValue().floatValue() - width.getValue().floatValue() - spread, height.getValue().floatValue(), 0 - height.getValue().floatValue() - distance.getValue().floatValue() - spread, color.hashCode());

        if (leftRect.getValue())
            BThackRender.drawRect(0 - height.getValue().floatValue() - distance.getValue().floatValue() - width.getValue().floatValue() - spread, 0 - height.getValue().floatValue(), 0 - height.getValue().floatValue() - distance.getValue().floatValue() - spread, height.getValue().floatValue(), color.hashCode());

        if (downRect.getValue())
            BThackRender.drawRect(-height.getValue().floatValue(), height.getValue().floatValue() + distance.getValue().floatValue() + spread, height.getValue().floatValue(), height.getValue().floatValue() + distance.getValue().floatValue() + width.getValue().floatValue() + spread, color.hashCode());

        if (rightRect.getValue())
            BThackRender.drawRect(height.getValue().floatValue() + distance.getValue().floatValue() + spread, 0 - height.getValue().floatValue(), height.getValue().floatValue() + distance.getValue().floatValue() + width.getValue().floatValue() + spread, height.getValue().floatValue(), color.hashCode());

        BThackMatrix.peek().getPositionMatrix().rotate((float) -Math.toRadians(rotate.getValue()), 0, 0, 1);
        BThackMatrix.translate(-(window.getScaledWidth() / 2f), -(window.getScaledHeight() / 2f), 0);

        prevSpread = currentSpread;
        currentSpread -= scatterSpeed.getValue().floatValue();
        if (currentSpread < 0)
            currentSpread = 0;

        BThackMatrix.pop();
    }

    @EventSubscriber
    public void onAction(AttackEntityEvent e) {
        if (nullCheck() || !movable.getValue()) return;

        if (e.getPlayer() == mc.player) {
            currentSpread += 6;
            if (currentSpread > scatterLimit.getValue())
                currentSpread = scatterLimit.getValue().intValue();
        }
    }
}
