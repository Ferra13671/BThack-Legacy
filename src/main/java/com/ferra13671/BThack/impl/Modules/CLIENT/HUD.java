package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Events.Render.RenderHudPreEvent;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Gui.HudMover.HudMoverScreen;
import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.SpeedMathThread;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.TextureUtils.GLTexture;
import com.ferra13671.TextureUtils.PathMode;

public class HUD extends Module {

    public final BooleanSetting rainbow = new BooleanSetting("Rainbow", this, true);
    public final NumberSetting rainbowType = new NumberSetting("Rainbow type", this, 3, 1, 8, true, rainbow::getValue);

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
        visible = false;

        mc.getWindow().swapBuffers();

        initSettings(
                rainbow,
                rainbowType
        );
    }

    public static final GLTexture bthack_logo = GLTexture.fromPath("assets/bthack/bthacklogo.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA);


    private int updateTickDelay = 0;

    @Override
    public void onDisable() {
        super.onDisable();
        updateTickDelay = 0;
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;
        if (mc.currentScreen instanceof HudMoverScreen) return;

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

    @EventSubscriber(priority = Integer.MAX_VALUE)
    public void onRender(RenderHudPreEvent e) {
        if (mc.currentScreen instanceof HudMoverScreen) return;
        BThackRender.guiGraphics.getMatrices().push();
        BThackRender.guiGraphics.getMatrices().translate(0,0,3000);

        for (HudComponent hudComponent : Client.hudComponents) {
            if (hudComponent.isEnabled()) {
                hudComponent.render();
            }
        }

        BThackRender.guiGraphics.getMatrices().pop();
    }

    public static int getHUDColor() {
        if (ModuleList.HUD.rainbow.getValue()) {
            return ColorUtils.rainbowType((int) ModuleList.HUD.rainbowType.getValue());
        } else {
            return ClickGui.getClickGuiColor(false);
        }
    }
}