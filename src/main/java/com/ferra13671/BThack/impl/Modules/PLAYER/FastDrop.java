package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

@ModuleInfo(name = "FastDrop", description = "lang.module.FastDrop", category = "PLAYER")
public class FastDrop extends Module {

    public final NumberSetting delay = new NumberSetting("Delay", this, 0, 0, 4, true);


    private int ticks;

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.options.dropKey.isPressed() && ticks > delay.getValue()) {
            Managers.NETWORK_MANAGER.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.DROP_ITEM,
                    BlockPos.ORIGIN, Direction.DOWN));
            mc.player.dropSelectedItem(Screen.hasControlDown());
            ticks = 0;
        }
        ++ticks;
    }
}
