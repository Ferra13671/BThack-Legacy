package com.ferra13671.BThack.impl.Modules.Misc;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

import java.util.Arrays;
import java.util.function.Predicate;

@ModuleInfo(name = "AutoShear", description = "lang.module.AutoShear", category = "MISC")
public class AutoShear extends Module {

    public final BooleanSetting rotate = new BooleanSetting("Rotate", this, false);
    public final ModeSetting rotateMode = new ModeSetting("Rotate Mode", this, Arrays.asList("Grim", "Packet"), rotate::getValue);
    public final ModeSetting swap = new ModeSetting("Swap", this, Arrays.asList("Packet", "Client"));


    private Predicate<Entity> filter;

    @Override
    public void onEnable() {
        super.onEnable();

        filter = entity -> entity instanceof SheepEntity sheep && !sheep.isBaby() && !sheep.isSheared();
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        Entity entity = KillAuraUtils.filterEntity(4, filter);
        if (entity == null) return;

        int slot = InventoryUtils.findItem(Items.SHEARS);
        if (slot != -1) {
            int oldSlot = mc.player.getInventory().selectedSlot;

            InventoryUtils.swapAction(oldSlot, slot, false, swap.getValue());
            rotatePre(entity);
            mc.interactionManager.interactEntity(mc.player, entity, Hand.MAIN_HAND);
            rotatePost();
            InventoryUtils.swapAction(oldSlot, slot, true, swap.getValue());
        }
    }

    public void rotatePre(Entity entity) {
        float[] rots = RotateUtils.rotations(entity);
        if (rotate.getValue()) {
            switch (rotateMode.getValue()) {
                case "Grim" -> GrimUtils.sendPreActionGrimPackets(rots[0], rots[1]);
                case "Packet" -> RotateUtils.packetRotate(rots[0], rots[1]);
            }
        }
    }

    public void rotatePost() {
        if (rotate.getValue())
            if (rotateMode.getValue().equals("Grim")) GrimUtils.sendPostActionGrimPackets();
    }
}
