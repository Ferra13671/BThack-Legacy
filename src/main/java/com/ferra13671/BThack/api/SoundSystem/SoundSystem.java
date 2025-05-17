package com.ferra13671.BThack.api.SoundSystem;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Module.Module;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.random.Random;

public final class SoundSystem implements Mc {
    private static Random random;

    public static void playSound(Sound sound) {
        playSound(sound, 1);
    }

    public static void playSound(Sound sound, float volume) {
        playSound(sound, 1, volume);
    }

    public static void playSound(Sound sound, float pitch, float volume) {
        if (random == null) random = SoundInstance.createRandom();
        if (Module.nullCheck())
            mc.getSoundManager().play(new PositionedSoundInstance(sound.getSoundEvent().id(), SoundCategory.MASTER, volume, pitch, random, false, 0, SoundInstance.AttenuationType.NONE, 0.0, 0.0, 0.0, true));
        else
            mc.world.playSound(mc.player, mc.player.getBlockPos(), sound.getSoundEvent(), SoundCategory.MASTER, volume, pitch);
    }
}
