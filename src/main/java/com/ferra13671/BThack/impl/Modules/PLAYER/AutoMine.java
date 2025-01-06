package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.api.Events.SendMessageEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BaritoneUtils;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.List.BlockList.BlockLists;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class AutoMine extends Module {

    public static BooleanSetting ancientDebris;
    public static BooleanSetting diamond;
    public static BooleanSetting deepslateDiamond;
    public static BooleanSetting gold;
    public static BooleanSetting deepslateGold;
    public static BooleanSetting netherGold;
    public static BooleanSetting iron;
    public static BooleanSetting deepslateIron;
    public static BooleanSetting copper;
    public static BooleanSetting deepslateCopper;
    public static BooleanSetting coal;
    public static BooleanSetting deepslateCoal;
    public static BooleanSetting quartz;

    public static BooleanSetting extraBlocks;

    public AutoMine() {
        super("AutoMine",
                "lang.module.AutoMine",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        ancientDebris = new BooleanSetting("Ancient Debris", this, true);
        diamond = new BooleanSetting("Diamond", this, true);
        deepslateDiamond = new BooleanSetting("Deepslate Diamond", this, true);
        gold = new BooleanSetting("Gold", this, false);
        deepslateGold = new BooleanSetting("Deepslate Gold", this, false);
        netherGold = new BooleanSetting("Nether Gold", this, false);
        iron = new BooleanSetting("Iron", this, false);
        deepslateIron = new BooleanSetting("Deepslate Iron", this, false);
        copper = new BooleanSetting("Copper", this, false);
        deepslateCopper = new BooleanSetting("Deepslate Copper", this, false);
        coal = new BooleanSetting("Coal", this, false);
        deepslateCoal = new BooleanSetting("Deepslate Coal", this, false);
        quartz = new BooleanSetting("Quartz", this, false);

        extraBlocks = new BooleanSetting("Extra Blocks", this, false);

        initSettings(
                ancientDebris,
                diamond,
                deepslateDiamond,
                gold,
                deepslateGold,
                netherGold,
                iron,
                deepslateIron,
                copper,
                deepslateCopper,
                coal,
                deepslateCoal,
                quartz,

                extraBlocks
        );
    }

    @Override
    public void onChangeSetting(Setting setting) {
        if (this.isEnabled())
            reMineAction();
        if (setting.equals(extraBlocks))
            if (extraBlocks.getValue())
                ChatUtils.sendMessage(Formatting.GRAY + "Use: " + Client.clientInfo.getChatPrefix() + BlockLists.get("AutoMine").editBlockListCommand.getUsage());
    }

    @Override
    public void onEnable() {
        super.onEnable();
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
    }

    public void reMineAction() {
        BaritoneUtils.cancelMine();
        startMineAction();
    }

    public void startMineAction() {
        if (!ancientDebris.getValue() &&
                !diamond.getValue() &&
                !deepslateDiamond.getValue() &&
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

        if (extraBlocks.getValue()) blocks.addAll(BlockLists.get("AutoMine").blocks);

        BaritoneUtils.mine(blocks);
    }
}
