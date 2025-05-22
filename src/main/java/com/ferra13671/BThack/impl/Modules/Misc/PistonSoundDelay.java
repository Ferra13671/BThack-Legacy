package com.ferra13671.BThack.impl.Modules.Misc;


import com.ferra13671.BThack.events.SoundPlayEvent;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.sound.SoundEvents;

@ModuleInfo(name = "PistonSoundDelay", description = "lang.module.PistonSoundDelay", category = "MISC")
public class PistonSoundDelay extends Module {

    public final NumberSetting soundDelay = new NumberSetting("Sound Delay", this, 5, 1,15,true);


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
