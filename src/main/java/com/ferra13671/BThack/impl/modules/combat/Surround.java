package com.ferra13671.BThack.impl.modules.combat;

import com.ferra13671.BThack.api.utils.rotate.RotateMode;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.events.PacketEvent;
import com.ferra13671.BThack.managers.impl.place.PlaceManager;
import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.CategorySetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.utils.InventoryUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.item.BlockItem;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "Surround", description = "lang.module.Surround", category = "COMBAT")
public class Surround extends Module {

    public final NumberSetting blocksPerTick = new NumberSetting("Blocks Per Tick", this, 4, 1, 8, true);
    public final BooleanSetting extraBlocks = new BooleanSetting("Extra Blocks", this, false);
    public final BooleanSetting silentSwap = new BooleanSetting("Silent Swap", this, true);

    public final CategorySetting autoDisableCategory = new CategorySetting("Auto Disable", this);
    public final BooleanSetting autoDisable = new BooleanSetting("Auto Disable", this, true).inCategory(autoDisableCategory);
    public final BooleanSetting disableOnY = new BooleanSetting("Disable On Y", this, true, autoDisable::getValue).inCategory(autoDisableCategory);
    public final BooleanSetting disableOnTp = new BooleanSetting("Disable On Tp", this, true, autoDisable::getValue).inCategory(autoDisableCategory);
    public final BooleanSetting disableOnDeath = new BooleanSetting("Disable On Death", this, true, autoDisable::getValue).inCategory(autoDisableCategory);
    public final BooleanSetting disableIfNoBlocks = new BooleanSetting("Disable If No Blocks", this, true, autoDisable::getValue).inCategory(autoDisableCategory);


    private double prevY;

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void onEnable() {
        if (nullCheck()) toggle();

        prevY = mc.player.getY();

        if (!mc.player.verticalCollision) {
            sendNotification(Formatting.RED + LanguageSystem.translate("lang.module.AutoBuilder.notStandingOnGround"));
            toggle();
        }

        super.onEnable();
    }

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) {
            toggle();
            return;
        }

        if (autoDisable.getValue()) {
            if (disableOnY.getValue()) {
                if (prevY != mc.player.getY()) {
                    toggle();
                    return;
                }
            }
            if (disableOnDeath.getValue()) {
                if (mc.player.isDead()) {
                    toggle();
                    return;
                }
            }
            if (disableIfNoBlocks.getValue()) {
                if (!PlaceManager.pickUpPlaceBlocks(false, PlaceManager.obsidians)) {
                    sendNotification(Formatting.RED + LanguageSystem.translate("lang.module.Scaffold.noBlocks"));
                    toggle();
                    return;
                }
            }
        }

        prevY = mc.player.getY();

        if (!PlaceManager.pickUpPlaceBlocks(false, PlaceManager.obsidians)) {
            if (autoDisable.getValue() && disableIfNoBlocks.getValue()) {
                toggle();
            }
            return;
        }

        int count = 0;
        for (BlockPos blockPos : getBlockPoses()) {
            if (mc.world.isAir(blockPos)) {
                if (silentSwap.getValue()) {
                    if (PlaceManager.pickUpPlaceBlocks(false, PlaceManager.obsidians)) {
                        int slot = findSlot();
                        if (slot != -1) {
                            int oldSlot = mc.player.getInventory().selectedSlot;
                            InventoryUtils.swapAction(oldSlot, slot, false, "Client");
                            PlaceManager.placeBlock(blockPos, RotateMode.GRIM);
                            InventoryUtils.swapAction(oldSlot, slot, true, "Client");
                        }
                    }
                } else if (PlaceManager.pickUpPlaceBlocks(true, PlaceManager.obsidians)) PlaceManager.placeBlock(blockPos, RotateMode.GRIM);
                count++;
                if (count >= blocksPerTick.getValue()) break;
            }
        }
    }

    @EventSubscriber
    public void onPacketReceive(PacketEvent.Receive e) {
        if (nullCheck()) return;

        if (autoDisable.getValue() && disableOnTp.getValue() && e.getPacket() instanceof PlayerPositionLookS2CPacket)
            toggle();
    }

    @SuppressWarnings("DataFlowIssue")
    public List<BlockPos> getBlockPoses() {
        List<BlockPos> result = new ArrayList<>();
        BlockPos playerPos = BlockPos.ofFloored(mc.player.getX(), mc.player.getY(), mc.player.getZ());
        result.add(playerPos.add(-1, 0, 0));
        result.add(playerPos.add(1, 0, 0));
        result.add(playerPos.add(0, 0, 1));
        result.add(playerPos.add(0, 0, -1));
        if (extraBlocks.getValue()) {
            result.add(playerPos.add(-1, -1, 0));
            result.add(playerPos.add(1, -1, 0));
            result.add(playerPos.add(0, -1, 1));
            result.add(playerPos.add(0, -1, -1));
        }
        return result;
    }

    @SuppressWarnings("DataFlowIssue")
    public int findSlot() {
        if (mc.player.getMainHandStack().getItem() instanceof BlockItem item)
            if (PlaceManager.isNeedBlock(item.getBlock(), PlaceManager.obsidians)) return mc.player.getInventory().selectedSlot;

        for (int i = 0; i < 36; i++)
            if (mc.player.getInventory().getStack(i).getItem() instanceof BlockItem blockItem &&
                    PlaceManager.isNeedBlock(blockItem.getBlock(), PlaceManager.obsidians))
                return i;
        return -1;
    }
}
