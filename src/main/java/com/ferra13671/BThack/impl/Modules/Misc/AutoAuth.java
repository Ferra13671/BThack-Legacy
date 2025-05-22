package com.ferra13671.BThack.impl.Modules.Misc;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Client.Client;
import com.ferra13671.BThack.events.PacketEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.managers.managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.util.Formatting;

@ModuleInfo(name = "AutoAuth", description = "lang.module.AutoAuth", category = "MISC")
public class AutoAuth extends Module {

    public final BooleanSetting autoToggle = new BooleanSetting("AutoToggle", this, false);
    public final NumberSetting delay = new NumberSetting("Delay", this, 1000, 500, 5000, true);
    public final BooleanSetting antiFake = new BooleanSetting("AntiFake", this, true);

    @Override
    public void onEnable() {
        super.onEnable();
        ChatUtils.sendMessage(Formatting.GRAY + "Use: " + Client.clientInfo.getChatPrefix() + "autoAuth");
    }

    @EventSubscriber
    public void onPacketReceive(PacketEvent.Receive e) {
        if (e.getPacket() instanceof GameMessageS2CPacket packet) {
            String text = packet.content().getString().toLowerCase();
            String password = Managers.AUTO_AUTH_MANAGER.getPassword(mc.getSession().getUsername());
            if (password == null) return;
            if (text.contains("/reg") || text.contains("/register")) {
                if (antiFake.getValue())
                    if (ChatUtils.isNotServerMessage(text)) return;
                sendCommandAction("/reg " + password);
                return;
            }
            if (text.contains("/l") || text.contains("/login")) {
                if (antiFake.getValue())
                    if (ChatUtils.isNotServerMessage(text)) return;
                sendCommandAction("/l " + password);
            }
        }
    }

    public void sendCommandAction(String command) {
        ThreadManager.startNewThread(thread -> {
            thread.sleepThread(delay.getValue().longValue());
            ChatUtils.sendCommand(command);
            if (autoToggle.getValue())
                setEnabled(false);
        });
    }

}
