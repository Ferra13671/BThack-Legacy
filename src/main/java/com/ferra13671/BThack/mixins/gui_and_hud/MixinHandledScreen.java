package com.ferra13671.BThack.mixins.gui_and_hud;

import com.ferra13671.BThack.Core.Client.ModuleList;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class MixinHandledScreen<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T> {

    @Shadow
    protected Slot focusedSlot;

    protected MixinHandledScreen(Text title) {
        super(title);
    }

    @Inject(method = "drawMouseoverTooltip", at = @At(value = "HEAD"), cancellable = true)
    private void hookDrawMouseoverTooltip(DrawContext context, int x, int y, CallbackInfo ci) {
        if (focusedSlot == null)
            return;

        ItemStack itemStack = focusedSlot.getStack();

        if (itemStack.getItem() == Items.FILLED_MAP && ModuleList.tooltips.maps.getValue()) {
            ci.cancel();
            ModuleList.tooltips.renderMapTooltip(context, focusedSlot.getStack(), x, y - 30);
            return;
        }

        if (ModuleList.tooltips.isEnabled() && ModuleList.tooltips.shulkers.getValue() && itemStack.contains(DataComponentTypes.CONTAINER)) {
            ContainerComponent compoundTag = itemStack.get(DataComponentTypes.CONTAINER);
            if (compoundTag == null) return;
            if (compoundTag.stream().toList().isEmpty()) return;
            ci.cancel();

            ModuleList.tooltips.renderShulkerTooltip(itemStack, compoundTag.stream().toList(), x + 6, y - 33);
        }
    }
}
