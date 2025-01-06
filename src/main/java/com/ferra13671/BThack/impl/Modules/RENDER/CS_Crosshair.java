package com.ferra13671.BThack.impl.Modules.RENDER;


import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Events.Entity.AttackEntityEvent;
import com.ferra13671.BThack.api.Events.Render.RenderHudPostEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.client.util.Window;

import java.awt.*;

public class CS_Crosshair extends Module {

    public static NumberSetting width;
    public static NumberSetting height;
    public static NumberSetting distance;
    public static BooleanSetting movable;
    public static NumberSetting scatterLimit;
    public static NumberSetting scatterSpeed;

    public static BooleanSetting leftRect;
    public static BooleanSetting rightRect;
    public static BooleanSetting upRect;
    public static BooleanSetting downRect;
    public static BooleanSetting centerRect;

    public static NumberSetting redColor;
    public static NumberSetting greenColor;
    public static NumberSetting blueColor;

    public static NumberSetting rotate;

    public static BooleanSetting rainbow;

    private float spread = 0;

    public CS_Crosshair() {
        super("CS_Crosshair",
                "lang.module.CS_Crosshair",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        width = new NumberSetting("Width", this, 4, 1, 50, false);
        height = new NumberSetting("Height", this, 1, 1, 10, false);
        distance = new NumberSetting("Distance", this, 3, 2, 30, true);

        movable = new BooleanSetting("Movable", this, true);
        scatterLimit = new NumberSetting("Scatter Limit", this, 15, 6, 30, true, movable::getValue);
        scatterSpeed = new NumberSetting("Scatter Speed", this, 0.15, 0.05, 0.3, false, movable::getValue);

        leftRect = new BooleanSetting("Left Rect", this, true);
        rightRect = new BooleanSetting("Right Rect", this, true);
        upRect = new BooleanSetting("Up Rect", this, true);
        downRect = new BooleanSetting("Down Rect", this, true);
        centerRect = new BooleanSetting("Center Rect", this, true);

        redColor = new NumberSetting("Red", this, 0, 0, 255, true, () -> !rainbow.getValue());
        greenColor = new NumberSetting("Green", this, 255, 0, 255, true, () -> !rainbow.getValue());
        blueColor = new NumberSetting("Blue", this, 0, 0, 255, true, () -> !rainbow.getValue());

        rotate = new NumberSetting("Rotate", this, 0, 0, 90, true);

        rainbow = new BooleanSetting("Rainbow", this, false);

        initSettings(
                width,
                height,
                distance,
                movable,
                scatterLimit,
                scatterSpeed,
                leftRect,
                rightRect,
                upRect,
                downRect,
                centerRect,
                redColor,
                greenColor,
                blueColor,
                rotate,
                rainbow
        );
    }

    @EventSubscriber
    public void onOverlay(RenderHudPostEvent e) {
        if (nullCheck()) return;

        if (!movable.getValue())
            spread = 0;

        BThackRender.guiGraphics.getMatrices().push();

        Window window = mc.getWindow();

        Color color = rainbow.getValue() ? new Color(ColorUtils.rainbow(100)) : new Color((int) redColor.getValue(), (int) greenColor.getValue(), (int) blueColor.getValue());

        BThackRender.guiGraphics.getMatrices().translate(window.getScaledWidth() / 2f, window.getScaledHeight() / 2f, 0);
        BThackRender.guiGraphics.getMatrices().peek().getPositionMatrix().rotate((float) Math.toRadians(rotate.getValue()), 0, 0, 1);

        if (centerRect.getValue())
            BThackRender.drawRect((int)-height.getValue(), (int)- height.getValue(), (int)height.getValue(), (int)height.getValue(), color.hashCode());

        if (upRect.getValue())
            BThackRender.drawRect((int)(0 -height.getValue()), (int)(0 - height.getValue() - distance.getValue() - spread), (int)(0 + height.getValue()), (int)(0 - height.getValue() - distance.getValue() - width.getValue() - spread), color.hashCode());

        if (leftRect.getValue())
            BThackRender.drawRect((int)(0 - height.getValue() - distance.getValue() - spread), (int)(0 - height.getValue()), (int)(0 - height.getValue() - distance.getValue() - width.getValue() - spread), (int)(0 + height.getValue()), color.hashCode());

        if (downRect.getValue())
            BThackRender.drawRect((int)-height.getValue(), (int)(0 + height.getValue() + distance.getValue() + spread), (int)( + height.getValue()), (int)(0 + height.getValue() + distance.getValue() + width.getValue() + spread), color.hashCode());

        if (rightRect.getValue())
            BThackRender.drawRect((int)(0 + height.getValue() + distance.getValue() + spread), (int)(0 - height.getValue()), (int)(0 + height.getValue() + distance.getValue() + width.getValue() + spread), (int)(0 + height.getValue()), color.hashCode());

        BThackRender.guiGraphics.getMatrices().peek().getPositionMatrix().rotate((float) -Math.toRadians(rotate.getValue()), 0, 0, 1);
        BThackRender.guiGraphics.getMatrices().translate(-(window.getScaledWidth() / 2f), -(window.getScaledHeight() / 2f), 0);

        spread -= (float) scatterSpeed.getValue();
        if (spread < 0)
            spread = 0;

        BThackRender.guiGraphics.getMatrices().pop();
    }

    @EventSubscriber
    public void onAction(AttackEntityEvent e) {
        if (nullCheck() || !movable.getValue()) return;

        if (e.getPlayer() == mc.player) {
            spread += 6;
            if (spread > scatterLimit.getValue())
                spread = (int) scatterLimit.getValue();
        }
    }
}
