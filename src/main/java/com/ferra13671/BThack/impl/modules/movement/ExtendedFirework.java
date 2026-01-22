package com.ferra13671.BThack.impl.modules.movement;

import com.ferra13671.BThack.events.entity.FireworkTickEvent;
import com.ferra13671.BThack.events.PacketEvent;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.api.imixin.ModifyFireworkRocket;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.impl.setting.Settings.NumberSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.Setting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.utils.Ticker;
import com.ferra13671.BThack.mixins.accessor.entity.IFireworkRocketEntity;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "ExtendedFirework", description = "lang.module.ExtendedFirework", category = "MOVEMENT")
public class ExtendedFirework extends Module {

    public final NumberSetting maxTime = new NumberSetting("Max Time", this, 55, 45, 59, false);


    private boolean extendFirework;
    private final Ticker ticker = new Ticker();
    private FireworkRocketEntity firework;

    private static final List<CommonPongC2SPacket> packetList = new ArrayList<>();
    private static int freezeMillis = 0;

    @Override
    public void onChangeSetting(Setting<?> setting) {
        arrayListInfo = maxTime.getValue() + "s.";
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (firework != null)
            ((ModifyFireworkRocket) firework)._explodeAndRemove();
        firework = null;
        extendFirework = false;
        freezeMillis = 0;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        arrayListInfo = maxTime.getValue() + "s.";
    }

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onFireworkTick(FireworkTickEvent e) {
        if (nullCheck()) return;
        if (mc.player.isGliding() && firework != e.firework && ((IFireworkRocketEntity) e.firework).hookWasShotByEntity() && ((IFireworkRocketEntity) e.firework).getShooter() == mc.player) {
            extendFirework = true;
            e.setCancelled(true);
            firework = e.firework;
            freezeMillis = (int) (maxTime.getValue() * 10);
            ticker.reset();
        }
    }

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onTick(ClientTickEvent e) {
        if (nullCheck() || !extendFirework) return;

        if (!mc.player.isGliding() || mc.player.isOnGround() || ticker.passed(maxTime.getValue() * 1000)) {
            extendFirework = false;
            if (firework != null) {
                ((ModifyFireworkRocket) firework)._explodeAndRemove();
                firework = null;
            }
            freezeMillis = 0;
        }
        if (freezeMillis <= 0)
            return;
        freezeMillis -= 50;
        if (freezeMillis <= 0) {
            if (!packetList.isEmpty()) {
                for (CommonPongC2SPacket p : packetList)
                    Managers.NETWORK_MANAGER.sendPacket(p);
                packetList.clear();
            }
        }
    }

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onPacketSend(PacketEvent.Send e) {
        if (nullCheck()) return;
        if (e.getPacket() instanceof CommonPongC2SPacket
                && (!extendFirework || !mc.player.isGliding())) {
            freezeMillis = 0;
        }
        if (freezeMillis <= 0) return;
        if (e.getPacket() instanceof CommonPongC2SPacket packet) {
            e.setCancelled(true);
            packetList.add(packet);
        }
    }

    @EventSubscriber
    @SuppressWarnings("DataFlowIssue")
    public void onPacketReceive(PacketEvent.Receive e) {
        if (nullCheck() || !mc.player.isGliding() || !extendFirework) return;
        if (e.getPacket() instanceof EntitiesDestroyS2CPacket packet && firework != null) {
            for (int id : packet.getEntityIds()) {
                if (id == firework.getId()) {
                    e.setCancelled(true);
                    return;
                }
            }
        }
        if (e.getPacket() instanceof PlayerPositionLookS2CPacket) {
            extendFirework = false;
            if (firework != null) {
                ((ModifyFireworkRocket) firework)._explodeAndRemove();
                firework = null;
            }
            freezeMillis = 0;
        }
    }
}
