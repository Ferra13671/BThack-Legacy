package com.ferra13671.BThack.mixins.gui_and_hud;

import com.ferra13671.BThack.api.Gui.Screen.MainMenu.BThackMainMenuScreen;
import com.ferra13671.BThack.api.GuiSystem.Screen.BThackScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public class MixinMultiplayerScreen extends Screen {
    @Shadow @Final private Screen parent;

    protected MixinMultiplayerScreen(Text title) {
        super(title);
    }

    @Inject(method = "close", at = @At("HEAD"), cancellable = true)
    public void modifyClose(CallbackInfo ci) {
        if (parent instanceof BThackMainMenuScreen) {
            ci.cancel();
            BThackScreen.changeScreen(this, parent);
        }
    }
}
