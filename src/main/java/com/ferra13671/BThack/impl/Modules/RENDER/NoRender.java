package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

@ModuleInfo(name = "NoRender", description = "lang.module.NoRender", category = "RENDER")
public class NoRender extends Module {

    public final BooleanSetting explosions = new BooleanSetting("Explosions", this, true);
    public final BooleanSetting particles = new BooleanSetting("Particles", this, false);
    public final BooleanSetting overlay = new BooleanSetting("Overlay", this, true);
    public final BooleanSetting waterFog = new BooleanSetting("Water Fog", this, true);
    public final BooleanSetting lavaFog = new BooleanSetting("Lava Fog", this, true);
    public final BooleanSetting powderSnowFog = new BooleanSetting("PowderSnow Fog", this, true);
    public final BooleanSetting armor = new BooleanSetting("Armor", this, false);
    public final BooleanSetting totemAnimation = new BooleanSetting("Totem Animation", this, false);
    public final BooleanSetting nausea = new BooleanSetting("Nausea", this, true);
    public final BooleanSetting fallingBlocks = new BooleanSetting("Falling Blocks", this, true);
    public final BooleanSetting armorStands = new BooleanSetting("Armor Stands", this, false);
    public final BooleanSetting textureRotations = new BooleanSetting("Texture Rotations", this, false);

    public final BooleanSetting chestRender = new BooleanSetting("Chest Render", this, false);
    public final NumberSetting chestRadius = new NumberSetting("CRender Range", this, 10, 5, 50, false, chestRender::getValue);

    public final BooleanSetting shulkerRender = new BooleanSetting("Shulker Render", this, false);
    public final NumberSetting shulkerRadius = new NumberSetting("SRender Range", this, 20, 5, 50, false, shulkerRender::getValue);

    public final BooleanSetting eTableRender = new BooleanSetting("ETable Render", this, false);
    public final NumberSetting eTableRadius = new NumberSetting("ETRender Range", this, 10, 5, 50, false, eTableRender::getValue);


    @Override
    public void onChangeSetting(Setting<?> setting) {
        if (setting == textureRotations) mc.worldRenderer.reload();
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onPacketReceive(PacketEvent.Receive e) {
        if (nullCheck()) return;

        if (explosions.getValue() && e.getPacket() instanceof ExplosionS2CPacket packet) {
            mc.world.playSound(mc.player ,packet.getX(), packet.getY(), packet.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 1, 1);
            e.setCancelled(true);
        }
    }
}
