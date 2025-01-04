package com.ferra13671.BThack.api.Utils;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.api.Interfaces.Mc;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class ChatUtils implements Mc {

    private static final String prefix = "[" + Formatting.BLUE + Client.clientInfo.getCName() + Formatting.RESET + "] ";

    public static void sendMessage(String message) {
        if (mc.player == null) return;
        mc.player.sendMessage(Text.of(prefix + message));

    }

    public static void sendMessage(String msg, SoundEvent soundEvent) {
        if (mc.player == null) return;
        mc.player.sendMessage(Text.of(prefix + msg));
        mc.player.playSound(soundEvent,1,1);
    }

    public static void sendChatMessage(String message) {
        if (mc.player == null) return;
        mc.player.networkHandler.sendChatMessage(message);
    }

    public static void sendCommand(String command) {
        if (mc.player == null) return;
        mc.player.networkHandler.sendChatCommand(command.substring(1));
    }
}
