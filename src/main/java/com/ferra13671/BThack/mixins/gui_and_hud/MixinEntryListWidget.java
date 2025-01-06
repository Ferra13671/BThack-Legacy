package com.ferra13671.BThack.mixins.gui_and_hud;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ContainerWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntryListWidget.class)
public abstract class MixinEntryListWidget extends ContainerWidget {

    public MixinEntryListWidget(int i, int j, int k, int l, Text text) {
        super(i, j, k, l, text);
    }

    @Shadow public abstract boolean isMouseOver(double mouseX, double mouseY);

    @Shadow private boolean renderBackground;

    @Shadow protected abstract void enableScissor(DrawContext context);

    @Shadow private boolean renderHeader;

    @Shadow public abstract int getRowLeft();

    @Shadow public abstract double getScrollAmount();

    @Shadow protected abstract void renderList(DrawContext context, int mouseX, int mouseY, float delta);

    @Shadow public abstract int getMaxScroll();

    @Shadow protected abstract int getScrollbarPositionX();

    @Shadow protected abstract int getMaxPosition();

    @Shadow @Final private static Identifier SCROLLER_TEXTURE;

    @Shadow protected abstract void renderDecorations(DrawContext context, int mouseX, int mouseY);

    @Shadow protected abstract void renderHeader(DrawContext context, int x, int y);


    //Making fixes without using Redirect is a real pain in the ass
    @Inject(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;setShaderColor(FFFF)V", ordinal = 0), cancellable = true)
    public void modifyRenderWidget(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ci.cancel();
        context.setShaderColor(0.125F, 0.125F, 0.125F, 1.0F);
        BThackRender.drawRect(getX(), getY(), getX() + width, getY() + height, ColorUtils.fastRGBA(0,0,0, 100));
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        enableScissor(context);
        int j;
        int i;
        if (renderHeader) {
            i = getRowLeft();
            j = getY() + 4 - (int) getScrollAmount();
            renderHeader(context, i, j);
        }

        renderList(context, mouseX, mouseY, delta);
        context.disableScissor();
        if (renderBackground) {
            context.fillGradient(RenderLayer.getGuiOverlay(), getX(), getY(), getRight(), getY() + 4, -16777216, 0, 0);
            context.fillGradient(RenderLayer.getGuiOverlay(), getX(), getBottom() - 4, getRight(), getBottom(), 0, -16777216, 0);
        }

        i = getMaxScroll();
        if (i > 0) {
            j = getScrollbarPositionX();
            int k = (int)((float)(height * height) / (float)getMaxPosition());
            k = MathHelper.clamp(k, 32, height - 8);
            int l = (int) getScrollAmount() * (height - k) / i + getY();

            if (l < getY())
                l = getY();

            context.fill(j, getY(), j + 6, getBottom(), -16777216);
            context.drawGuiTexture(SCROLLER_TEXTURE, j, l, 6, k);
        }

        renderDecorations(context, mouseX, mouseY);
    }
}
