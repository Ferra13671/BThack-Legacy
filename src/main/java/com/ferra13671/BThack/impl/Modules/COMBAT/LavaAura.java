package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.Clans.ClanSettingsBuilder;
import com.ferra13671.BThack.api.Utils.*;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Deprecated(forRemoval = true)
//Not working
public class LavaAura extends Module {

    public final BooleanSetting allowInventory = new BooleanSetting("Allow Inventory", this, true);
    public final ModeSetting swap = new ModeSetting("Swap Mode", this, Arrays.asList("Client", "Packet"));

    public final BooleanSetting pickUpAfter = new BooleanSetting("Pick Up After", this, true);
    public final NumberSetting pickUpDelay = new NumberSetting("PickUpDelay", this, 3, 1, 7, false, pickUpAfter::getValue);

    public final BooleanSetting players = new BooleanSetting("Players", this, true);
    public final BooleanSetting friends = new BooleanSetting("Friends", this, false);
    public final BooleanSetting teammates = new BooleanSetting("Teammates", this, false);
    public final BooleanSetting clanManager = ClanSettingsBuilder.buildToggle(this);
    public final ModeSetting clanMode = ClanSettingsBuilder.buildStatusMode(this, clanManager);
    public final ModeSetting targetClan = ClanSettingsBuilder.buildClanTargetMode(this, clanManager, clanMode);

    public final BooleanSetting mobs = new BooleanSetting("Mobs", this, false);

    public LavaAura() {
        super("LavaAura",
                "",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

        initSettings(
                allowInventory,
                swap,

                pickUpAfter,
                pickUpDelay,

                players,
                friends,
                teammates,
                clanManager,
                clanMode,
                targetClan,

                mobs
        );
    }

    public List<BlockPos> lavas = new ArrayList<>();
    private final Ticker ticker = new Ticker();

    @Override
    public void onEnable() {
        super.onEnable();
        ticker.reset();
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        int slot = InventoryUtils.findItem(Items.LAVA_BUCKET, allowInventory.getValue() ? 36 : 9);
        if (slot == -1) return;

        PlayerEntity player = KillAuraUtils.filterPlayers(
                4,
                friends.getValue(),
                teammates.getValue(),
                clanManager.getValue(),
                clanMode.getValue(),
                targetClan.getValue(),
                (entity) -> entity.onGround &&
                        !BuildManager.lavas.contains(mc.world.getBlockState(BlockPos.ofFloored(entity.getX(), entity.getY(), entity.getZ())).getBlock()) &&
                        BlockUtils.hasLineOfSight((new Vec3d(mc.player.getX(), mc.player.getY(), mc.player.getZ()).add(0, mc.player.getStandingEyeHeight(), 0)), new Vec3d(entity.getX(), entity.getY(), entity.getZ()))
                );
        Entity entity = KillAuraUtils.filterEntity(
                4,
                (entity1) -> entity1.onGround &&
                        entity1 instanceof LivingEntity &&
                        !BuildManager.lavas.contains(mc.world.getBlockState(BlockPos.ofFloored(entity1.getX(), entity1.getY(), entity1.getZ())).getBlock()) &&
                        BlockUtils.hasLineOfSight((new Vec3d(mc.player.getX(), mc.player.getY(), mc.player.getZ()).add(0, mc.player.getStandingEyeHeight(), 0)), new Vec3d(entity1.getX(), entity1.getY(), entity1.getZ()))
                );

        preAction();

        if (players.getValue() && player != null) {
            action(slot, player);
        } else if (mobs.getValue() && entity != null) {
            action(slot, entity);
        }
    }

    public void preAction() {
        if (!pickUpAfter.getValue() && ticker.passed(pickUpDelay.getValue() * 1000)) {
            lavas.removeIf(pos -> MathUtils.getDistance(mc.player.getPos(), new Vec3d(pos.getX(), pos.getY(), pos.getZ())) > 4);

            for (BlockPos pos : lavas) {
                if (mc.world.getBlockState(pos).getBlock() != Blocks.LAVA) return;
                int slot = InventoryUtils.findItem(Items.BUCKET, allowInventory.getValue() ? 36 : 9);
                if (slot == -1) return;
                int oldSlot = mc.player.getInventory().selectedSlot;

                swapAction(oldSlot, slot, false);
                Vec3d vec3d = new Vec3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                vec3d.y = pos.getY() - 0.4;
                float[] rots = RotateUtils.rotations(vec3d);
                GrimUtils.sendPreActionGrimPackets(rots[0], rots[1]);
                ItemUtils.useItem(Hand.MAIN_HAND, false, rots[0], rots[1]);
                GrimUtils.sendPostActionGrimPackets();
                //ItemsUtils.useItemOnBlock(BuildManager.getHitResult(pos, false, Direction.UP));

                //swapAction(oldSlot, slot, true);
            }
        }
    }

    public void action(int slot, Entity entity) {
        int oldSlot = mc.player.getInventory().selectedSlot;

        swapAction(oldSlot, slot, false);

        BlockPos pos = BlockPos.ofFloored(entity.getX(), entity.getY(), entity.getZ());
        Vec3d vec3d = new Vec3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        vec3d.y = pos.getY() - 0.4;
        float[] rots = RotateUtils.rotations(vec3d);
        GrimUtils.sendPreActionGrimPackets(rots[0], rots[1]);
        ItemUtils.useItem(Hand.MAIN_HAND, false, rots[0], rots[1]);
        GrimUtils.sendPostActionGrimPackets();
        //ItemsUtils.useItemOnBlock(BuildManager.getHitResult(pos.add(0, -1, 0), false, Direction.UP));
        lavas.add(pos);

        //swapAction(oldSlot, slot, true);
    }

    public void swapAction(int oldSlot, int slot, boolean post) {
        if (!post && oldSlot == slot) return;

        switch (swap.getValue()) {
            case "Client" -> {
                if (slot < 9) {
                    InventoryUtils.swapItem((post ? oldSlot : slot));
                } else {
                    InventoryUtils.swapItemOnInventory(oldSlot, slot);
                }
            }
            case "Packet" -> {
                if (slot < 9)
                    InventoryUtils.packetSwapItem((post ? oldSlot : slot));
                else
                    InventoryUtils.swapItemOnInventory(oldSlot, slot);
            }
        }
    }
}
