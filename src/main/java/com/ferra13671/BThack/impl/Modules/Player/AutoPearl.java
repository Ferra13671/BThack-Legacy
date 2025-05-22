package com.ferra13671.BThack.impl.Modules.Player;

import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Module.OneActionModule;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import net.minecraft.item.Items;

@ModuleInfo(name = "AutoPearl", description = "lang.module.AutoPearl", category = "PLAYER")
public class AutoPearl extends OneActionModule {

    public final BooleanSetting swingHand = new BooleanSetting("Swing Hand", this, true);


    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        ItemUtils.useItem(Items.ENDER_PEARL, swingHand.getValue(), mc.player.getYaw(), mc.player.getPitch());
        toggle();
    }
}
