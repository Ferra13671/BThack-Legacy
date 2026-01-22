package com.ferra13671.BThack.core.client.systems;

import com.ferra13671.BThack.events.DisconnectEvent;
import com.ferra13671.BThack.api.utils.ChatUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class GrimNoFallSystem {
    private static int takenFallDamage = 0;


    @EventSubscriber
    @SuppressWarnings("unused")
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
