package com.ferra13671.BThack.impl.Modules.Movement;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Client.Systems.GrimNoFallSystem;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.events.PacketEvent;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.mixins.accessor.entity.ILivingEntity;
import com.ferra13671.BThack.mixins.accessor.packet.IPlayerMoveC2SPacket;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.util.Arrays;

/*
For Grim NoFall to work, you need to take fall damage 5 times when NoFall enabled
(you only need to do this 1 time during the entire session on the server. Only after
 reconnecting the server you need to do it again).

I never understood why 5, but the most important thing is that it works.
 */
@ModuleInfo(name = "NoFall", description = "lang.module.NoFall", category = "MOVEMENT")
public class NoFall extends Module {

    public ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("Grim", "Default"));


    private boolean started = false;
    private boolean skipTick = true;

    private boolean sentMessage = false;

    @Override
    public void onChangeSetting(Setting<?> setting) {
        if (setting == mode && mode.getValue().equals("Grim") && GrimNoFallSystem.getTakenFallDamage() < 1)
            ChatUtils.sendMessage(LanguageSystem.translate("lang.module.NoFall.message"));
    }

    @EventSubscriber
    public void onPacketSend(PacketEvent.Send e) {
        if (e.getPacket() instanceof PlayerMoveC2SPacket packet) {
            if (mode.getValue().equals("Default"))
                ((IPlayerMoveC2SPacket) packet).setOnGround(true);
            else if (started)
                ((IPlayerMoveC2SPacket) packet).setOnGround(false);
        }
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) {
            started = false;
            skipTick = true;
            sentMessage = false;
            return;
        } else if (!sentMessage && GrimNoFallSystem.getTakenFallDamage() < 1) {
            ChatUtils.sendMessage(LanguageSystem.translate("lang.module.NoFall.message"));
            sentMessage = true;
        }
        if (!mc.player.isOnGround() && mc.player.fallDistance > 3 && !started)
            started = true;

        if (started) {
            mc.options.jumpKey.setPressed(false);
            if (mc.player.isOnGround()) {
                if (skipTick) {
                    skipTick = false;
                    return;
                }
                mc.player.jump();
                ((ILivingEntity) mc.player).setJumpingCooldown(10);
                started = false;
                skipTick = true;
                GrimNoFallSystem.updateFallDamage();
            }
        }
    }
}
