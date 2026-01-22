package com.ferra13671.BThack.impl.modules.player;

import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.module.OneActionModule;
import com.ferra13671.BThack.api.utils.ItemUtils;
import net.minecraft.item.Items;

@ModuleInfo(name = "AutoPearl", description = "lang.module.AutoPearl", category = "PLAYER")
public class AutoPearl extends OneActionModule {

    public final BooleanSetting swingHand = new BooleanSetting("Swing Hand", this, true);

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        ItemUtils.useItem(Items.ENDER_PEARL, swingHand.getValue(), mc.player.getYaw(), mc.player.getPitch());
        toggle();
    }
}
