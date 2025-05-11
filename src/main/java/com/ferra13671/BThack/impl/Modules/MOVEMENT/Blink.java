package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Events.Entity.AttackEntityEvent;
import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

import java.util.ArrayList;
import java.util.List;

public class Blink extends Module {

    public final NumberSetting maxTime = new NumberSetting("Max Time", this, 59, 10, 59, false);
    public final BooleanSetting onlyMovement = new BooleanSetting("Only Movement", this, false);

    public final BooleanSetting autoDisable = new BooleanSetting("Auto Disable", this, true);
    public final BooleanSetting disableIfVelocity = new BooleanSetting("Disable If Velocity", this, true);
    public final BooleanSetting disableIfAttack = new BooleanSetting("Disable If Attack", this, true);

    public Blink() {
        super("Blink",
                "lang.module.Blink",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );
    }

    private final List<Packet<?>> packets = new ArrayList<>();
    private final Ticker ticker = new Ticker();

    @Override
    public void onChangeSetting(Setting<?> setting) {
        arrayListInfo = maxTime.getValue() + "ms.";
    }

    @Override
    public void onEnable() {
        super.onEnable();
        ticker.reset();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        sendPackets();
    }

    @EventSubscriber
    public void onSend(PacketEvent.Send e) {
        if (nullCheck() || mc.isInSingleplayer()) return;

        if (e.getPacket() instanceof KeepAliveC2SPacket || e.getPacket() instanceof CommonPongC2SPacket) {
            packets.add(e.getPacket());
            e.setCancelled(true);
            return;
        }

        if (onlyMovement.getValue()) {
            if (e.getPacket() instanceof PlayerMoveC2SPacket) {
                packets.add(e.getPacket());
                e.setCancelled(true);
            }
        } else {
            packets.add(e.getPacket());
            e.setCancelled(true);
        }
    }

    @EventSubscriber
    public void onPacketReceive(PacketEvent.Receive e) {
        if (nullCheck() || mc.isInSingleplayer()) return;

        if (autoDisable.getValue() && disableIfVelocity.getValue() && e.getPacket() instanceof EntityVelocityUpdateS2CPacket packet && packet.getEntityId() == mc.player.getId()) {
            setToggled(false);
        }
    }

    @EventSubscriber
    public void onAttack(AttackEntityEvent e) {
        if (autoDisable.getValue() && disableIfAttack.getValue() && e.getPlayer() == mc.player)
            setToggled(false);
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) {
            if (!packets.isEmpty()) sendPackets();
        }

        arrayListInfo = (ticker.getPassedTime() / 1000) + "s.";

        if (ticker.passed(maxTime.getValue() * 1000)) {
            sendPackets();
            ticker.reset();
        }
    }

    private void sendPackets() {
        if (!nullCheck()) {
            for (Packet<?> packet : packets)
                Managers.NETWORK_MANAGER.sendPacketNoEvent(packet);
        }
        packets.clear();
    }
}
