package com.ferra13671.BThack.impl.Modules.PLAYER;


import com.ferra13671.BThack.api.Events.Block.UseBlockEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;

public class PacketPlace extends Module {

    public final BooleanSetting shifting = new BooleanSetting("Shifting", this, false);
    public final NumberSetting sendPackets = new NumberSetting("Send Packets", this, 2, 1, 15, true);

    public PacketPlace() {
        super("PacketPlace",
                "lang.module.PacketPlace",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );
    }

    @EventSubscriber
    public void onPlaceBlock(UseBlockEvent e) {

        e.setCancelled(true);
        ThreadManager.startNewThread(thread -> {
            thread.sleepThread(5);

            for (int i = 0; i < sendPackets.getValue().intValue(); i++) {
                if (shifting.getValue())
                    Managers.NETWORK_MANAGER.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));

                Managers.NETWORK_MANAGER.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, e.getBlockHitResult(), 0));

                if (shifting.getValue())
                    Managers.NETWORK_MANAGER.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));

                thread.sleepThread(5);
            }
        });
    }
}
