package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.api.Events.SendMessageEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BaritoneUtils;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.DataList.BlockList;
import com.ferra13671.BThack.api.Utils.DataList.DataLists;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class AutoMine extends Module {

    public final BooleanSetting ancientDebris = new BooleanSetting("Ancient Debris", this, true);
    public final BooleanSetting diamond = new BooleanSetting("Diamond", this, true);
    public final BooleanSetting deepslateDiamond = new BooleanSetting("Deepslate Diamond", this, true);
    public final BooleanSetting redstone = new BooleanSetting("Redstone", this, false);
    public final BooleanSetting deepslateRedstone = new BooleanSetting("Deepslate Redstone", this, false);
    public final BooleanSetting gold = new BooleanSetting("Gold", this, false);
    public final BooleanSetting deepslateGold = new BooleanSetting("Deepslate Gold", this, false);
    public final BooleanSetting netherGold = new BooleanSetting("Nether Gold", this, false);
    public final BooleanSetting iron = new BooleanSetting("Iron", this, false);
    public final BooleanSetting deepslateIron = new BooleanSetting("Deepslate Iron", this, false);
    public final BooleanSetting copper = new BooleanSetting("Copper", this, false);
    public final BooleanSetting deepslateCopper = new BooleanSetting("Deepslate Copper", this, false);
    public final BooleanSetting coal = new BooleanSetting("Coal", this, false);
    public final BooleanSetting deepslateCoal = new BooleanSetting("Deepslate Coal", this, false);
    public final BooleanSetting quartz = new BooleanSetting("Quartz", this, false);

    public final BooleanSetting extraBlocks = new BooleanSetting("Extra Blocks", this, false);

    public AutoMine() {
        super("AutoMine",
                "lang.module.AutoMine",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );
    }

    @Override
    public void onChangeSetting(Setting<?> setting) {
        if (this.isEnabled())
            reMineAction();
        if (setting.equals(extraBlocks))
            if (extraBlocks.getValue())
                ChatUtils.sendMessage(Formatting.GRAY + "Use: " + Client.clientInfo.getChatPrefix() + DataLists.get("AutoMine", BlockList.class).editDataListCommand.getAliases()[0]);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (nullCheck()) {
            toggle();
            return;
        }
        reMineAction();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        BaritoneUtils.cancelMine();
    }

    @EventSubscriber
    public void onSendMessage(SendMessageEvent e) {
        if (e.message.startsWith("#mine ")) {
            setToggled(false);
        }
        if (e.message.startsWith("#stop"))
            setToggled(false);
    }

    public void reMineAction() {
        BaritoneUtils.cancelMine();
        startMineAction();
    }

    public void startMineAction() {
        if (!ancientDebris.getValue() &&
                !diamond.getValue() &&
                !deepslateDiamond.getValue() &&
                !redstone.getValue() &&
                !deepslateRedstone.getValue() &&
                !gold.getValue() &&
                !deepslateGold.getValue() &&
                !netherGold.getValue() &&
                !iron.getValue() &&
                !deepslateIron.getValue() &&
                !copper.getValue() &&
                !deepslateCopper.getValue() &&
                !coal.getValue() &&
                !deepslateCoal.getValue() &&
                !quartz.getValue() &&
                !extraBlocks.getValue()) return;
        List<Block> blocks = new ArrayList<>();
        if (ancientDebris.getValue()) blocks.add(Blocks.ANCIENT_DEBRIS);
        if (diamond.getValue()) blocks.add(Blocks.DIAMOND_ORE);
        if (deepslateDiamond.getValue()) blocks.add(Blocks.DEEPSLATE_DIAMOND_ORE);
        if (redstone.getValue()) blocks.add(Blocks.REDSTONE_ORE);
        if (deepslateRedstone.getValue()) blocks.add(Blocks.DEEPSLATE_REDSTONE_ORE);
        if (gold.getValue()) blocks.add(Blocks.GOLD_ORE);
        if (deepslateGold.getValue()) blocks.add(Blocks.DEEPSLATE_GOLD_ORE);
        if (netherGold.getValue()) blocks.add(Blocks.NETHER_GOLD_ORE);
        if (iron.getValue()) blocks.add(Blocks.IRON_ORE);
        if (deepslateIron.getValue()) blocks.add(Blocks.DEEPSLATE_IRON_ORE);
        if (copper.getValue()) blocks.add(Blocks.COPPER_ORE);
        if (deepslateCopper.getValue()) blocks.add(Blocks.DEEPSLATE_COPPER_ORE);
        if (coal.getValue()) blocks.add(Blocks.COAL_ORE);
        if (deepslateCoal.getValue()) blocks.add(Blocks.DEEPSLATE_COAL_ORE);
        if (quartz.getValue()) blocks.add(Blocks.NETHER_QUARTZ_ORE);

        if (extraBlocks.getValue()) blocks.addAll(DataLists.get("AutoMine", BlockList.class).values);

        BaritoneUtils.mine(blocks);
    }
}
