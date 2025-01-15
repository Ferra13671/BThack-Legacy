package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ElytraReplace extends Module {

    public final NumberSetting minDurability = new NumberSetting("Min Durability", this, 15, 1, 100, true);

    public ElytraReplace() {
        super("ElytraReplace",
                "lang.module.ElytraReplace",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        initSettings(
                minDurability
        );
    }

    boolean needReplace = true;

    @EventSubscriber
    public void onClientTick(ClientTickEvent e) {
        if (nullCheck()) return;

        ItemStack stack = mc.player.getInventory().getArmorStack(2);

        if (stack.getItem() == Items.ELYTRA) {
            if (ItemUtils.getItemDurability(stack) < minDurability.getValue()) {
                if (!needReplace) return;
                for (int i = 0; i < 36; i++) {
                    ItemStack item = mc.player.getInventory().getStack(i);

                    if (item.getItem() == Items.ELYTRA) {
                        int durability = ItemUtils.getItemDurability(item);

                        if (durability > minDurability.getValue()) {
                            int needItem = i;

                            if (needItem < 9) {
                                needItem = needItem + 36;
                            }

                            InventoryUtils.replaceItems(6, needItem, 50);
                            needReplace = false;
                        }
                    }
                }
            } else {
                needReplace = true;
            }
        }
    }
}
