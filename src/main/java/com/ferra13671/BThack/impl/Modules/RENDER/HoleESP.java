package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Box.RenderBox;
import com.ferra13671.BThack.api.Events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
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

    public final ModeSetting page = new ModeSetting("Page", this, new ArrayList<>(Arrays.asList("Range", "Update", "Box", "Holes")));

    //Range Settings
    public final ModeSetting rangeMode = new ModeSetting("Range Mode", this, new ArrayList<>(Arrays.asList("Normal", "Extra")), () -> page.getValue().equals("Range"));
    public final NumberSetting range = new NumberSetting("Range", this, 7, 3, 200, true, () -> rangeMode.getValue().equals("Normal") && page.getValue().equals("Range"));
    public final NumberSetting rangeH = new NumberSetting("RangeH", this, 7, 3, 200, false, () -> rangeMode.getValue().equals("Extra") && page.getValue().equals("Range"));
    public final NumberSetting rangeV = new NumberSetting("RangeV", this, 7, 3, 200, false, () -> rangeMode.getValue().equals("Extra") && page.getValue().equals("Range"));
    public final BooleanSetting sphere = new BooleanSetting("Sphere", this, true, () -> rangeMode.getValue().equals("Extra") && page.getValue().equals("Range"));
    /////////

    //Update Settings
    public final ModeSetting updateMode = new ModeSetting("UpMode", this, new ArrayList<>(Arrays.asList("Thread", "Unoptimized")), () -> page.getValue().equals("Update"));
    public final NumberSetting updateDelay = new NumberSetting("Delay", this, 500, 100, 1500, true, () -> updateMode.getValue().equals("Thread") && page.getValue().equals("Update"));
    /////////

    //Box Settings
    public final NumberSetting boxLength = new NumberSetting("Box Length", this, 0.5, 0.05, 0.5, false, () -> page.getValue().equals("Box"));
    public final NumberSetting boxWidth = new NumberSetting("Box Width", this, 0.5, 0.05, 0.5, false, () -> page.getValue().equals("Box"));
    public final NumberSetting boxHeight = new NumberSetting("Box Height", this, 0.2, 0.1, 1, false, () -> page.getValue().equals("Box"));
    /////////

    //Holes Settings
    public final BooleanSetting obsidianHoles = new BooleanSetting("Obsidian Holes", this, true, () -> page.getValue().equals("Holes"));
    public final ColorSetting obsidianColor = new ColorSetting("Obsidian Color", this, new Color(255, 255, 0), () -> obsidianHoles.getValue() && page.getValue().equals("Holes")).withBlockedAlpha();

    public final BooleanSetting bedrockHoles = new BooleanSetting("Bedrock Holes", this, true, () -> page.getValue().equals("Holes"));
    public final ColorSetting bedrockColor = new ColorSetting("Bedrock Color", this, new Color(61, 194, 46), () -> bedrockHoles.getValue() && page.getValue().equals("Holes")).withBlockedAlpha();
    /////////


    public HoleESP() {
        super("HoleESP",
                "lang.module.HoleESP",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        initSettings(
                page,
                rangeMode,
                range,
                rangeH,
                rangeV,
                sphere,
                updateMode,
                updateDelay,
                boxLength,
                boxWidth,
                boxHeight,
                obsidianHoles,
                obsidianColor,

                bedrockHoles,
                bedrockColor
        );
    }

    private BThackThread searchThread = new SearchThread();

    public List<BlockPos> findObsidianHoles() {
        List<BlockPos> obsHoles;
        if (rangeMode.getValue().equals("Normal")) {
            obsHoles = BlockUtils.getNearbyBlocks(mc.player, range.getValue(), false);
        } else {
            obsHoles = BlockUtils.getSphere(new BlockPos(mc.player.getBlockPos()), (float) rangeH.getValue(), (float) rangeV.getValue(), false, sphere.getValue(), 0);
        }
        return obsHoles.stream()
                .filter(blockPos -> HoleUtils.isMutableHole(blockPos, true))
                .collect(Collectors.toList());
    }

    public List<BlockPos> findBedrockHoles() {
        List<BlockPos> bedHoles;
        if (rangeMode.getValue().equals("Normal")) {
            bedHoles = BlockUtils.getNearbyBlocks(mc.player, range.getValue(), false);
        } else {
            bedHoles = BlockUtils.getSphere(new BlockPos(mc.player.getBlockPos()), (float) rangeH.getValue(), (float) rangeV.getValue(), false, sphere.getValue(), 0);
        }
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
                    obsHoles = bedHoles = BlockUtils.getSphere(new BlockPos(mc.player.getBlockPos()), (float) ModuleList.holeESP.rangeH.getValue(), (float) ModuleList.holeESP.rangeV.getValue(), false, ModuleList.holeESP.sphere.getValue(), 0);
                }
                ModuleList.holeESP.obsidianHoleList = obsHoles.stream().filter(blockPos -> HoleUtils.isMutableHole(blockPos, true))
                        .collect(Collectors.toList());
                ModuleList.holeESP.bedrockHoleList = bedHoles.stream().filter(HoleUtils::isBedrockHole)
                        .collect(Collectors.toList());

                sleepThread((int) ModuleList.holeESP.updateDelay.getValue());
            }
        }
    }
}
