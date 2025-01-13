package com.ferra13671.BThack.api.SoundSystem;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class Sound {
    private final SoundEvent soundEvent;

    public Sound(String name) {
        this("bthack", name);
    }

    public Sound(String nameSpace, String name) {
        Identifier identifier = Identifier.of(nameSpace + ":" + name);
        soundEvent = SoundEvent.of(identifier);
        Registry.register(Registries.SOUND_EVENT, identifier, soundEvent);
    }

    public SoundEvent getSoundEvent() {
        return soundEvent;
    }
}
