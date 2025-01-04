package com.ferra13671.BThack.api.Events;

import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

public class SoundPlayEvent extends Event {
    public double x;
    public double y;
    public double z;
    public SoundEvent soundEvent;
    public SoundCategory soundCategory;
    public float volume;
    public float pitch;
    public boolean useDistance;

    public SoundPlayEvent(double x, double y, double z, SoundEvent soundEvent, SoundCategory soundCategory, float volume, float pitch, boolean useDistance) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.soundEvent = soundEvent;
        this.soundCategory = soundCategory;
        this.volume = volume;
        this.pitch = pitch;
        this.useDistance = useDistance;
    }
}
