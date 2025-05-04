package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;

public class AutoMend extends Module {

    public final BooleanSetting autoToggle = new BooleanSetting("Auto Toggle", this, true);

    public AutoMend() {
        super("AutoMend",
                "lang.module.AutoMend",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        initSettings(
                autoToggle
        );
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck() || needPause()) return;

        int slot = getSlot();
        if (slot == -1) {
            if (autoToggle.getValue()) {
                sendNotification(LanguageSystem.translate("lang.module.AutoMend.toggleMessage"));
                setToggled(false);
            }
            return;
        }
        InventoryUtils.replaceItems(slot, InventoryUtils.OFFHAND_SLOT);
    }


    public boolean needPause() {
        ItemStack itemStack = mc.player.getOffHandStack();

        if (itemStack.isEmpty()) return false;

        if (ItemUtils.equalsEnchantment(itemStack, Enchantments.MENDING)) {
            return itemStack.getDamage() != 0;
        }

        return false;
    }

    private int getSlot() {
        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = mc.player.getInventory().getStack(i);

            if (ItemUtils.equalsEnchantment(itemStack, Enchantments.MENDING) && itemStack.getDamage() > 0) {
                return i;
            }
        }

        return -1;
    }
}
