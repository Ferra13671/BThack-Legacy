package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Events.Entity.UpdateInputEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Managers.managers.TravelChange.TravelChanger;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.StrafeUtils;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.entity.attribute.EntityAttributes;

import java.util.ArrayList;
import java.util.Arrays;

public class Sprint extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Standard", "Legal")));
    public final BooleanSetting strafe = new BooleanSetting("Strafe", this, true);

    public Sprint() {
        super("Sprint",
                "lang.module.Sprint",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );
    }
    private float yaw;
    private final TravelChanger travelChanger = new TravelChanger(500,
            () -> new Float[]{yaw, RotateUtils.getCameraPitch()},
            () -> GrimUtils.sendPreActionGrimPackets(Managers.TRAVEL_CHANGE_MANAGER.getLastYaw(), Managers.TRAVEL_CHANGE_MANAGER.getLastPitch()),
            () -> true
    );

    @Override
    public void onChangeSetting(Setting<?> setting) {
        if (isEnabled()) arrayListInfo = mode.getValue() + (strafe.getValue() ? ": strafe" : "");
    }

    @Override
    public void onEnable() {
        if (ModuleList.highwayBuilder.isEnabled()) {
            toggle();
            return;
        }

        super.onEnable();

        arrayListInfo = mode.getValue() + (strafe.getValue() ? ": strafe" : "");

        if (!nullCheck()) yaw = RotateUtils.getCameraYaw();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Managers.TRAVEL_CHANGE_MANAGER.removeChanger(travelChanger);
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        switch (mode.getValue()) {
            case "Standard" -> {
                if (!mc.player.isSprinting()) {
                    try {
                        if (needSprint())
                            mc.player.setSprinting(true);
                    } catch (Exception ignored) {}
                }
            }
            case "Legal" -> mc.options.sprintKey.setPressed(!mc.player.isFallFlying());
        }

        if (strafe.getValue()) {
            yaw = StrafeUtils.getPlayerYawOnKeybindings();
            if (!Managers.TRAVEL_CHANGE_MANAGER.containsChanger(travelChanger)) Managers.TRAVEL_CHANGE_MANAGER.addChanger(travelChanger);
        } else if (Managers.TRAVEL_CHANGE_MANAGER.containsChanger(travelChanger)) Managers.TRAVEL_CHANGE_MANAGER.removeChanger(travelChanger);
    }

    @EventSubscriber
    public void onInputUpdate(UpdateInputEvent e) {
        if (!strafe.getValue()) return;
        if (!(mc.player.input instanceof KeyboardInput)) return;

        if (mc.options.forwardKey.isPressed() || mc.options.backKey.isPressed() || mc.options.leftKey.isPressed() || mc.options.rightKey.isPressed()) {
            mc.player.input.movementForward = mc.player.isSneaking() ? (float) mc.player.getAttributeValue(EntityAttributes.PLAYER_SNEAKING_SPEED) : 1;
            mc.player.input.movementSideways = 0;
        }
    }

    public boolean needSprint() {
        if (mc.player == null || mc.world == null) return false;

        return
                !mc.options.sneakKey.isPressed()
                && !mc.player.isFallFlying()
                && mc.player.getHungerManager().getFoodLevel() > 6
                && isMoving()
                && !mc.player.getAbilities().flying;
    }

    public boolean isMoving() {
        return
                !mc.player.isSneaking()
                && mc.player.input.movementForward > 0
                        && !mc.player.horizontalCollision;
    }
}
