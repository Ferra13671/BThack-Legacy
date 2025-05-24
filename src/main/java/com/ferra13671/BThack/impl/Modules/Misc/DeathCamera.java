package com.ferra13671.BThack.impl.Modules.Misc;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.events.Camera.PositionCameraEvent;
import com.ferra13671.BThack.events.Camera.RotateCameraEvent;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.events.GuiOpenEvent;
import com.ferra13671.BThack.events.InputEvent;
import com.ferra13671.BThack.events.Player.ChangePlayerLookEvent;
import com.ferra13671.BThack.events.Render.RenderHudPreEvent;
import com.ferra13671.BThack.events.SetOpaqueCubeEvent;
import com.ferra13671.BThack.managers.managers.Setting.Settings.KeyCodeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.impl.Modules.Player.FreeCam;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.util.math.Vec2f;

@ModuleInfo(name = "DeathCamera", description = "lang.module.DeathCamera", category = "MISC")
public class DeathCamera extends Module {

    public final KeyCodeSetting respawnKey = new KeyCodeSetting("Respawn Key", this);


    private boolean death = false;
    private final FreeCam.FreeCamData freeCamData = new FreeCam.FreeCamData();

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        ModuleList.freeCam.setEnabled(false);

        super.onEnable();
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void onDisable() {
        super.onDisable();
        death = false;
        if (nullCheck()) return;
        mc.player.input = new KeyboardInput(mc.options);
        mc.player.requestRespawn();
    }

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) {
            death = false;
            return;
        }

        if (mc.player.isDead() && !death) {
            freeCamData.reset();
            death = true;
            mc.player.input = new FreeCam.FreecamKeyboardInput(mc.options, freeCamData);
            mc.player.setHealth(20);
        }
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onSetScreen(GuiOpenEvent e) {
        if (e.getScreen() instanceof DeathScreen) e.setCancelled(true);
    }

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onInput(InputEvent.KeyInputEvent e) {
        if (nullCheck()) return;

        if (respawnKey.isPressed() && death) {
            mc.player.requestRespawn();
            death = false;
            mc.player.input = new KeyboardInput(mc.options);
        }
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onCameraPosition(PositionCameraEvent e) {
        if (death)
            e.setPosition(freeCamData.lastPosition.lerp(freeCamData.position, e.getTickDelta()));
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onCameraRotate(RotateCameraEvent e) {
        if (death)
            e.setRotation(new Vec2f(freeCamData.yaw, freeCamData.pitch));
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onMouseUpdate(ChangePlayerLookEvent e) {
        if (death) {
            e.cancel();
            freeCamData.changeLookDirection(e.cursorDeltaX, e.cursorDeltaY);
        }
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onSetOpaqueCube(SetOpaqueCubeEvent e) {
        if (death)
            e.setCancelled(true);
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onHudRender(RenderHudPreEvent e) {
        if (death)
            BThackRender.drawCenteredString(String.format(LanguageSystem.translate("lang.module.DeathCamera.message"), KeyboardUtils.getKeyName(respawnKey.getValue())), mc.getWindow().getScaledWidth() / 2f, mc.getWindow().getScaledHeight() / 4f, ColorUtils.fastRGBA(255, 100, 100, 255));
    }
}
