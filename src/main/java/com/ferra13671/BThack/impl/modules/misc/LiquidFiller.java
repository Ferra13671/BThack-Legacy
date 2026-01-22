package com.ferra13671.BThack.impl.modules.misc;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.impl.place.PlaceManager;
import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.ModeSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.utils.BlockUtils;
import com.ferra13671.BThack.api.utils.InventoryUtils;
import com.ferra13671.BThack.api.utils.rotate.RotateMode;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ModuleInfo(name = "LiquidFiller", description = "lang.module.LiquidFiller", category = "MISC")
public class LiquidFiller extends Module {

    public final NumberSetting range = new NumberSetting("Range", this, 3, 3, 7, false);
    public final NumberSetting placePerTick = new NumberSetting("Place per tick", this, 1, 1, 5, true);
    public final BooleanSetting water = new BooleanSetting("Water", this, true);
    public final BooleanSetting lava = new BooleanSetting("Lava", this, true);
    public final BooleanSetting other = new BooleanSetting("Other (Mods)", this, true);

    public final BooleanSetting rotate = new BooleanSetting("Rotate", this, false);
    public final ModeSetting rotateMode = new ModeSetting("Rotate Mode", this, Arrays.asList("Packet", "Grim"), rotate::getValue).defaultValue("Grim");
    public final BooleanSetting ignoreWalls = new BooleanSetting("Ignore Walls", this, true);

    public final ModeSetting swap = new ModeSetting("Swap", this, Arrays.asList("Client", "Packet"));


    @EventSubscriber
    @SuppressWarnings("unused")
    public void onTick(ClientTickEvent e) {
        if (nullCheck() || PlaceManager.isBuilding) return;

        List<Vec3i> sch = new ArrayList<>();

        filterAction(sch);
        interactAction(sch);
    }

    @SuppressWarnings("DataFlowIssue")
    public void filterAction(List<Vec3i> sch) {
        for (BlockPos pos : BlockUtils.getSphere(BlockPos.ofFloored(mc.player.getX(), mc.player.getY(), mc.player.getZ()), range.getValue().floatValue(), range.getValue().floatValue(), false, true, 0)) {
            Block block = mc.world.getBlockState(pos).getBlock();

            if (!ignoreWalls.getValue())
                if (!BlockUtils.hasLineOfSight(mc.player.getPos(), pos.toCenterPos())) continue;

            if (water.getValue() && block == Blocks.WATER) sch.add(pos);
            else if (lava.getValue() && block == Blocks.LAVA) sch.add(pos);
            else if (other.getValue() && block instanceof FluidBlock) sch.add(pos);
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public void interactAction(List<Vec3i> sch) {
        int places = 0;
        for (Vec3i pos : sch) {
            if (places >= placePerTick.getValue().intValue()) break;
            int slot = InventoryUtils.findItem(BlockItem.class);
            if (slot == -1) return;
            if (mc.player.getInventory().getStack(mc.player.getInventory().selectedSlot).getItem() instanceof BlockItem) slot = mc.player.getInventory().selectedSlot;
            int oldSlot = mc.player.getInventory().selectedSlot;

            InventoryUtils.swapAction(oldSlot, slot, false, swap.getValue());
            PlaceManager.placeBlock(new BlockPos(pos), RotateMode.valueOf(rotateMode.getValue().toUpperCase()));
            InventoryUtils.swapAction(oldSlot, slot, true, swap.getValue());
            places++;
        }
    }
}
