package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.Build.BuildThread3D;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ferra13671 and Nikitadan4pi
 */

public class Scaffold extends Module {

    public final BooleanSetting keepY = new BooleanSetting ("Keep Y", this, false);
    public final ModeSetting switchMode = new ModeSetting("Switch Mode", this, Arrays.asList("Normal", "Logic"));

    public final BooleanSetting extraWidth = new BooleanSetting("Extra Width", this, false);

    public Scaffold() {
        super("Scaffold",
                "lang.module.Scaffold",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        initSettings(
                keepY,
                switchMode,

                extraWidth
        );
    }

    private double yFlag;
    private BlockPos oldPos;


    @Override
    public void onEnable() {
        super.onEnable();
        yFlag = 0;
        oldPos = null;
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        action();
    }

    public void action() {
        if (!BuildManager.pickUpPlaceBlocks(false) || BuildManager.isBuilding) return;


        BlockPos blockPos;
        if (keepY.getValue()){
            if (yFlag == 0){
                yFlag = mc.player.getY() - 1;
            }
            blockPos = BlockPos.ofFloored(mc.player.getX(), yFlag, mc.player.getZ());
        }
        else {
            blockPos = BlockPos.ofFloored(mc.player.getX(), mc.player.getY() - 1, mc.player.getZ());
        }
        if (!mc.world.getBlockState(blockPos).isReplaceable()) return;

        if (oldPos != null && switchMode.getValue().equals("Logic")) {
            Block block = mc.world.getBlockState(oldPos).getBlock();
            int slot = InventoryUtils.findItem(block.asItem());
            if (slot != -1) {
                if (slot < 9) InventoryUtils.swapItem(slot);
                else {
                    int freeSlot = InventoryUtils.findFreeHotbarSlot();
                    if (freeSlot == -1) freeSlot = mc.player.getInventory().selectedSlot;
                    InventoryUtils.swapItemOnInventory(freeSlot, slot);
                    InventoryUtils.swapItem(freeSlot);
                }
            }
        }


        oldPos = blockPos;

        BuildThread3D thread3D = new BuildThread3D();
        thread3D.set3DSchematic(0 , getSchematic(), blockPos);
        thread3D.start();
    }

    public List<Vec3i> getSchematic() {
        if (extraWidth.getValue()) return Arrays.asList(new Vec3i(0, 0, 0),
                new Vec3i(-1, 0, 0),
                new Vec3i(-1, 0, -1),
                new Vec3i(1, 0, -1),
                new Vec3i(-1, 0, 1),
                new Vec3i(1, 0, 1),
                new Vec3i(0, 0, -1),
                new Vec3i(1, 0, 0),
                new Vec3i(0, 0, 1));
        else return Arrays.asList(new Vec3i(0, 0, 0));
    }
}