package com.ferra13671.BThack.impl.Modules.Combat;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.BowItem;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

@ModuleInfo(name = "FastBow", description = "lang.module.FastBow", category = "COMBAT")
public class FastBow extends Module {
                                                                                                            //2.14 is the smallest value the server can handle
    public final NumberSetting delay = new NumberSetting("Delay", this, 5.0, 2.14, 20.0, false);


    @EventSubscriber
    public void onClientTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.player.getInventory().getMainHandStack().getItem() instanceof BowItem && mc.player.isUsingItem() && (float)this.getItemInUseMaxCount() >= delay.getValue()) {
            Managers.NETWORK_MANAGER.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
            Managers.NETWORK_MANAGER.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0, mc.player.getYaw(), mc.player.getPitch()));
            mc.player.clearActiveItem();
        }
    }

    public int getItemInUseMaxCount() {
        return mc.player.isUsingItem() ? mc.player.getActiveItem().getMaxUseTime(mc.player) - mc.player.itemUseTimeLeft : 0;
    }
}
