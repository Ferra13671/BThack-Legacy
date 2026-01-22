package com.ferra13671.BThack.impl.modules.combat;

import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.core.client.ModuleList;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.ModeSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.core.client.systems.sound.SoundSystem;
import com.ferra13671.BThack.core.client.systems.sound.Sounds;
import com.ferra13671.BThack.api.utils.*;
import com.ferra13671.BThack.api.utils.GrimUtils;
import com.ferra13671.BThack.api.utils.rotate.RotateUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ferra13671 and Nikitadan4pi
 */
@ModuleInfo(name = "PearlPhase", description = "lang.module.PearlPhase", category = "COMBAT")
public class PearlPhase extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("One", "Always"));

    public final BooleanSetting autoToggle = new BooleanSetting("Auto Toggle", this, false, () -> mode.getValue().equals("Always"));
    public final NumberSetting phaseDelay = new NumberSetting("Phase Delay", this, 100, 100, 500, true, () -> mode.getValue().equals("Always"));

    public final BooleanSetting swingHand = new BooleanSetting("Swing Hand", this, true);
    public final NumberSetting phasePitch = new NumberSetting("Pitch", this, 85, 0, 180, true);


    private final List<Vec3i> phasePoses = Arrays.asList(
            new Vec3i(1, 0, 0),
            new Vec3i(1, 0, 1),
            new Vec3i(1, 0, -1),
            new Vec3i(-1, 0, 0),
            new Vec3i(-1, 0, 1),
            new Vec3i(-1, 0, -1),
            new Vec3i(0, 0, 1),
            new Vec3i(0, 0, -1)
    );
    private final Ticker ticker = new Ticker();

    @Override
    public void playOffSound() {
        if (mode.getValue().equals("Always"))
            if (ModuleList.clientSettings.moduleToggleSound.getValue())
                SoundSystem.playSound(Sounds.MODULE_OFF, ModuleList.clientSettings.soundVolume.getValue().floatValue());
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }
        ticker.reset();

        if (mode.getValue().equals("One")) {
            pearlPhaseAction(mc.player.getYaw());
            toggle();
        } else
            super.onEnable();
    }

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.player.horizontalCollision) {
            if (ticker.passed(phaseDelay.getValue())) {
                BlockPos pos = BlockPos.ofFloored(mc.player.getX(), mc.player.getY(), mc.player.getZ());
                if (!BlockUtils.isOpaqueFullCube(pos) || mc.world.isAir(pos)) {
                    double minLength = 1000;
                    BlockPos minPos = null;
                    for (Vec3i vec : phasePoses) {
                        BlockPos tempPos = pos.add(vec);
                        double tempLength = MathUtils.getDistance(mc.player.getPos(), tempPos.toCenterPos());
                        if (tempLength < minLength) {
                            minLength = tempLength;
                            minPos = tempPos;
                        }
                    }
                    if (minPos != null) {
                        pearlPhaseAction(RotateUtils.rotations(minPos)[0]);
                        if (autoToggle.getValue())
                            setEnabled(false);
                        ticker.reset();
                    }
                }
            }
        } else
            ticker.reset();
    }

    public void pearlPhaseAction(float yaw) {
        GrimUtils.sendPreActionGrimPackets(yaw, phasePitch.getValue().floatValue());

        ItemUtils.useItem(Items.ENDER_PEARL, swingHand.getValue(), yaw, phasePitch.getValue().floatValue());

        GrimUtils.sendPostActionGrimPackets();
    }

}
