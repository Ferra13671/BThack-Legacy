package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackMatrix;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Events.Render.RenderHudPreEvent;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Gui.Screen.HudEditor.HudEditorScreen;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.HudComponent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.SpeedMathThread;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

import java.util.Arrays;
import java.util.function.Consumer;

public class HUD extends Module {

    public final BooleanSetting rainbow = new BooleanSetting("Rainbow", this, true);
    public final NumberSetting rainbowType = new NumberSetting("Rainbow type", this, 3, 1, 8, true, rainbow::getValue);

    public final ModeSetting style = new ModeSetting("Style", this, Arrays.asList("Rounded New", "Rounded Old", "Primitive", "Old"));

    public HUD() {
        super("HUD",
                "lang.module.HUD",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                true
        );

        ModuleList.HUD = this;

        allowRemapKeyCode = false;
        allowRemapVisible = false;
        setVisible(false);

        mc.getWindow().swapBuffers();

        initSettings(
                rainbow,
                rainbowType,
                style
        );
    }
    public HudStyle hudStyle = HudStyle.valueOf(style.getValue().toUpperCase().replace(" ", "_"));

    private int updateTickDelay = 0;

    @Override
    public void onChangeSetting(Setting<?> setting) {
        if (setting == style)
            hudStyle = HudStyle.valueOf(style.getValue().toUpperCase().replace(" ", "_"));
    }

    @Override
    public void onDisable() {
        super.onDisable();
        updateTickDelay = 0;
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;
        if (mc.currentScreen instanceof HudEditorScreen) return;

        updateTickDelay++;
        if (updateTickDelay < 3) return;
        updateTickDelay = 0;

        for (HudComponent hudComponent : Client.hudComponents) {
            if (hudComponent.isEnabled()) {
                hudComponent.tick();
            }
        }

        if (!SpeedMathThread.active) {
            new SpeedMathThread().start();
        }
    }

    @EventSubscriber(priority = Integer.MIN_VALUE)
    public void onRender(RenderHudPreEvent e) {
        if (mc.currentScreen instanceof HudEditorScreen) return;
        BThackMatrix.push();
        BThackMatrix.translate(0,0,3000);

        for (HudComponent hudComponent : Client.hudComponents) {
            if (hudComponent.isEnabled()) {
                hudComponent.render();
            }
        }

        BThackMatrix.pop();
    }

    public static int getHUDColor() {
        if (ModuleList.HUD.rainbow.getValue()) {
            return ColorUtils.rainbowType(ModuleList.HUD.rainbowType.getValue().intValue());
        } else {
            return ClickGui.getClickGuiColor(false);
        }
    }

    public enum HudStyle {
        ROUNDED_NEW(pos -> {
            BThackRenderUtils.applyBlend();
            BThackRender.drawRoundedRectWithOutline(pos[0], pos[1], pos[2], pos[3], 5f, ColorUtils.fastRGBA(0, 0, 0, 150), HUD.getHUDColor(), 1f / BThackRenderUtils.getGuiScale());
        }),
        ROUNDED_OLD(pos -> {
            BThackRenderUtils.applyBlend();
            BThackRender.drawRoundedRectOld(pos[0], pos[1], pos[2], pos[3], 2.5f, HUD.getHUDColor());
            float step = 1f / BThackRenderUtils.getGuiScale();
            BThackRender.drawRoundedRectOld(pos[0] + step, pos[1] + step, pos[2] - step, pos[3] - step, 2.5f, ColorUtils.fastRGBA(0, 0, 0, 190));
        }),
        PRIMITIVE(pos -> {
            BThackRenderUtils.applyBlend();
            BThackRender.drawRect(pos[0], pos[1], pos[2], pos[3], ColorUtils.fastRGBA(0, 0, 0, 190));
            BThackRender.drawOutlineRect(pos[0], pos[1], pos[2], pos[3], 1f / BThackRenderUtils.getGuiScale(), HUD.getHUDColor());
        }),
        OLD(pos -> {});

        private final Consumer<Float[]> consumer;

        HudStyle(Consumer<Float[]> consumer) {
            this.consumer = consumer;
        }

        public void draw(float x1, float y1, float x2, float y2) {
            consumer.accept(new Float[]{x1, y1, x2, y2});
        }
    }
}