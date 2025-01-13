package com.ferra13671.BThack.api.SoundSystem;

import com.ferra13671.BThack.api.Interfaces.Mc;
import net.minecraft.sound.SoundCategory;

public final class SoundSystem implements Mc {
    public static void playSound(Sound sound) {
        playSound(sound, 1);
    }

    public static void playSound(Sound sound, float volume) {
        playSound(sound, 1, volume);
    }

    public static void playSound(Sound sound, float pitch, float volume) {
        if (mc.player != null && mc.world != null)
            mc.world.playSound(mc.player, mc.player.getBlockPos(), sound.getSoundEvent(), SoundCategory.MASTER, volume, pitch);
        /*
        if (mc.world == null || mc.player == null) {
            mc.getSoundManager().reloadSounds();
            ThreadManager.startNewThread(thread ->
                    mc.getSoundManager().play(PositionedSoundInstance.master(sound.getSoundEvent(), pitch, volume))
            );
        } else
            mc.player.playSound(sound.getSoundEvent(), pitch, volume);

         */
    }
}
