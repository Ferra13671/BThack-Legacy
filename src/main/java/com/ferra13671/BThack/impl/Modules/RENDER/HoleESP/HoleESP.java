package com.ferra13671.BThack.impl.Modules.RENDER.HoleESP;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Box.RenderBox;
import com.ferra13671.BThack.api.Events.Render.RenderWorldEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.*;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HoleESP extends Module {
    HoleESPSearchThread searchThread = new HoleESPSearchThread();

    public static ModeSetting page;


    public static ModeSetting rangeMode;
    public static NumberSetting range;
    public static NumberSetting rangeH;
    public static NumberSetting rangeV;
    public static BooleanSetting sphere;


    public static ModeSetting updateMode;
    public static NumberSetting updateDelay;


    public static NumberSetting boxLength;
    public static NumberSetting boxWidth;
    public static NumberSetting boxHeight;


    public static BooleanSetting obsidianHoles;
    public static NumberSetting obsidianRed;
    public static NumberSetting obsidianGreen;
    public static NumberSetting obsidianBlue;

    public static BooleanSetting bedrockHoles;
    public static NumberSetting bedrockRed;
    public static NumberSetting bedrockGreen;
    public static NumberSetting bedrockBlue;



    public HoleESP() {
        super("HoleESP",
                "lang.module.HoleESP",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        page = new ModeSetting("Page", this, new ArrayList<>(Arrays.asList("Range", "Update", "Box", "Holes")));

        //Range Settings
        rangeMode = new ModeSetting("Range Mode", this, new ArrayList<>(Arrays.asList("Normal", "Extra")), () -> page.getValue().equals("Range"));
        range = new NumberSetting("Range", this, 7, 3, 200, true, () -> rangeMode.getValue().equals("Normal") && page.getValue().equals("Range"));

        rangeH = new NumberSetting("RangeH", this, 7, 3, 200, false, () -> rangeMode.getValue().equals("Extra") && page.getValue().equals("Range"));
        rangeV = new NumberSetting("RangeV", this, 7, 3, 200, false, () -> rangeMode.getValue().equals("Extra") && page.getValue().equals("Range"));
        sphere = new BooleanSetting("Sphere", this, true, () -> rangeMode.getValue().equals("Extra") && page.getValue().equals("Range"));
        /////////

        //Update Settings
        updateMode = new ModeSetting("UpMode", this, new ArrayList<>(Arrays.asList("Thread", "Unoptimized")), () -> page.getValue().equals("Update"));

        updateDelay = new NumberSetting("Delay", this, 500, 100, 1500, true, () -> updateMode.getValue().equals("Thread") && page.getValue().equals("Update"));
        /////////

        //Box Settings
        boxLength = new NumberSetting("Box Length", this, 0.5, 0.05, 0.5, false, () -> page.getValue().equals("Box"));
        boxWidth = new NumberSetting("Box Width", this, 0.5, 0.05, 0.5, false, () -> page.getValue().equals("Box"));
        boxHeight = new NumberSetting("Box Height", this, 0.2, 0.1, 1, false, () -> page.getValue().equals("Box"));
        /////////

        //Holes Settings
        obsidianHoles = new BooleanSetting("Obsidian Holes", this, true, () -> page.getValue().equals("Holes"));
        obsidianRed = new NumberSetting("Obs Red", this, 255, 0, 255, true, () -> obsidianHoles.getValue() && page.getValue().equals("Holes"));
        obsidianGreen = new NumberSetting("Obs Green", this, 255, 0, 255, true, () -> obsidianHoles.getValue() && page.getValue().equals("Holes"));
        obsidianBlue = new NumberSetting("Obs Blue", this, 0, 0, 255, true, () -> obsidianHoles.getValue() && page.getValue().equals("Holes"));

        bedrockHoles = new BooleanSetting("Bedrock Holes", this, true, () -> page.getValue().equals("Holes"));
        bedrockRed = new NumberSetting("Bed Red", this, 61, 0, 255, true, () -> bedrockHoles.getValue() && page.getValue().equals("Holes"));
        bedrockGreen = new NumberSetting("Bed Green", this, 194, 0, 255, true, () -> bedrockHoles.getValue() && page.getValue().equals("Holes"));
        bedrockBlue = new NumberSetting("Bed Blue", this, 46, 0, 255, true, () -> bedrockHoles.getValue() && page.getValue().equals("Holes"));
        /////////

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
                obsidianRed,
                obsidianGreen,
                obsidianBlue,
                bedrockHoles,
                bedrockRed,
                bedrockGreen,
                bedrockBlue
        );
    }

    public List<BlockPos> findObsidianHoles() {
        List<BlockPos> obsHoles;
        if (rangeMode.getValue().equals("Normal")) {
            obsHoles = BlockUtils.getNearbyBlocks(mc.player, range.getValue(), false);
        } else {
            obsHoles = BlockUtils.getSphere(new ModifyBlockPos(mc.player.getBlockPos()), (float) rangeH.getValue(), (float) rangeV.getValue(), false, sphere.getValue(), 0);
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
            bedHoles = BlockUtils.getSphere(new ModifyBlockPos(mc.player.getBlockPos()), (float) rangeH.getValue(), (float) rangeV.getValue(), false, sphere.getValue(), 0);
        }
        return bedHoles.stream()
                .filter(HoleUtils::isBedrockHole)
                .collect(Collectors.toList());
    }

    protected static List<BlockPos> obsidianHoleList = new ArrayList<>();
    protected static List<BlockPos> bedrockHoleList = new ArrayList<>();

    @EventSubscriber
    public void onRenderWorldLast(RenderWorldEvent.Last event) {
        if (nullCheck()) return;

        if (updateMode.getValue().equals("Unoptimized")) {
            obsidianHoleList = findObsidianHoles();
            bedrockHoleList = findBedrockHoles();
        } else {
            if (!searchThread.isAlive()) {
                searchThread = new HoleESPSearchThread();
                searchThread.start();
            }
        }

        ArrayList<RenderBox> renderBoxes = new ArrayList<>();

        if (obsidianHoles.getValue() && obsidianHoleList != null) {

            float oRed = (float) obsidianRed.getValue() / 255f;
            float oGreen = (float) obsidianGreen.getValue() / 255f;
            float oBlue = (float) obsidianBlue.getValue() / 255f;

            for (BlockPos obsidianHole : obsidianHoleList) {
                Box box = BlockUtils.createBox(obsidianHole, boxLength.getValue(), boxWidth.getValue(), boxHeight.getValue(), false);
                renderBoxes.add(new RenderBox(box, oRed, oGreen, oBlue, 0.6f, oRed, oGreen, oBlue, 0.4f));
            }
        }

        if (bedrockHoles.getValue() && bedrockHoleList != null) {
            float bRed = (float) bedrockRed.getValue() / 255f;
            float bGreen = (float) bedrockGreen.getValue() / 255f;
            float bBlue = (float) bedrockBlue.getValue() / 255f;

            for (BlockPos bedrockHole : bedrockHoleList) {
                Box box = BlockUtils.createBox(bedrockHole, boxLength.getValue(), boxWidth.getValue(), boxHeight.getValue(), false);
                renderBoxes.add(new RenderBox(box, bRed, bGreen, bBlue, 0.6f, bRed, bGreen, bBlue, 0.4f));
            }
        }

        BThackRender.boxRender.prepareBoxRender();
        BThackRender.boxRender.renderBoxes(renderBoxes);
        BThackRender.boxRender.stopBoxRender();
    }
}
