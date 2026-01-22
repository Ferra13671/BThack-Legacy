package com.ferra13671.BThack.impl.modules.misc;

import com.ferra13671.BThack.events.GuiOpenEvent;
import com.ferra13671.BThack.events.PacketEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.mixins.accessor.IAbstractSignEditScreen;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;

@ModuleInfo(name = "AutoSign", description = "lang.module.AutoSign", category = "MISC")
public class AutoSign extends Module {

    public final BooleanSetting closeScreen = new BooleanSetting("Close Screen", this, true);


    private String[] text;

    @Override
    public void onDisable() {
        super.onDisable();
        text = null;
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onPacketSend(PacketEvent.Send e) {
        if (e.getPacket() instanceof UpdateSignC2SPacket packet) {
            text = packet.getText();
        }
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onOpenScreen(GuiOpenEvent e) {
        if (!(e.getScreen() instanceof AbstractSignEditScreen) || text == null) return;

        SignBlockEntity sign = ((IAbstractSignEditScreen) e.getScreen()).getSign();

        Managers.NETWORK_MANAGER.sendPacket(new UpdateSignC2SPacket(sign.getPos(), true, text[0], text[1], text[2], text[3]));

        if (closeScreen.getValue())
            e.setCancelled(true);
    }
}
