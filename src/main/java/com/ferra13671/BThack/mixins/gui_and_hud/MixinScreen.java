package com.ferra13671.BThack.mixins.gui_and_hud;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Gui.MainMenu.BThackMainMenuScreen;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class MixinScreen extends AbstractParentElement implements Drawable {
    //Yeah, it looks really dumb, but who cares?

    @Shadow @Nullable protected MinecraftClient client;

    @Shadow protected abstract void switchFocus(GuiNavigationPath path);

    @Unique
    private static float _mouseX;
    @Unique
    private static float _mouseY;

    @Inject(method = "renderBackgroundTexture", at = @At("HEAD"), cancellable = true)
    private static void modifyRenderBackgroundTexture(DrawContext context, Identifier texture, int x, int y, float u, float v, int width, int height, CallbackInfo ci) {
        if (ModuleList.bthackMainMenu.isEnabled() && Module.nullCheck()) {
            ci.cancel();
            BThackMainMenuScreen.drawWallpaper(_mouseX, _mouseY);
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (client.world == null) {
            _mouseX = mouseX;
            _mouseY = mouseY;
        }
    }

    @Inject(method = "setInitialFocus()V", at = @At("HEAD"), cancellable = true)
    public void modifySetInitialFocus(CallbackInfo ci) {
        if (client == null) {
            ci.cancel();
            if (Mc.mc.getNavigationType().isKeyboard()) {
                GuiNavigation.Tab tab = new GuiNavigation.Tab(true);
                GuiNavigationPath guiNavigationPath = super.getNavigationPath(tab);
                if (guiNavigationPath != null) {
                    switchFocus(guiNavigationPath);
                }
            }
        }
    }
}
