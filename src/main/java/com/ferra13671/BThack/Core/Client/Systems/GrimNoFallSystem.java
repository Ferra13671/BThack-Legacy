package com.ferra13671.BThack.Core.Client.Systems;

import com.ferra13671.BThack.api.Events.DisconnectEvent;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class GrimNoFallSystem {
    private static int takenFallDamage = 0;


    @EventSubscriber
    public void onDisconnect(DisconnectEvent e) {
        takenFallDamage = 0;
    }

    public static void updateFallDamage() {
        if (takenFallDamage < 5) {
            takenFallDamage++;
            ChatUtils.sendMessage(String.format("[Grim NoFall] Fall damage taken %s times", takenFallDamage));
        }
    }

    public static int getTakenFallDamage() {
        return takenFallDamage;
    }
}
