package com.ferra13671.BThack.mixins.gui_and_hud;

import com.ferra13671.BThack.api.Gui.TransitionScreen;
import com.ferra13671.BThack.api.Gui.MainMenu.BThackMainMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SelectWorldScreen.class)
public class MixinSelectWorldScreen extends Screen {

    @Shadow @Final protected Screen parent;

    protected MixinSelectWorldScreen(Text title) {
        super(title);
    }

    @Inject(method = "close", at = @At("HEAD"), cancellable = true)
    public void modifyClose(CallbackInfo ci) {
        if (parent instanceof BThackMainMenuScreen) {
            ci.cancel();
            client.setScreen(new TransitionScreen(() -> this, () -> parent));
        }
    }

    @Inject(method = "method_19939", at = @At("HEAD"), cancellable = true)
    public void modifyMethod_19939(ButtonWidget button, CallbackInfo ci) {
        ci.cancel();
        client.setScreen(new TransitionScreen(() -> this, () -> parent));
    }
}
