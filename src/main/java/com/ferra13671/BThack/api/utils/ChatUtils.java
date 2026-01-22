package com.ferra13671.BThack.api.utils;

import com.ferra13671.BThack.Constants;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;

public final class ChatUtils implements Mc {

    public static void sendMessage(String message) {
        if (mc.player == null) return;
        mc.player.sendMessage(Text.literal(Constants.BTHACK_PREFIX + message), false);

    }

    public static void sendMessage(String msg, SoundEvent soundEvent) {
        if (mc.player == null) return;
        mc.player.sendMessage(Text.literal(Constants.BTHACK_PREFIX + msg), false);
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

    /**
     * Checks if this message is fake(Written by a player/personal message)
     */
    public static boolean isNotServerMessage(String message) {
        if (message.contains(LanguageSystem.translate("lang.module.AutoAuth.ruWord", "RU")) || message.contains("whispers") || message.contains("whispering")) return true;
        if (mc.player != null) {
            if (mc.player.networkHandler.getPlayerList().size() > 1) {
                for (PlayerListEntry info : mc.player.networkHandler.getPlayerList()) {
                    String playerName = getPlayerName(info);
                    if (playerName.length() > 3) {
                        if (message.contains("<" + playerName + ">")) return true;
                        if (!playerName.equals(mc.getSession().getUsername()))
                            if (message.contains(playerName)) return true;
                    }
                }
            }
        }
        return false;
    }

    private static String getPlayerName(PlayerListEntry networkPlayerInfoIn) {
        return networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getString() : networkPlayerInfoIn.getProfile().getName();
    }
}
