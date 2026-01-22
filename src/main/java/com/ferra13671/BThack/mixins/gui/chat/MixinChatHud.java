package com.ferra13671.BThack.mixins.gui.chat;

import com.ferra13671.BThack.core.client.Client;
import com.ferra13671.BThack.core.client.ModuleList;
import com.ferra13671.BThack.api.imixin.ModifyChatHud;
import com.ferra13671.BThack.impl.modules.render.BetterChat;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.util.ChatMessages;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChatHud.class)
public abstract class MixinChatHud implements ModifyChatHud {


    @Shadow public abstract int getWidth();

    @Shadow public abstract double getChatScale();

    @Shadow public abstract boolean isChatFocused();

    @Shadow private int scrolledLines;

    @Shadow private boolean hasUnreadNewMessages;

    @Shadow public abstract void scroll(int scroll);

    @Shadow @Final private List<ChatHudLine.Visible> visibleMessages;

    @Shadow @Final private MinecraftClient client;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public void _clearChat() {
        this.visibleMessages.clear();
    }

    //---------Chat Animation---------//
    @Shadow private int getLineHeight() {
        return 0;
    }

    @ModifyArg(method = "render", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", ordinal = 0))
    private float applyYOffset(float y) {
        if (ModuleList.betterChat.isEnabled() && ModuleList.betterChat.chatAnimation.getValue()) {
            // Apply the offset
            ModuleList.betterChat.calculateYOffset(getLineHeight(), scrolledLines);

            // Raised mod compatibility
            if (FabricLoader.getInstance().getObjectShare().get("raised:hud") instanceof Integer distance) {
                // for Raised 1.2.0+
                y -= distance;
            } else if (FabricLoader.getInstance().getObjectShare().get("raised:distance") instanceof Integer distance) {
                y -= distance;
            }

            return y + BetterChat.chatDisplacementY;
        } else return y;
    }

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At("TAIL"))
    private void addMessage(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
        BetterChat.messageTimestamps.addFirst(System.currentTimeMillis());
        while (BetterChat.messageTimestamps.size() > visibleMessages.size()) {
            BetterChat.messageTimestamps.removeLast();
        }
    }
    //--------------------------------//


    @Inject(method = "addVisibleMessage", at = @At("HEAD"), cancellable = true)
    public void modifyAddMessage(ChatHudLine message, CallbackInfo ci) {
        ci.cancel();

        int i = MathHelper.floor((double)this.getWidth() / this.getChatScale());
        MessageIndicator.Icon icon = message.getIcon();
        if (icon != null) {
            i -= icon.width + 4 + 2;
        }

        List<OrderedText> list = ChatMessages.breakRenderedChatMessageLines(message.content(), i, this.client.textRenderer);
        boolean bl = this.isChatFocused();

        for(int j = 0; j < list.size(); ++j) {
            OrderedText orderedText = list.get(j);
            if (bl && this.scrolledLines > 0) {
                this.hasUnreadNewMessages = true;
                this.scroll(1);
            }

            boolean bl2 = j == list.size() - 1;
            this.visibleMessages.addFirst(new ChatHudLine.Visible(message.creationTick(), orderedText, message.indicator(), bl2));
        }

        while(this.visibleMessages.size() > getMaxChatSize()) {
            this.visibleMessages.removeLast();
        }
    }

    @Unique
    public int getMaxChatSize() {
        if (Client.inited && ModuleList.moreChatHistory.isEnabled()) return ModuleList.moreChatHistory.size.getValue().intValue();
        else return 100;
    }
}
