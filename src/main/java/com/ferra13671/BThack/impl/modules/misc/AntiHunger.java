package com.ferra13671.BThack.impl.modules.misc;

import com.ferra13671.BThack.events.PacketEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.Setting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.mixins.accessor.packet.IPlayerMoveC2SPacket;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

@ModuleInfo(name = "AntiHunger", description = "lang.module.AntiHunger", category = "MISC")
public class AntiHunger extends Module {

    public final BooleanSetting cancelMoveState = new BooleanSetting("Cancel Move State", this, true);

    @Override
    public void onChangeSetting(Setting<?> setting) {
        arrayListInfo = cancelMoveState.getValue() ? "Cancel Move" : "Standard";
    }

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onPacket(PacketEvent.Send e) {

        if (e.getPacket() instanceof PlayerMoveC2SPacket) {
            IPlayerMoveC2SPacket packet = (IPlayerMoveC2SPacket) e.getPacket();
            packet.setOnGround((Managers.FALL_DISTANCE_MANAGER.getFallDistance() <= 0 || mc.interactionManager.isBreakingBlock()) && mc.player.isGliding());
        }

        if (e.getPacket() instanceof ClientCommandC2SPacket) {
            if (cancelMoveState.getValue()) {
                ClientCommandC2SPacket packet = (ClientCommandC2SPacket) e.getPacket();
                if (packet.getMode() == ClientCommandC2SPacket.Mode.START_SPRINTING ||
                        packet.getMode() == ClientCommandC2SPacket.Mode.STOP_SPRINTING
                )
                    e.setCancelled(true);
            }
        }
    }
}
