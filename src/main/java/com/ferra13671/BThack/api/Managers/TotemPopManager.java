package com.ferra13671.BThack.api.Managers;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Events.DisconnectEvent;
import com.ferra13671.BThack.api.Events.Entity.EntityDeathEvent;
import com.ferra13671.BThack.api.Events.Entity.TotemPopEvent;
import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TotemPopManager implements Mc {
    private final ConcurrentHashMap<UUID, Integer> playerInfos = new ConcurrentHashMap<>();

    public int getPoppedTotems(LivingEntity entity) {
        return playerInfos.getOrDefault(entity.getUuid(), 0);
    }

    @EventSubscriber
    public void onPacketReceive(PacketEvent.Receive e) {
        if (mc.world != null) {
            if (e.getPacket() instanceof EntityStatusS2CPacket packet
                    && packet.getStatus() == EntityStatuses.USE_TOTEM_OF_UNDYING) {
                Entity entity = packet.getEntity(mc.world);
                if (entity != null && entity.isAlive()) {
                    if (!(entity instanceof PlayerEntity)) return;
                    int totemsPopped = playerInfos.containsKey(entity.getUuid()) ?
                            playerInfos.get(entity.getUuid()) + 1 : 1;
                    playerInfos.put(entity.getUuid(), totemsPopped);

                    BThack.EVENT_BUS.activate(new TotemPopEvent(entity, totemsPopped));
                }
            }
        }
    }

    @EventSubscriber
    public void onEntityDeath(EntityDeathEvent e) {
        playerInfos.remove(e.entity.getUuid());
    }

    @EventSubscriber
    public void onDisconnect(DisconnectEvent e) {
        playerInfos.clear();
    }
}
