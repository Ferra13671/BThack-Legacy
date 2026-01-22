package com.ferra13671.BThack.impl.modules.player;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.utils.InventoryUtils;
import com.ferra13671.BThack.api.utils.ItemUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;

@ModuleInfo(name = "AutoMend", description = "lang.module.AutoMend", category = "PLAYER")
public class AutoMend extends Module {

    public final BooleanSetting autoToggle = new BooleanSetting("Auto Toggle", this, true);


    @EventSubscriber
    @SuppressWarnings("unused")
    public void onTick(ClientTickEvent e) {
        if (nullCheck() || needPause()) return;

        int slot = getSlot();
        if (slot == -1) {
            if (autoToggle.getValue()) {
                sendNotification(LanguageSystem.translate("lang.module.AutoMend.toggleMessage"));
                setEnabled(false);
            }
            return;
        }
        InventoryUtils.replaceItems(slot, InventoryUtils.OFFHAND_SLOT);
    }


    @SuppressWarnings("DataFlowIssue")
    public boolean needPause() {
        ItemStack itemStack = mc.player.getOffHandStack();

        if (itemStack.isEmpty()) return false;

        if (ItemUtils.equalsEnchantment(itemStack, Enchantments.MENDING))
            return itemStack.getDamage() != 0;

        return false;
    }

    @SuppressWarnings("DataFlowIssue")
    private int getSlot() {
        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = mc.player.getInventory().getStack(i);
            if (ItemUtils.equalsEnchantment(itemStack, Enchantments.MENDING) && itemStack.getDamage() > 0)
                return i;
        }

        return -1;
    }
}
