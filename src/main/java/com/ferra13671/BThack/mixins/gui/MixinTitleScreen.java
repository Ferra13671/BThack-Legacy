package com.ferra13671.BThack.mixins.gui;

import com.ferra13671.BThack.shaders.CoreShaderLoader;
import com.ferra13671.BThack.core.client.ModuleList;
import com.ferra13671.BThack.gui.screen.mainmenu.BThackMainMenuScreen;
import com.ferra13671.BThack.api.utils.Mc;
import com.ferra13671.BThack.core.client.systems.sound.Sounds;
import com.ferra13671.BThack.core.client.systems.gui.BThackScreens;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MixinTitleScreen implements Mc {

    @Inject(method = "init", at = @At("HEAD"))
    public void modifyInit(CallbackInfo ci) {
        RenderSystem.recordRenderCall(CoreShaderLoader::loadPrograms);
        if (BThackMainMenuScreen.firstOpened) {
            if (ModuleList.clientSettings.startSound.getValue())
                mc.getSoundManager().play(PositionedSoundInstance.master(Sounds.START.getSoundEvent(), 1, 1));
        }

        if (ModuleList.bthackMainMenu.isEnabled())
            mc.setScreen(BThackScreens.BTHACK_MAIN_MENU);
        else BThackMainMenuScreen.firstOpened = false;
    }
}
