package com.ferra13671.BThack.impl.modules.combat;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.managers.impl.clans.ClanSettingsBuilder;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.utils.InventoryUtils;
import com.ferra13671.BThack.api.utils.ItemUtils;
import com.ferra13671.BThack.api.utils.modules.KillAuraUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;

@ModuleInfo(name = "WebAura", description = "lang.module.WebAura", category = "COMBAT")
public class WebAura extends Module {

    public final BooleanSetting allowInventory = new BooleanSetting("Allow Inventory", this, true);
    public final ModeSetting swap = new ModeSetting("Swap Mode", this, Arrays.asList("Client", "Packet"));
    public final BooleanSetting ignoreWalls = new BooleanSetting("Ignore Walls", this, true);

    public final BooleanSetting players = new BooleanSetting("Players", this, true);
    public final BooleanSetting invisibles = new BooleanSetting("Invisibles", this, true, players::getValue);
    public final BooleanSetting friends = new BooleanSetting("Friends", this, false, players::getValue);
    public final BooleanSetting teammates = new BooleanSetting("Teammates", this, false, players::getValue);
    public final BooleanSetting mobs = new BooleanSetting("Mobs", this, false);
    public final BooleanSetting clanManager = ClanSettingsBuilder.buildToggle(this);
    public final ModeSetting clanMode = ClanSettingsBuilder.buildStatusMode(this, clanManager);
    public final ModeSetting targetClan = ClanSettingsBuilder.buildClanTargetMode(this, clanManager, clanMode);


    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        int slot = InventoryUtils.findItem(Items.COBWEB, allowInventory.getValue() ? 36 : 9);
        if (slot == -1) return;

        PlayerEntity player = KillAuraUtils.filterPlayers(4,
                invisibles.getValue(),
                friends.getValue(),
                teammates.getValue(),
                clanManager.getValue(),
                clanMode.getValue(),
                targetClan.getValue(),
                (entity) -> entity.onGround &&
                        KillAuraUtils.canBeSeeTarget(ignoreWalls, entity) &&
                        mc.world.isAir(BlockPos.ofFloored(entity.getX(), entity.getY(), entity.getZ()))
        );

        Entity entity = KillAuraUtils.filterEntity(
                4,
                (entity1) -> entity1.onGround &&
                        entity1 instanceof LivingEntity &&
                        KillAuraUtils.canBeSeeTarget(ignoreWalls, entity1) &&
                        mc.world.isAir(BlockPos.ofFloored(entity1.getX(), entity1.getY(), entity1.getZ()))
        );

        if ((players.getValue() && player != null) || (mobs.getValue() && entity != null))
            placeAction(slot, player);
    }

    @SuppressWarnings("DataFlowIssue")
    public void placeAction(int slot, Entity entity) {
        int oldSlot = mc.player.getInventory().selectedSlot;

        InventoryUtils.swapAction(oldSlot, slot, false, swap.getValue());

        BlockPos pos = BlockPos.ofFloored(entity.getX(), entity.getY(), entity.getZ());
        ItemUtils.useItemOnBlock(pos);

        InventoryUtils.swapAction(oldSlot, slot, true, swap.getValue());
    }
}
