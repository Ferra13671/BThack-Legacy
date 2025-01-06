package com.ferra13671.BThack.mixins;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.DisconnectEvent;
import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.IMixin.ModifyClientConnection;
import com.ferra13671.BThack.impl.Modules.MISC.NoPacketKick;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.OffThreadException;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.RejectedExecutionException;

@Mixin(value = ClientConnection.class, priority = Integer.MAX_VALUE)
public abstract class MixinClientConnection implements ModifyClientConnection {

    @Shadow private int packetsSentCounter;

    @Shadow private Channel channel;

    @Shadow protected abstract void sendInternal(Packet<?> packet, @Nullable PacketCallbacks callbacks, boolean flush);

    @Shadow @Nullable private volatile PacketListener packetListener;

    @Shadow @Final private static Logger LOGGER;

    @Shadow public abstract void disconnect(Text disconnectReason);

    @Shadow
    private static <T extends PacketListener> void handlePacket(Packet<T> packet, PacketListener listener) {
    }

    @Shadow private int packetsReceivedCounter;

    @Shadow public abstract boolean isOpen();

    @Inject(method = "exceptionCaught", at = @At("HEAD"), cancellable = true)
    public void modifyExceptionCaught(ChannelHandlerContext context, Throwable ex, CallbackInfo ci) {
        if (ModuleList.noPacketKick.isEnabled()) {
            String text = "Exception caught on network thread: " + ex.getMessage();
            if (NoPacketKick.chatNotify.getValue())
                ModuleList.noPacketKick.sendNotification(text);
            BThack.log("[NoPacketKick] " + text);
        }
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void modifyChannelRend0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        ci.cancel();

        PacketEvent packetEvent = new PacketEvent.Receive(packet);
        BThack.EVENT_BUS.activate(packetEvent);
        if (packetEvent.isCancelled()) {
            return;
        }

        if (channel.isOpen()) {
            PacketListener packetListener = this.packetListener;
            if (packetListener == null) {
                throw new IllegalStateException("Received a packet before the packet listener was initialized");
            } else {
                if (packetListener.accepts(packetEvent.getPacket())) {
                    try {
                        handlePacket(packetEvent.getPacket(), packetListener);
                    } catch (OffThreadException var5) {
                    } catch (RejectedExecutionException var6) {
                        disconnect(Text.translatable("multiplayer.disconnect.server_shutdown"));
                    } catch (ClassCastException var7) {
                        ClassCastException classCastException = var7;
                        LOGGER.error("Received {} that couldn't be processed", packetEvent.getPacket().getClass(), classCastException);
                        disconnect(Text.translatable("multiplayer.disconnect.invalid_packet"));
                    }

                    ++packetsReceivedCounter;
                }

            }
        }
    }

    @Inject(method = "sendImmediately", at = @At("HEAD"), cancellable = true)
    public void modifySendImmediately(Packet<?> packet, PacketCallbacks callbacks, boolean flush, CallbackInfo ci) {
        ci.cancel();

        PacketEvent packetEvent = new PacketEvent.Send(packet);
        BThack.EVENT_BUS.activate(packetEvent);
        if (packetEvent.isCancelled()) {
            return;
        }
        ++packetsSentCounter;
        if (channel.eventLoop().inEventLoop()) {
            sendInternal(packetEvent.getPacket(), callbacks, flush);
        } else {
            channel.eventLoop().execute(() -> {
                sendInternal(packetEvent.getPacket(), callbacks, flush);
            });
        }
    }

    @Override
    public void sendPacketNoEvent(Packet<?> packet) {
        ++packetsSentCounter;
        if (channel.eventLoop().inEventLoop()) {
            sendInternal(packet, null, false);
        } else {
            channel.eventLoop().execute(() -> {
                sendInternal(packet, null, false);
            });
        }
    }

    @Inject(method = "disconnect", at = @At(value = "HEAD"))
    private void hookDisconnect(Text disconnectReason, CallbackInfo ci) {
        BThack.EVENT_BUS.activate(new DisconnectEvent());
    }
}
