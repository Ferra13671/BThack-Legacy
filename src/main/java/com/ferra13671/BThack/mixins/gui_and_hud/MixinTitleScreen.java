package com.ferra13671.BThack.mixins.gui_and_hud;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.SoundSystem.Sounds;
import com.ferra13671.BThack.api.Utils.System.BThackScreens;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MixinTitleScreen implements Mc {

    @Unique
    private static boolean firstOpened = true;

    @Unique
    private static boolean guiOverwritten = false;


    @Inject(method = "init", at = @At("HEAD"))
    public void modifyInit(CallbackInfo ci) {
        if (!guiOverwritten) {
            mc.options.getGuiScale().setValue(2);
            guiOverwritten = true;
        }

        if (firstOpened) {
            boolean needReturn = false;
            if (ModuleList.clientSettings.startSound.getValue())
                mc.getSoundManager().play(PositionedSoundInstance.master(Sounds.START.getSoundEvent(), 1, 1));

            if (BThack.instance.versionInfo.isOutdated()) {
                if (BThack.instance.versionInfo.isNeedShowAgainAllReleases()) {//              It seems that disabling showing the same issue
                    if (BThack.instance.versionInfo.isNeedShowAgainOneRelease()) {//      <--- multiple times is broken, but I assure you it works.
                        mc.setScreen(BThackScreens.OUTDATED_VERSION);
                        needReturn = true;
                    }
                }
            }

            if (!needReturn) {
                if (BThack.instance.versionInfo.isFirstLaunched()) {
                    mc.setScreen(BThackScreens.LANGUAGE_SELECTOR);
                    needReturn = true;
                }
            }

            firstOpened = false;
            if (needReturn) return;
        }

        if (ModuleList.bthackMainMenu.isEnabled())
            mc.setScreen(BThackScreens.BTHACK_MAIN_MENU);
    }
}
