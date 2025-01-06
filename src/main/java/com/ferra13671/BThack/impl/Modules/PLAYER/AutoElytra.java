package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.mixins.accessor.IEntity;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

public class AutoElytra extends Module {

    public static NumberSetting fallDist;

    public AutoElytra() {
        super("AutoElytra",
                "lang.module.AutoElytra",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        fallDist = new NumberSetting("Fall Distance", this, 5, 3, 10, false);

        initSettings(
                fallDist
        );
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck() || mc.player.isFallFlying()) return;

        if (mc.player.fallDistance >= fallDist.getValue() && !mc.player.verticalCollision) {
            if (!(mc.player.getInventory().getArmorStack(2).getItem() instanceof ElytraItem)) {
                if (equipAction())
                    startFlyAction();
            } else
                startFlyAction();
        }
    }

    public boolean equipAction() {
        int slot = InventoryUtils.findItem(Items.ELYTRA, 36);
        if (slot == -1) return false;

        InventoryUtils.replaceItems(InventoryUtils.CHESTPLATE_SLOT, (slot < 9 ? slot + 36 : slot));
        return true;
    }

    public void startFlyAction() {
        IEntity player = (IEntity) mc.player;
        player.invokeSetFlag(7, true);
        mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
    }
}
