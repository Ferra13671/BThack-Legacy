package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.mixins.accessor.packet.IPlayerMoveC2SPacket;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class AntiHunger extends Module {

    public final BooleanSetting cancelMoveState = new BooleanSetting("Cancel Move State", this, true);

    public AntiHunger() {
        super("AntiHunger",
                "lang.module.AntiHunger",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        initSettings(
                cancelMoveState
        );
    }

    @Override
    public void onChangeSetting(Setting setting) {
        arrayListInfo = cancelMoveState.getValue() ? "Cancel Move" : "Standard";
    }

    @EventSubscriber
    public void onPacket(PacketEvent.Send e) {

        if (e.getPacket() instanceof PlayerMoveC2SPacket) {
            IPlayerMoveC2SPacket packet = (IPlayerMoveC2SPacket) e.getPacket();
            packet.setOnGround((mc.player.fallDistance <= 0 || mc.interactionManager.isBreakingBlock()) && mc.player.isFallFlying());
        }

        if (e.getPacket() instanceof ClientCommandC2SPacket) {
            if (cancelMoveState.getValue()) {
                ClientCommandC2SPacket packet = (ClientCommandC2SPacket) e.getPacket();
                if (packet.getMode() == ClientCommandC2SPacket.Mode.START_SPRINTING ||
                        packet.getMode() == ClientCommandC2SPacket.Mode.STOP_SPRINTING
                ) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
