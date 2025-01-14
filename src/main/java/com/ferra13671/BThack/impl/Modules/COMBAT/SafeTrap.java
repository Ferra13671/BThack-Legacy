package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BTbot.api.Utils.Motion.Align.AlignWithXZ;
import com.ferra13671.BTbot.api.Utils.Motion.Align.WhereToAlign;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.Build.BuildThread3D;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;

public class SafeTrap extends Module {

    public final NumberSetting minHealth = new NumberSetting("Min Health", this, 7, 1, 15, false);
    public final BooleanSetting checkObsidian = new BooleanSetting("Check Obsidian", this, false);


    public SafeTrap() {
        super("SafeTrap",
                "lang.module.SafeTrap",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

        initSettings(
                minHealth,
                checkObsidian
        );
    }

    private final ArrayList<Vec3d> trapVectors = new ArrayList<>(Arrays.asList(
            new Vec3d(1,0,0),
            new Vec3d(-1,0,0),
            new Vec3d(0,0,1),
            new Vec3d(0,0,-1),

            new Vec3d(1,1,0),
            new Vec3d(-1,1,0),
            new Vec3d(0,1,1),
            new Vec3d(0,1,-1),

            new Vec3d(1,2,0),
            new Vec3d(-1,2,0),
            new Vec3d(0,2,1),
            new Vec3d(0,2,-1),

            new Vec3d(1,3,0),
            new Vec3d(0,3,0)
    ));

    private boolean isBlocksFinded;
    private boolean startBuild = false;

    @EventSubscriber
    public void onClientTick(ClientTickEvent e) {
        if (nullCheck()) return;

        arrayListInfo = "" + minHealth.getValue();

        if (mc.player.getHealth() <=minHealth.getValue() && mc.player.verticalCollision && !BuildManager.isBuilding) {
            if (startBuild) {
                sendNotification(LanguageSystem.translate("lang.module.SafeTrap.built"));
                toggle();
                startBuild = false;
                return;
            }

            BlockPos blockPos = BlockPos.ofFloored(new Vec3d(mc.player.getX(), mc.player.getY() - 0.1, mc.player.getZ()));

            if (checkObsidian.getValue()) {
                if (mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                    sendError(Formatting.YELLOW + getChatName() + LanguageSystem.translate("lang.module.Surround.unSafeBlock"));
                    return;
                }
            }

            WhereToAlign whereToAlign = new WhereToAlign();
            AlignWithXZ alignWithXZ = new AlignWithXZ(whereToAlign);

            alignWithXZ.alignWithXZ();

            isBlocksFinded = false;

            findObsidian();

            if (!isBlocksFinded) {
                return;
            }

            BuildThread3D thread = new BuildThread3D();

            thread.set3DSchematic(1, trapVectors, blockPos);
            thread.start();
            startBuild = true;
        }
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
