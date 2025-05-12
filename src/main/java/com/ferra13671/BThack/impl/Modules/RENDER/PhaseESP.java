package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Box.RenderBox;
import com.ferra13671.BThack.api.Events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3i;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ModuleInfo(name = "PhaseESP", description = "lang.module.PhaseESP", category = "RENDER")
public class PhaseESP extends Module {

    public final NumberSetting extraRange = new NumberSetting("Extra Range", this, 0, 0, 3, true);
    public final BooleanSetting outline = new BooleanSetting("Outline", this, true);
    public final BooleanSetting fill = new BooleanSetting("Fill", this, true);

    public final ColorSetting goodColor = new ColorSetting("Good Color", this, new Color(0, 178, 0)).withBlockedAlpha();
    public final ColorSetting normalColor = new ColorSetting("Normal Color", this, new Color(0, 0, 178)).withBlockedAlpha();
    public final ColorSetting dangerColor = new ColorSetting("Danger Color", this, new Color(178, 0, 0)).withBlockedAlpha();


    private final List<Block> normalBlocks = Arrays.asList(Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN);
    private final List<Block> goodBlocks = Arrays.asList(Blocks.BEDROCK, Blocks.BARRIER, Blocks.END_PORTAL_FRAME, Blocks.COMMAND_BLOCK, Blocks.STRUCTURE_BLOCK);

    List<RenderBox> boxes = new CopyOnWriteArrayList<>();

    @EventSubscriber
    public void onRender (RenderWorldLastEvent e){
        List<RenderBox> renderBoxes = new CopyOnWriteArrayList<>();
        if (nullCheck() || mc.player == null ||!mc.player.isOnGround()) return;
        for (Vec3i vec : getVectors()) {
            BlockPos blockPos = BlockPos.ofFloored(mc.player.getX() + vec.getX(), mc.player.getY(), mc.player.getZ() + vec.getZ());
            BlockPos blockPosy = BlockPos.ofFloored(mc.player.getX() + vec.getX(), mc.player.getY() - 1, mc.player.getZ() + vec.getZ());
            Box box = BlockUtils.createBox(blockPos, 0.5, 0.5, 0.03, false);
            if (blockPos == null || mc.world.isAir(blockPos)) continue;

            Block block = mc.world.getBlockState(blockPos).getBlock();
            Block downBlock = mc.world.getBlockState(blockPosy).getBlock();

            if (goodBlocks.contains(block) && goodBlocks.contains(downBlock)) {
                addGood(box, renderBoxes);
            } else
            if ((goodBlocks.contains(block) || normalBlocks.contains(block)) && (goodBlocks.contains(downBlock) || normalBlocks.contains(downBlock))) {
                addNormal(box, renderBoxes);
            } else addDanger(box, renderBoxes);
        }
        boxes = renderBoxes;
        BThackRender.boxRender.prepareBoxRender();
        BThackRender.boxRender.renderBoxes(new ArrayList<>(boxes));
        BThackRender.boxRender.stopBoxRender();
    }

    private void addGood(Box box, List<RenderBox> renderBoxes) {
        renderBoxes.add(new RenderBox(
                box,
                goodColor.getValue().getRed() / 255f,
                goodColor.getValue().getGreen() / 255f,
                goodColor.getValue().getBlue() / 255f,
                outline.getValue() ? 0.6f : 0,
                goodColor.getValue().getRed() / 255f,
                goodColor.getValue().getGreen() / 255f,
                goodColor.getValue().getBlue() / 255f,
                fill.getValue() ? 0.3f : 0
        ));
    }

    private void addNormal(Box box, List<RenderBox> renderBoxes) {
        renderBoxes.add(new RenderBox(
                box,
                normalColor.getValue().getRed() / 255f,
                normalColor.getValue().getGreen() / 255f,
                normalColor.getValue().getBlue() / 255f,
                outline.getValue() ? 0.6f : 0,
                normalColor.getValue().getRed() / 255f,
                normalColor.getValue().getGreen() / 255f,
                normalColor.getValue().getBlue() / 255f,
                fill.getValue() ? 0.3f : 0
        ));
    }

    private void addDanger(Box box, List<RenderBox> renderBoxes) {
        renderBoxes.add(new RenderBox(
                box,
                dangerColor.getValue().getRed() / 255f,
                dangerColor.getValue().getGreen() / 255f,
                dangerColor.getValue().getBlue() / 255f,
                outline.getValue() ? 0.6f : 0,
                dangerColor.getValue().getRed() / 255f,
                dangerColor.getValue().getGreen() / 255f,
                dangerColor.getValue().getBlue() / 255f,
                fill.getValue() ? 0.3f : 0
        ));
    }

    private List<Vec3i> getVectors() {
        List<Vec3i> vecList = new ArrayList<>();
        for (int x = -1 - extraRange.getValue().intValue(); x < 2 + extraRange.getValue().intValue(); x++)
            for (int z = -1 - extraRange.getValue().intValue(); z < 2 + extraRange.getValue().intValue(); z++)
                vecList.add(new Vec3i(x, 0, z));
        return vecList;
    }
}
