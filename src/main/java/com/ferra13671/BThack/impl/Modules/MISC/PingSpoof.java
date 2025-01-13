package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.Packet;

import java.util.ArrayList;
import java.util.List;

public class PingSpoof extends Module {

    public final NumberSetting delay = new NumberSetting("Delay", this, 500, 100, 15000, true);

    public PingSpoof() {
        super("PingSpoof",
                "lang.module.PingSpoof",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        initSettings(
                delay
        );
    }

    private final List<Packet<?>> packets = new ArrayList<>();
    private final Ticker ticker = new Ticker();
    private boolean sendingPackets = false;

    @Override
    public void onChangeSetting(Setting setting) {
        arrayListInfo = delay.getValue() + "ms.";
    }

    @Override
    public void onEnable() {
        super.onEnable();
        ticker.reset();
        sendingPackets = false;

        arrayListInfo = delay.getValue() + "ms.";
    }

    @Override
    public void onDisable() {
        super.onDisable();
        sendPackets();
    }

    @EventSubscriber
    public void onSend(PacketEvent.Send e) {
        if (nullCheck() || mc.isInSingleplayer()) return;

        if (sendingPackets) return;
        packets.add(e.getPacket());
        e.setCancelled(true);
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) {
            if (!packets.isEmpty()) sendPackets();
        }

        if (ticker.passed(delay.getValue()) && !sendingPackets) {
            sendPackets();
            ticker.reset();
        }
    }

    private void sendPackets() {
        sendingPackets = true;
        if (!nullCheck()) {
            for (Packet<?> packet : packets)
                mc.getNetworkHandler().sendPacket(packet);
        }
        packets.clear();
        sendingPackets = false;
    }
}
