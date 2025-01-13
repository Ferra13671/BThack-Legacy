package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Events.Entity.JumpHeightEvent;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Events.Entity.UpdateInputEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.StrafeUtils;
import com.ferra13671.BThack.api.Utils.PlayerUtils;
import com.ferra13671.BThack.mixins.accessor.ILivingEntity;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;

public class Speed extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("Normal", "3b3t"));

    public final NumberSetting speed = new NumberSetting("Speed", this, 0.25,0.1,1,false, () -> mode.getValue().equals("Normal"));
    public final NumberSetting jumpHeight = new NumberSetting("Jump Height", this, 0.11, 0.05, 0.42, false, () -> mode.getValue().equals("Normal"));

    public Speed() {
        super("Speed",
                "lang.module.Speed",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        initSettings(
                mode,

                speed,
                jumpHeight
        );
    }

    @EventSubscriber
    public void onPlayerTick(ClientTickEvent e) {
        if (nullCheck()) return;

        switch (mode.getValue()) {
            case "Normal" -> {
                ((ILivingEntity) mc.player).setJumpingCooldown(0);

                if (mc.player.onGround && mc.player.input.movementForward > 0 && !PlayerUtils.isInWater() && !mc.player.isInLava()) {
                    mc.player.setSprinting(true);

                    float yaw = mc.player.yaw * 0.0174532920F;

                    mc.player.velocity.x -= MathHelper.sin(yaw) * (speed.getValue() / 5);
                    mc.player.velocity.z += MathHelper.cos(yaw) * (speed.getValue() / 5);
                }
            }
            case "3b3t" -> {
                ((ILivingEntity) mc.player).setJumpingCooldown(0);

                if (!PlayerUtils.isInWater() && !mc.player.isInFluid()) {
                    if (mc.player.input.movementForward > 0 && !PlayerUtils.isInWater() && !mc.player.isInLava()) {
                        mc.player.setSprinting(true);

                        double[] moves = StrafeUtils.getMoveFactors(0.55);

                        mc.player.velocity.x = moves[0];
                        mc.player.velocity.z = moves[1];
                    }
                }
            }
        }
    }

    @EventSubscriber
    public void onJumpHeight(JumpHeightEvent e) {
        if (mode.getValue().equals("Normal"))
            e.setJumpHeight((float) jumpHeight.getValue());
    }

    @EventSubscriber
    public void onInput(UpdateInputEvent e) {
        mc.player.input.jumping = true;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        mc.options.jumpKey.setPressed(false);
    }
}
