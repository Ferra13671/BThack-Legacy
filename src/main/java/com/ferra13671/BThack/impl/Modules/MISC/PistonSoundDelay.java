package com.ferra13671.BThack.impl.Modules.MISC;


import com.ferra13671.BThack.api.Events.SoundPlayEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.sound.SoundEvents;

public class PistonSoundDelay extends Module {

    public final NumberSetting soundDelay = new NumberSetting("Sound Delay", this, 5, 1,15,true);

    public PistonSoundDelay() {
        super("PistonSoundDelay",
                "lang.module.PistonSoundDelay",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        initSettings(
                soundDelay
        );
    }

    private long delay = 0;

    @EventSubscriber
    public void onSound(SoundPlayEvent e) {
        if (e.soundEvent == SoundEvents.BLOCK_PISTON_EXTEND || e.soundEvent == SoundEvents.BLOCK_PISTON_CONTRACT) {
            if (delay < soundDelay.getValue()) {
                delay++;
                e.setCancelled(true);
            } else {
                delay = 0;
            }
        }
    }
}
