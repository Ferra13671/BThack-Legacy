package com.ferra13671.BThack.Core.Client.Systems;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.DisconnectEvent;
import com.ferra13671.BThack.api.Events.Entity.EntityDeathEvent;
import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;

public class HotbarSystem implements Mc {
    private int currentSlot = -1;

    @EventSubscriber
    public void onSend(PacketEvent.Send e) {
        if (e.getPacket() instanceof UpdateSelectedSlotC2SPacket packet) {
            if (currentSlot == packet.getSelectedSlot()) e.setCancelled(true);
            else currentSlot = packet.getSelectedSlot();
        }
    }

    @EventSubscriber
    public void onReceive(PacketEvent.Receive e) {
        if (Module.nullCheck()) return;
        if (e.getPacket() instanceof UpdateSelectedSlotS2CPacket packet) {
            if (ModuleList.noServerSlot.isEnabled()) {
                currentSlot = mc.player.getInventory().selectedSlot;
                return;
            }

            currentSlot = packet.getSlot();
        }
    }

    @EventSubscriber
    public void onDisconnect(DisconnectEvent e) {
        currentSlot = -1;
    }

    @EventSubscriber
    public void onDeath(EntityDeathEvent e) {
        if (e.entity == mc.player) currentSlot = -1;
    }
}
