package com.ferra13671.BThack.mixins.gui_and_hud;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Gui.Screen.MainMenu.BThackMainMenuScreen;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.SoundSystem.Sounds;
import com.ferra13671.BThack.api.GuiSystem.BThackScreens;
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
    private static boolean guiOverwritten = false;


    @Inject(method = "init", at = @At("HEAD"))
    public void modifyInit(CallbackInfo ci) {
        if (!guiOverwritten) {
            mc.options.getGuiScale().setValue(2);
            guiOverwritten = true;
        }

        if (BThackMainMenuScreen.firstOpened) {
            if (ModuleList.clientSettings.startSound.getValue())
                mc.getSoundManager().play(PositionedSoundInstance.master(Sounds.START.getSoundEvent(), 1, 1));
        }

        if (ModuleList.bthackMainMenu.isEnabled())
            mc.setScreen(BThackScreens.BTHACK_MAIN_MENU);
    }
}
