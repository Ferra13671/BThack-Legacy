package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Client.Client;
import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.util.Formatting;

@ModuleInfo(name = "Auto2FA", description = "lang.module.Auto2FA", category = "MISC")
public class Auto2FA extends Module {

    public final BooleanSetting autoToggle = new BooleanSetting("AutoToggle", this, false);
    public final NumberSetting delay = new NumberSetting("Delay", this, 5000, 5000, 10000, true);
    public final BooleanSetting antiFake = new BooleanSetting("AntiFake", this, true);


    @Override
    public void onEnable() {
        super.onEnable();
        ChatUtils.sendMessage(Formatting.GRAY + "Use: " + Client.clientInfo.getChatPrefix() + "auto2FA");
    }

    @EventSubscriber
    public void onPacketReceive(PacketEvent.Receive e) {
        if (e.getPacket() instanceof GameMessageS2CPacket packet) {
            String text = packet.content().getString().toLowerCase();

            if (text.contains("/2fa")) {
                String code = Managers.TWOFA_MANAGER.getCode(mc.getSession().getUsername());
                if (code == null) return;
                if (antiFake.getValue())
                    if (ChatUtils.isNotServerMessage(text)) return;
                sendCommandAction("/2fa " + code);
            }
        }
    }

    public void sendCommandAction(String command) {
        ThreadManager.startNewThread(thread -> {
            thread.sleepThread(delay.getValue().longValue());
            ChatUtils.sendCommand(command);
            if (autoToggle.getValue())
                setToggled(false);
        });
    }
}
