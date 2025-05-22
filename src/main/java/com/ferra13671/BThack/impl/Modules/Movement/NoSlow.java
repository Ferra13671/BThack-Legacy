package com.ferra13671.BThack.impl.Modules.Movement;

import com.ferra13671.BThack.events.Entity.UpdateInputEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;

import java.util.Arrays;

@ModuleInfo(name = "NoSlow", description = "lang.module.NoSlow", category = "MOVEMENT")
public class NoSlow extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("Default", "Grim V2", "Grim V3"));
    public final BooleanSetting useItems = new BooleanSetting("Use Items", this, true);

    public final BooleanSetting soulSand = new BooleanSetting("SoulSand", this, false);
    public final BooleanSetting slime = new BooleanSetting("Slime", this, false);
    public final BooleanSetting honey = new BooleanSetting("Honey", this, false);


    Int2ObjectOpenHashMap<ItemStack> int2ObjectMap = new Int2ObjectOpenHashMap<>();
    @EventSubscriber
    public void onInput(UpdateInputEvent e) {
        if (mode.getValue().equals("Grim V3")) {
            if(mc.player.isUsingItem()){
                if(mc.player.getItemUseTime() <= 5) {
                    Managers.NETWORK_MANAGER.sendPacket(new ClickSlotC2SPacket(mc.player.currentScreenHandler.syncId, 0, 1, 0, SlotActionType.PICKUP, ItemStack.EMPTY, int2ObjectMap));
                } else {
                    mc.player.input.movementSideways *= 5;
                    mc.player.input.movementForward *= 5;
                }
            }
        }
    }
}
