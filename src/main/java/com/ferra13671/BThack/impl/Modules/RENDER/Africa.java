package com.ferra13671.BThack.impl.Modules.RENDER;


import com.ferra13671.BTbot.api.Utils.Generate.GenerateNumber;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Events.Render.RenderHudPostEvent;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class Africa extends Module {

    public Africa() {
        super("Africa",
                "lang.module.Africa",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );
    }

    private int africaColor = ColorUtils.fastRGBA(255, 101, 0, 76);

    @EventSubscriber
    public void onClientTick(ClientTickEvent e) {
        if (nullCheck()) return;

        mc.player.setFireTicks(3);
    }

    private short tickTimer = 0;
    private int colorAlpha = 76;

    @EventSubscriber(priority = Integer.MIN_VALUE)
    public void onHudRender(RenderHudPostEvent e) {
        BThackRender.drawRect(0,0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), africaColor);
        tickTimer++;
        if (tickTimer > 15) {
            int alpha = (int) (colorAlpha * GenerateNumber.generateFloat(0.94f, 1.05f));
            if (alpha > 110) {
                alpha = 110;
            } else if (alpha < 50) {
                alpha = 50;
            }
            colorAlpha = alpha;

            africaColor = ColorUtils.fastRGBA(255, 101, 0, alpha);
            tickTimer = 0;
        }
    }
}
