package com.ferra13671.BThack.api.Utils;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.core.DeviceSystem;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.managers.Thread.ThreadManager;
import com.ferra13671.DiscordRPC.DiscordEventHandlers;
import com.ferra13671.DiscordRPC.DiscordRPC;
import com.ferra13671.DiscordRPC.DiscordRichPresence;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;

public final class DiscordUtils implements Mc {
    private static DiscordRPC discordRPC;
    private static DiscordRichPresence discordRichPresence;

    private static String details;
    private static String state;

    private static boolean inited = false;

    public static void init() {
        if (DeviceSystem.getLaunchDevice() == DeviceSystem.LaunchDevice.PHONE) return;

        try {
            discordRPC = DiscordRPC.INSTANCE;
            discordRichPresence = new DiscordRichPresence();
            inited = true;
        } catch (Exception ignored) {
            inited = false;
        }
    }

    public static void startup() {
        if (!inited) return;
        BThack.log("Discord RPC is starting up!");

        DiscordEventHandlers handlers = new DiscordEventHandlers();

        discordRPC.Discord_Initialize(Constants.BTHACK_APP_ID, handlers, true, "");

        String imageKey = "bthack_icon";

        if (ModuleList.discordRPC.secret.getValue()) {
            double percent = MathUtils.randomInt(1, 100) / 10d;
            if (percent < 5)
                imageKey = "hentai_face" + MathUtils.randomInt(1, 3);
        }

        discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        discordRichPresence.largeImageKey = imageKey;
        discordRichPresence.largeImageText = "BThack client";

        discordRPC.Discord_UpdatePresence(discordRichPresence);

        ThreadManager.startNewThread("RPC-Callback-Handler", thread -> {
            while (!thread.isInterrupted()) {
                try {
                    details = "Version " + BThack.VERSION;
                    state = "Main Menu";

                    if (mc.isIntegratedServerRunning()) {
                        state = "In singleplayer";
                    } else if (mc.currentScreen instanceof MultiplayerScreen) {
                        state = "Multiplayer Menu";
                    } else if (mc.currentScreen instanceof SelectWorldScreen) {
                        state = "Singleplayer Menu";
                    } else if (mc.getCurrentServerEntry() != null) {
                        state = "Server | " + mc.getCurrentServerEntry().address.toLowerCase();
                    }

                    discordRichPresence.details = details;
                    discordRichPresence.state = state;

                    discordRPC.Discord_UpdatePresence(discordRichPresence);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                thread.sleepThread(1000);
            }
        });
    }

    public static void shutdown() {
        if (!inited) return;
        BThack.log("Discord RPC is shutting down!");

        discordRPC.Discord_Shutdown();
        discordRPC.Discord_ClearPresence();
    }
}
