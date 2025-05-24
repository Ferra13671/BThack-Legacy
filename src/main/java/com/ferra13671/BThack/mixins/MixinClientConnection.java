package com.ferra13671.BThack.mixins;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.events.DisconnectEvent;
import com.ferra13671.BThack.events.PacketEvent;
import com.ferra13671.BThack.api.IMixin.ModifyClientConnection;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientConnection.class, priority = Integer.MAX_VALUE)
public abstract class MixinClientConnection implements ModifyClientConnection {

    @Shadow private int packetsSentCounter;

    @Shadow private Channel channel;

    @Shadow protected abstract void sendInternal(Packet<?> packet, @Nullable PacketCallbacks callbacks, boolean flush);

    @Shadow public abstract boolean isOpen();


    @Unique PacketEvent lastReceiveEvent;
    @Unique PacketEvent lastSendEvent;

    @Inject(method = "exceptionCaught", at = @At("HEAD"))
    public void modifyExceptionCaught(ChannelHandlerContext context, Throwable ex, CallbackInfo ci) {
        if (ModuleList.noPacketKick.isEnabled()) {
            String text = "Exception caught on network thread: " + ex.getMessage();
            if (ModuleList.noPacketKick.chatNotify.getValue())
                ModuleList.noPacketKick.sendNotification(text);
            BThack.log("[NoPacketKick] " + text);
        }
    }

    @ModifyVariable(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public Packet<?> modifyChannelRead01(Packet<?> packet) {
        PacketEvent packetEvent = new PacketEvent.Receive(packet);
        BThack.EVENT_BUS.activate(packetEvent);
        lastReceiveEvent = packetEvent;
        return packetEvent.getPacket();
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void modifyChannelRead02(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        if (lastReceiveEvent != null) {
            if (lastReceiveEvent.isCancelled()) ci.cancel();
            lastReceiveEvent = null;
        }
    }

    @ModifyVariable(method = "sendImmediately", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public Packet<?> modifySendImmediately1(Packet<?> packet) {
        PacketEvent packetEvent = new PacketEvent.Send(packet);
        BThack.EVENT_BUS.activate(packetEvent);
        lastSendEvent = packetEvent;
        return packetEvent.getPacket();
    }

    @Inject(method = "sendImmediately", at = @At("HEAD"), cancellable = true)
    public void modifySendImmediately2(Packet<?> packet, PacketCallbacks callbacks, boolean flush, CallbackInfo ci) {
        if (lastSendEvent != null) {
            if (lastSendEvent.isCancelled()) ci.cancel();
            lastSendEvent = null;
        }
    }

    @SuppressWarnings({"AddedMixinMembersNamePattern", "resource"})
    @Override
    public void sendPacketNoEvent(Packet<?> packet) {
        ++packetsSentCounter;
        if (channel.eventLoop().inEventLoop()) {
            sendInternal(packet, null, false);
        } else {
            channel.eventLoop().execute(() -> sendInternal(packet, null, false));
        }
    }

    @Inject(method = "disconnect*", at = @At(value = "HEAD"))
    private void hookDisconnect(Text disconnectReason, CallbackInfo ci) {
        BThack.EVENT_BUS.activate(new DisconnectEvent());
    }
}
