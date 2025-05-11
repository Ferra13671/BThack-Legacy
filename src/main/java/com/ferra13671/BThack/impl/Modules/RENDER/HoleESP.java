package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Box.RenderBox;
import com.ferra13671.BThack.api.Events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.*;
import com.ferra13671.BThack.api.Managers.managers.Thread.BThackThread;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.*;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HoleESP extends Module {

    public final CategorySetting rangeCategory = new CategorySetting("Range", this);
    public final ModeSetting rangeMode = new ModeSetting("Range Mode", this, new ArrayList<>(Arrays.asList("Normal", "Extra"))).inCategory(rangeCategory);
    public final NumberSetting range = new NumberSetting("Range", this, 7, 3, 200, true, () -> rangeMode.getValue().equals("Normal")).inCategory(rangeCategory);
    public final NumberSetting rangeH = new NumberSetting("RangeH", this, 7, 3, 200, false, () -> rangeMode.getValue().equals("Extra")).inCategory(rangeCategory);
    public final NumberSetting rangeV = new NumberSetting("RangeV", this, 7, 3, 200, false, () -> rangeMode.getValue().equals("Extra")).inCategory(rangeCategory);
    public final BooleanSetting sphere = new BooleanSetting("Sphere", this, true, () -> rangeMode.getValue().equals("Extra")).inCategory(rangeCategory);

    public final CategorySetting updateCategory = new CategorySetting("Update", this);
    public final ModeSetting updateMode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Thread", "Unoptimized"))).inCategory(updateCategory);
    public final NumberSetting updateDelay = new NumberSetting("Delay", this, 500, 100, 1500, true, () -> updateMode.getValue().equals("Thread")).inCategory(updateCategory);

    public final CategorySetting boxCategory = new CategorySetting("Box", this);
    public final NumberSetting boxLength = new NumberSetting("Box Length", this, 0.5, 0.05, 0.5, false).inCategory(boxCategory);
    public final NumberSetting boxWidth = new NumberSetting("Box Width", this, 0.5, 0.05, 0.5, false).inCategory(boxCategory);
    public final NumberSetting boxHeight = new NumberSetting("Box Height", this, 0.2, 0.1, 1, false).inCategory(boxCategory);

    public final CategorySetting holesCategory = new CategorySetting("Holes", this);
    public final BooleanSetting obsidianHoles = new BooleanSetting("Obsidian Holes", this, true).inCategory(holesCategory);
    public final ColorSetting obsidianColor = new ColorSetting("Obsidian Color", this, new Color(255, 255, 0), obsidianHoles::getValue).withBlockedAlpha().inCategory(holesCategory);
    public final BooleanSetting bedrockHoles = new BooleanSetting("Bedrock Holes", this, true).inCategory(holesCategory);
    public final ColorSetting bedrockColor = new ColorSetting("Bedrock Color", this, new Color(61, 194, 46), bedrockHoles::getValue).withBlockedAlpha().inCategory(holesCategory);


    public HoleESP() {
        super("HoleESP",
                "lang.module.HoleESP",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );
    }

    private BThackThread searchThread = new SearchThread();

    public List<BlockPos> findObsidianHoles() {
        List<BlockPos> obsHoles;
        if (rangeMode.getValue().equals("Normal")) {
            obsHoles = BlockUtils.getNearbyBlocks(mc.player, range.getValue(), false);
        } else {
            obsHoles = BlockUtils.getSphere(new BlockPos(mc.player.getBlockPos()), rangeH.getValue().floatValue(), rangeV.getValue().floatValue(), false, sphere.getValue(), 0);
        }
        return obsHoles.stream()
                .filter(blockPos -> HoleUtils.isMutableHole(blockPos, true))
                .collect(Collectors.toList());
    }

    public List<BlockPos> findBedrockHoles() {
        List<BlockPos> bedHoles = rangeMode.getValue().equals("Normal") ?
                BlockUtils.getNearbyBlocks(mc.player, range.getValue(), false) :
                BlockUtils.getSphere(new BlockPos(mc.player.getBlockPos()), rangeH.getValue().floatValue(), rangeV.getValue().floatValue(), false, sphere.getValue(), 0);

        return bedHoles.stream()
                .filter(HoleUtils::isBedrockHole)
                .collect(Collectors.toList());
    }

    protected List<BlockPos> obsidianHoleList = new ArrayList<>();
    protected List<BlockPos> bedrockHoleList = new ArrayList<>();

    @EventSubscriber
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (nullCheck()) return;

        if (updateMode.getValue().equals("Unoptimized")) {
            obsidianHoleList = findObsidianHoles();
            bedrockHoleList = findBedrockHoles();
        } else {
            if (!searchThread.isAlive()) {
                searchThread = new SearchThread();
                searchThread.start();
            }
        }

        ArrayList<RenderBox> renderBoxes = new ArrayList<>();

        if (obsidianHoles.getValue() && obsidianHoleList != null) {

            float oRed = (float) obsidianColor.getValue().getRed() / 255f;
            float oGreen = (float) obsidianColor.getValue().getGreen() / 255f;
            float oBlue = (float) obsidianColor.getValue().getBlue() / 255f;

            for (BlockPos obsidianHole : obsidianHoleList) {
                Box box = BlockUtils.createBox(obsidianHole, boxLength.getValue(), boxWidth.getValue(), boxHeight.getValue(), false);
                renderBoxes.add(new RenderBox(box, oRed, oGreen, oBlue, 0.6f, oRed, oGreen, oBlue, 0.4f));
            }
        }

        if (bedrockHoles.getValue() && bedrockHoleList != null) {
            float bRed = (float) bedrockColor.getValue().getRed() / 255f;
            float bGreen = (float) bedrockColor.getValue().getGreen() / 255f;
            float bBlue = (float) bedrockColor.getValue().getBlue() / 255f;

            for (BlockPos bedrockHole : bedrockHoleList) {
                Box box = BlockUtils.createBox(bedrockHole, boxLength.getValue(), boxWidth.getValue(), boxHeight.getValue(), false);
                renderBoxes.add(new RenderBox(box, bRed, bGreen, bBlue, 0.6f, bRed, bGreen, bBlue, 0.4f));
            }
        }

        BThackRender.boxRender.prepareBoxRender();
        BThackRender.boxRender.renderBoxes(renderBoxes);
        BThackRender.boxRender.stopBoxRender();
    }

    public static class SearchThread extends BThackThread {

        @Override
        public void threadAction() {
            while (ModuleList.holeESP.isEnabled() && ModuleList.holeESP.updateMode.getValue().equals("Thread")) {
                if (Module.nullCheck()) return;
                List<BlockPos> obsHoles;
                List<BlockPos> bedHoles;
                if (ModuleList.holeESP.rangeMode.getValue().equals("Normal")) {
                    obsHoles = bedHoles = BlockUtils.getNearbyBlocks(mc.player, ModuleList.holeESP.range.getValue(), false);
                } else {
                    obsHoles = bedHoles = BlockUtils.getSphere(new BlockPos(mc.player.getBlockPos()), ModuleList.holeESP.rangeH.getValue().floatValue(), ModuleList.holeESP.rangeV.getValue().floatValue(), false, ModuleList.holeESP.sphere.getValue(), 0);
                }
                ModuleList.holeESP.obsidianHoleList = obsHoles.stream().filter(blockPos -> HoleUtils.isMutableHole(blockPos, true))
                        .collect(Collectors.toList());
                ModuleList.holeESP.bedrockHoleList = bedHoles.stream().filter(HoleUtils::isBedrockHole)
                        .collect(Collectors.toList());

                sleepThread(ModuleList.holeESP.updateDelay.getValue().longValue());
            }
        }
    }
}
