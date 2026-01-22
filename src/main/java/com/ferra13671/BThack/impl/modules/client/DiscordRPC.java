package com.ferra13671.BThack.impl.modules.client;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.api.utils.MathUtils;
import com.ferra13671.BThack.core.client.ModuleList;
import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.managers.impl.thread.ThreadManager;
import com.ferra13671.DiscordRPC.DiscordEventHandlers;
import com.ferra13671.DiscordRPC.DiscordRichPresence;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;

@ModuleInfo(name = "DiscordRPC", description = "lang.module.DiscordRPC", category = "CLIENT", autoEnabled = true, allowRemapKeyCode = false)
public class DiscordRPC extends Module {

    public final BooleanSetting secret = new BooleanSetting("Secret :3", this, false);

    private final com.ferra13671.DiscordRPC.DiscordRPC discordRPC;
    private final DiscordRichPresence discordRichPresence;

    public DiscordRPC() {
        discordRPC = com.ferra13671.DiscordRPC.DiscordRPC.INSTANCE;
        discordRichPresence = new DiscordRichPresence();
    }

    @Override
    public void onEnable() {
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
                    discordRichPresence.details = "Version " + BThack.VERSION;
                    discordRichPresence.state =
                            mc.isIntegratedServerRunning() ? "In singleplayer" :
                            mc.currentScreen instanceof MultiplayerScreen ? "Multiplayer Menu" :
                            mc.currentScreen instanceof SelectWorldScreen ? "Singleplayer Menu" :
                            mc.getCurrentServerEntry() != null ? "Server | " + mc.getCurrentServerEntry().address.toLowerCase() :
                            "Main Menu";

                    discordRPC.Discord_UpdatePresence(discordRichPresence);
                } catch (Exception exception) {
                    //noinspection CallToPrintStackTrace
                    exception.printStackTrace();
                }
                thread.sleepThread(1000);
            }
        });
    }

    @Override
    public void onDisable() {
        BThack.log("Discord RPC is shutting down!");

        discordRPC.Discord_Shutdown();
        discordRPC.Discord_ClearPresence();
    }
}
