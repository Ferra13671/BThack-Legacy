package com.ferra13671.BThack.mixins.gui_and_hud;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.impl.Modules.RENDER.Tooltips;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
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

        if (itemStack.getItem() == Items.FILLED_MAP && Tooltips.maps.getValue()) {
            ci.cancel();
            Tooltips.renderMapTooltip(context, focusedSlot.getStack(), x, y - 30);
            return;
        }

        NbtCompound compound = BlockItem.getBlockEntityNbt(itemStack);

        if (ModuleList.tooltips.isEnabled() && compound != null) {
            if (compound.contains("Items", NbtElement.LIST_TYPE) && Tooltips.shulkers.getValue()) {
                ci.cancel();

                DefaultedList<ItemStack> content = DefaultedList.ofSize(27, ItemStack.EMPTY);
                Inventories.readNbt(compound, content);

                Tooltips.renderShulkerTooltip(itemStack, content, x + 6, y - 33);
            }
        }
    }
}
