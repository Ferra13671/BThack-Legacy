package com.ferra13671.BThack.mixins.gui_and_hud;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Gui.MainMenu.OutdatedVersionScreen;
import com.ferra13671.BThack.api.Interfaces.Mc;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MixinTitleScreen implements Mc {

    @Unique
    private boolean guiOverwritten = false;

    @Unique
    private boolean firstOpened = true;


    @Inject(method = "init", at = @At("HEAD"))
    public void modifyInit(CallbackInfo ci) {
        if (!guiOverwritten) {
            mc.options.getGuiScale().setValue(2);
            guiOverwritten = true;
        }

        if (ModuleList.bthackMainMenu.isEnabled())
            mc.setScreen(BThack.instance.mainMenu);

        //I don't know what the fuck, but without that shit the button rendering breaks.  :/
        if (firstOpened) {
            int j = mc.getWindow().getScaledWidth();
            int k = mc.getWindow().getScaledHeight();
            BThack.instance.mainMenu.resize(mc, j, k);

            if (BThack.instance.versionInfo.isOutdated()) {
                if (BThack.instance.versionInfo.isNeedShowAgainAllReleases()) {//              It looks like the ban on showing the same release
                    if (BThack.instance.versionInfo.isNeedShowAgainOneRelease()) {//      <--- multiple times is broken, but I assure you it works.
                        mc.setScreen(new OutdatedVersionScreen());
                    }
                }
            }

            firstOpened = false;
        }
    }
}
