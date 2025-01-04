package com.ferra13671.BThack.impl.Modules.PLAYER;


import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.OneActionModule;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import net.minecraft.item.Items;

public class AutoPearl extends OneActionModule {

    public final BooleanSetting swingHand = new BooleanSetting("Swing Hand", this, true);

    public AutoPearl() {

        super("AutoPearl",
                "lang.module.AutoPearl",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        initSettings(
                swingHand
        );
    }

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
