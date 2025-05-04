package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.Arrays;

public class AutoOffhand extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("Standard", "Extra"));

    //---------Standard---------//
    public final ModeSetting item = new ModeSetting("Item", this, Arrays.asList("Totem", "Crystal", "Gapple"), () -> mode.getValue().equals("Standard"));
    //--------------------------//

    //---------Extra---------//
    public final BooleanSetting totem = new BooleanSetting("Totem", this, true, () -> mode.getValue().equals("Extra"));
    public final NumberSetting totemMaxHP = new NumberSetting("Totem MaxHP", this, 12, 1, 20, false, () -> mode.getValue().equals("Extra") && totem.getValue());
    public final NumberSetting totemMinHP = new NumberSetting("Totem MinHP", this, 0, 0, 19, false, () -> mode.getValue().equals("Extra") && totem.getValue());

    public final BooleanSetting crystal = new BooleanSetting("Crystal", this, false, () -> mode.getValue().equals("Extra"));
    public final NumberSetting crystalMaxHP = new NumberSetting("Crystal MaxHP", this, 1, 1, 20, false, () -> mode.getValue().equals("Extra") && crystal.getValue());
    public final NumberSetting crystalMinHP = new NumberSetting("Crystal MinHP", this, 0, 0, 19, false, () -> mode.getValue().equals("Extra") && crystal.getValue());

    public final BooleanSetting gapple = new BooleanSetting("Gapple", this, true, () -> mode.getValue().equals("Extra"));
    public final NumberSetting gappleMaxHP = new NumberSetting("Gapple MaxHP", this, 20, 1, 20, false, () -> mode.getValue().equals("Extra") && gapple.getValue());
    public final NumberSetting gappleMinHP = new NumberSetting("Gapple MinHP", this, 12, 0, 19, false, () -> mode.getValue().equals("Extra") && gapple.getValue());
    //-----------------------//

    public final BooleanSetting replaceOther = new BooleanSetting("Replace Other", this, true);

    public AutoOffhand() {
        super("AutoOffhand",
                "lang.module.AutoOffhand",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

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

    private boolean tryOffhand(Item item) {
        if (mc.player.getOffHandStack().getItem() != item) {
            if (!replaceOther.getValue()) {
                if (mc.player.getOffHandStack().getItem() != Items.AIR)
                    return false;
            }

            int slot = InventoryUtils.findItem(item);
            if (slot == -1) return false;
            if (slot < 9) slot += 36;

            InventoryUtils.replaceItems(slot, InventoryUtils.OFFHAND_SLOT);
            return true;
        }
        return false;
    }
}
