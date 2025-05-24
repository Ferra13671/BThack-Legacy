package com.ferra13671.BThack.impl.Modules.Combat;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.PlayerUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.Items;

@ModuleInfo(name = "AutoTotemFall", description = "lang.module.AutoTotemFall", category = "COMBAT")
public class AutoTotemFall extends Module {

    public final NumberSetting fallCheck = new NumberSetting("Fall Check", this, 10, 5, 20, true);
    public final NumberSetting toGround = new NumberSetting("To Ground", this, 5, 3, 10, true);


    boolean isFalling = false;

    @Override
    public void onEnable() {
        super.onEnable();
        isFalling = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        isFalling = false;
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        isFalling = Managers.FALL_DISTANCE_MANAGER.getFallDistance() > fallCheck.getValue() && !mc.player.verticalCollision;

        if (!isFalling) return;
        if (PlayerUtils.getGroundPos(mc.world, mc.player).y + toGround.getValue() > mc.player.getY()) {
            if (mc.player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {

                int slot = InventoryUtils.findItem(Items.TOTEM_OF_UNDYING);
                if (slot == -1) return;

                if (slot < 9) slot += 36;

                InventoryUtils.replaceItems(slot, InventoryUtils.OFFHAND_SLOT);
            }
        }
    }
}
