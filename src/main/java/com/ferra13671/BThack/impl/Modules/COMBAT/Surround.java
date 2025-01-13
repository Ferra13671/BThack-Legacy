package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BTbot.api.Utils.Motion.Align.AlignWithXZ;
import com.ferra13671.BTbot.api.Utils.Motion.Align.WhereToAlign;
import com.ferra13671.BThack.api.Managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.Build.BuildThread3D;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.ModifyBlockPos;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.Arrays;

public class Surround extends Module {

    public final BooleanSetting checkObsidian = new BooleanSetting("Check Obsidian", this, false);
    public final NumberSetting tickDelay = new NumberSetting("Tick Delay", this, 0, 0, 3, true);

    public Surround() {
        super("Surround",
                "lang.module.Surround",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

        initSettings(
                checkObsidian,
                tickDelay
        );
    }

    private final ArrayList<Vec3i> surroundVectors = new ArrayList<>(Arrays.asList(
            new Vec3i(1,0,0),
            new Vec3i(-1,0,0),
            new Vec3i(0,0,1),
            new Vec3i(0,0,-1),

            new Vec3i(1,1,0),
            new Vec3i(-1,1,0),
            new Vec3i(0,1,1),
            new Vec3i(0,1,-1)
    ));


    private boolean isBlocksFinded = false;

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        if (!mc.player.verticalCollision) {
            sendError(Formatting.YELLOW + LanguageSystem.translate("lang.module.AutoBuilder.notStandingOnGround"));
            toggle();
            return;
        }

        if (BuildManager.isBuilding) {
            sendError(Formatting.YELLOW + LanguageSystem.translate("lang.module.AutoBuilder.alreadyBuilding"));
            toggle();
            return;
        }

        BlockPos blockPos = new ModifyBlockPos(new Vec3d(mc.player.getX(), mc.player.getY() - 0.1, mc.player.getZ()));

        if (checkObsidian.getValue()) {
            if (mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN || mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK) {
                sendError(Formatting.YELLOW + LanguageSystem.translate("lang.module.Surround.unSafeBlock"));
                toggle();
                return;
            }
        }

        WhereToAlign whereToAlign = new WhereToAlign();
        AlignWithXZ alignWithXZ = new AlignWithXZ(whereToAlign);

        alignWithXZ.alignWithXZ();



        isBlocksFinded = false;

        if (!(mc.player.getMainHandStack().getItem() instanceof BlockItem block)) {
            findObsidian();

            if (!isBlocksFinded)
                return;
        } else {

            if (block.getBlock() != Blocks.OBSIDIAN) {
                findObsidian();

                if (!isBlocksFinded)
                    return;
            }
        }

        BuildThread3D thread = new BuildThread3D();

        thread.set3DSchematic((int) tickDelay.getValue(), surroundVectors, blockPos);
        thread.start();
        toggle();
    }




    private void sendError(String cause) {
        ChatUtils.sendMessage(cause, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);
    }

    private void findObsidian() {
        for (int needSlot = 0; needSlot < 36; needSlot++) {
            Item item = mc.player.getInventory().getStack(needSlot).getItem();
            if (item instanceof BlockItem block) {

                if (block.getBlock() == Blocks.OBSIDIAN) {

                    if (needSlot < 9) {
                        InventoryUtils.swapItem(needSlot);

                    } else {
                        int slot = InventoryUtils.findFreeHotbarSlot();
                        if (slot != -1) {
                            InventoryUtils.swapItemOnInventory(slot, needSlot);
                            InventoryUtils.swapItem(slot);
                        } else {
                            InventoryUtils.swapItemOnInventory(mc.player.getInventory().selectedSlot, needSlot);
                        }
                    }
                    isBlocksFinded = true;
                    break;
                }
            }
        }

        if (!isBlocksFinded) {
            sendError(LanguageSystem.translate("lang.module.Scaffold.noBlocks"));
            toggle();
        }
    }
}
