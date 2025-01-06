package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

import java.util.Arrays;

public class AutoOffhand extends Module {

    public static ModeSetting mode;

    //---------Standard---------//
    public static ModeSetting item;
    //--------------------------//

    //---------Extra---------//
    public static BooleanSetting totem;
    public static NumberSetting totemMaxHP;
    public static NumberSetting totemMinHP;

    public static BooleanSetting crystal;
    public static NumberSetting crystalMaxHP;
    public static NumberSetting crystalMinHP;

    public static BooleanSetting gapple;
    public static NumberSetting gappleMaxHP;
    public static NumberSetting gappleMinHP;
    //-----------------------//

    public static BooleanSetting replaceOther;

    public AutoOffhand() {
        super("AutoOffhand",
                "lang.module.AutoOffhand",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

        mode = new ModeSetting("Mode", this, Arrays.asList("Standard", "Extra"));

        //---------Standard---------//
        item = new ModeSetting("Item", this, Arrays.asList("Totem", "Crystal", "Gapple"), () -> mode.getValue().equals("Standard"));
        //--------------------------//

        //---------Extra---------//
        totem = new BooleanSetting("Totem", this, true, () -> mode.getValue().equals("Extra"));
        totemMaxHP = new NumberSetting("Totem MaxHP", this, 12, 1, 20, false, () -> mode.getValue().equals("Extra") && totem.getValue());
        totemMinHP = new NumberSetting("Totem MinHP", this, 0, 0, 19, false, () -> mode.getValue().equals("Extra") && totem.getValue());

        crystal = new BooleanSetting("Crystal", this, false, () -> mode.getValue().equals("Extra"));
        crystalMaxHP = new NumberSetting("Crystal MaxHP", this, 1, 1, 20, false, () -> mode.getValue().equals("Extra") && crystal.getValue());
        crystalMinHP = new NumberSetting("Crystal MinHP", this, 0, 0, 19, false, () -> mode.getValue().equals("Extra") && crystal.getValue());

        gapple = new BooleanSetting("Gapple", this, true, () -> mode.getValue().equals("Extra"));
        gappleMaxHP = new NumberSetting("Gapple MaxHP", this, 20, 1, 20, false, () -> mode.getValue().equals("Extra") && gapple.getValue());
        gappleMinHP = new NumberSetting("Gapple MinHP", this, 12, 0, 19, false, () -> mode.getValue().equals("Extra") && gapple.getValue());
        //-----------------------//

        replaceOther = new BooleanSetting("Replace Other", this, true);

        initSettings(
                mode,

                item,

                totem,
                totemMaxHP,
                totemMinHP,

                crystal,
                crystalMaxHP,
                crystalMinHP,

                gapple,
                gappleMaxHP,
                gappleMinHP,

                replaceOther
        );
    }

    @EventSubscriber
    public void onClientTick(ClientTickEvent e) {
        if (nullCheck()) return;

        switch (mode.getValue()) {
            case "Standard":
                arrayListInfo = "Standard";
                switch (item.getValue()) {
                    case "Totem":
                        tryOffhand(Items.TOTEM_OF_UNDYING);
                        break;
                    case "Crystal":
                        tryOffhand(Items.END_CRYSTAL);
                        break;
                    case "Gapple":
                        tryOffhand(Items.ENCHANTED_GOLDEN_APPLE);
                        break;
                }
                break;
            case "Extra":
                arrayListInfo = "Extra";
                float playerHp = mc.player.getHealth();
                if (totem.getValue()) {
                    if (playerHp > totemMinHP.getValue() && playerHp < totemMaxHP.getValue()) {
                        if (tryOffhand(Items.TOTEM_OF_UNDYING))
                            return;
                    }
                }
                if (crystal.getValue()) {
                    if (playerHp > crystalMinHP.getValue() && playerHp < crystalMaxHP.getValue()) {
                        if (tryOffhand(Items.END_CRYSTAL))
                            return;
                    }
                }
                if (gapple.getValue()) {
                    if (playerHp > gappleMinHP.getValue() && playerHp < gappleMaxHP.getValue()) {
                        if (tryOffhand(Items.ENCHANTED_GOLDEN_APPLE))
                            return;
                    }
                }
                break;
        }
    }

    //It works weird sometimes, but I don't know how to fix it. Just take it for what it is :/
    //In any case, this problem is in many cheats.
    public static void releaseHand() {
        MinecraftClient mc = MinecraftClient.getInstance();

        int freeSlot = InventoryUtils.findItem(Items.AIR);
        if (freeSlot < 9) freeSlot += 36;

        mc.interactionManager.clickSlot(0, freeSlot, 1, SlotActionType.PICKUP_ALL, mc.player);
        mc.interactionManager.tick();
    }

    private boolean tryOffhand(Item item) {
        if (mc.player.getOffHandStack().getItem() != item) {
            if (!replaceOther.getValue()) {
                if (mc.player.getOffHandStack().getItem() != Items.AIR)
                    return false;
            }

            int slot = InventoryUtils.findItem(item);
            if (slot == -1) return false;
            if (slot < 9) slot += 36;

            //if (mc.player.getInventory().getItemStack().getItem() != Items.AIR)
            //    releaseHand();

            InventoryUtils.replaceItems(slot, InventoryUtils.OFFHAND_SLOT, 0);

            /*
            mc.playerController.windowClick(0, slot, 1, ClickType.PICKUP, mc.player);
            mc.playerController.updateController();
            mc.playerController.windowClick(0, InventoryUtils.OFFHAND_SLOT, 1, ClickType.PICKUP_ALL, mc.player);
            mc.playerController.updateController();
            mc.playerController.windowClick(0, slot, 1, ClickType.PICKUP, mc.player);
            mc.playerController.updateController();

             */
            return true;
        }
        return false;
    }
}
