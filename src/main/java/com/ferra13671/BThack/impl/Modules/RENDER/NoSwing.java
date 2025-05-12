package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;

import java.util.ArrayList;
import java.util.Arrays;

@ModuleInfo(name = "NoSwing", description = "lang.module.NoSwing", category = "RENDER")
public class NoSwing extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Client", "Server")));


    @EventSubscriber
    public void onPacket(PacketEvent.Send e) {
        if (mode.getValue().equals("Server")  && e.getPacket() instanceof HandSwingC2SPacket) {
            e.setCancelled(true);
        }
    }

    @EventSubscriber
    public void onClientTick(ClientTickEvent e) {
        if (nullCheck()) return;
        mc.player.handSwinging = false;
        mc.player.handSwingTicks = 0;
        mc.player.handSwingProgress = 0.0f;
        mc.player.lastHandSwingProgress = 0.0f;
    }
}
