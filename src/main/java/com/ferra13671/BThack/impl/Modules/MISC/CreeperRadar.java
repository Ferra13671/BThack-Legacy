package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class CreeperRadar extends Module {

    public final NumberSetting range = new NumberSetting("Range", this, 20, 10, 50, false);

    public CreeperRadar() {
        super("CreeperRadar",
                "lang.module.CreeperRadar",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        initSettings(
                range
        );
    }

    private static final ArrayList<Entity> reportedCreepers = new ArrayList<>();

    @EventSubscriber
    public void onWorldRender(ClientTickEvent e) {
        if (nullCheck()) return;

        List<Entity> entities = new ArrayList<>();

        for (Entity entity : mc.world.getEntities()) {
            entities.add(entity);
            if (entity instanceof CreeperEntity && !reportedCreepers.contains(entity) && entity.distanceTo(mc.player) < range.getValue()) {
                String text = Formatting.GOLD + LanguageSystem.translate("lang.module.CreeperRadar.spotted");

                ChatUtils.sendMessage(text, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);

                reportedCreepers.add(entity);
            }
        }

        reportedCreepers.removeIf(creeper -> !entities.contains(creeper));
    }
}
