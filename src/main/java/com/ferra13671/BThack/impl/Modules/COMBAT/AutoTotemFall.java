package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.PlayerUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.Items;

public class AutoTotemFall extends Module {

    public final NumberSetting fallCheck = new NumberSetting("Fall Check", this, 10, 5, 20, true);
    public final NumberSetting toGround = new NumberSetting("To Ground", this, 5, 3, 10, true);

    public AutoTotemFall() {
        super("AutoTotemFall",
                "lang.module.AutoTotemFall",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );
    }

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

        isFalling = mc.player.fallDistance > fallCheck.getValue() && !mc.player.verticalCollision;

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
