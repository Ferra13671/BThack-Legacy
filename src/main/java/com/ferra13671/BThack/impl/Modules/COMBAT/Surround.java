package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Managers.managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.item.BlockItem;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class Surround extends Module {

    public final BooleanSetting autoToggle = new BooleanSetting("Auto Toggle", this, true);
    public final BooleanSetting disableOnY = new BooleanSetting("Disable On Y", this, true, autoToggle::getValue);
    public final BooleanSetting disableOnTp = new BooleanSetting("Disable On Tp", this, true, autoToggle::getValue);
    public final BooleanSetting disableOnDeath = new BooleanSetting("Disable On Death", this, true, autoToggle::getValue);
    public final BooleanSetting disableIfNoBlocks = new BooleanSetting("Disable If No Blocks", this, true, autoToggle::getValue);

    public final NumberSetting blocksPerTick = new NumberSetting("Blocks Per Tick", this, 4, 1, 8, true);
    public final BooleanSetting extraBlocks = new BooleanSetting("Extra Blocks", this, false);
    public final BooleanSetting silentSwap = new BooleanSetting("Silent Swap", this, true);

    public Surround() {
        super("Surround",
                "lang.module.Surround",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

        initSettings(
                autoToggle,
                disableOnY,
                disableOnTp,
                disableOnDeath,
                disableIfNoBlocks,

                blocksPerTick,
                extraBlocks,
                silentSwap
        );
    }

    private double prevY;

    @Override
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
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) {
            toggle();
            return;
        }

        if (autoToggle.getValue()) {
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
                if (!BuildManager.pickUpPlaceBlocks(false, BuildManager.obsidians)) {
                    sendNotification(Formatting.RED + LanguageSystem.translate("lang.module.Scaffold.noBlocks"));
                    toggle();
                    return;
                }
            }
        }

        prevY = mc.player.getY();

        if (!BuildManager.pickUpPlaceBlocks(false, BuildManager.obsidians)) {
            if (autoToggle.getValue() && disableIfNoBlocks.getValue()) {
                toggle();
            }
            return;
        }

        int count = 0;
        for (BlockPos blockPos : getBlockPoses()) {
            if (mc.world.isAir(blockPos)) {
                if (silentSwap.getValue()) {
                    if (BuildManager.pickUpPlaceBlocks(false, BuildManager.obsidians)) {
                        int slot = findSlot();
                        if (slot != -1) {
                            int oldSlot = mc.player.getInventory().selectedSlot;
                            InventoryUtils.swapAction(oldSlot, slot, false, "Client");
                            BuildManager.placeBlock(blockPos);
                            InventoryUtils.swapAction(oldSlot, slot, true, "Client");
                        }
                    }
                } else if (BuildManager.pickUpPlaceBlocks(true, BuildManager.obsidians)) BuildManager.placeBlock(blockPos);
                count++;
                if (count >= blocksPerTick.getValue()) break;
            }
        }
    }

    @EventSubscriber
    public void onPacketReceive(PacketEvent.Receive e) {
        if (nullCheck()) return;

        if (autoToggle.getValue() && disableOnTp.getValue()) {
            if (e.getPacket() instanceof PlayerPositionLookS2CPacket) {
                toggle();
            }
        }
    }

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

    public int findSlot() {
        if (mc.player.getMainHandStack().getItem() instanceof BlockItem item) {
            if (BuildManager.isNeedBlock(item.getBlock(), BuildManager.obsidians)) return mc.player.getInventory().selectedSlot;
        }

        for (int i = 0; i < 36; i++) {
            if (mc.player.getInventory().getStack(i).getItem() instanceof BlockItem blockItem) {
                if (BuildManager.isNeedBlock(blockItem.getBlock(), BuildManager.obsidians))
                    return i;
            }
        }
        return -1;
    }
}
