package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Box.RenderBox;
import com.ferra13671.BThack.api.Events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

//TODO: Color change
public class PhaseESP extends Module {
    public final BooleanSetting outline = new BooleanSetting("Outline", this, true);
    public final BooleanSetting fill = new BooleanSetting("Fill", this, true);

    public PhaseESP() {
        super("PhaseESP",
                "lang.module.PhaseESP",
                KeyboardUtils.RELEASE,
                Module.MCategory.RENDER,
                false
        );
        initSettings(
                outline,
                fill
        );

    }
    BlockPos blockPos;
    BlockPos blockPosy;

    List<RenderBox> boxes = new CopyOnWriteArrayList<>();
    final List<Vec3i> phaseBlocksVectors = Arrays.asList(
            new Vec3i(1, 0, 0),
            new Vec3i(0, 0, 1),
            new Vec3i(1, 0, 1),
            new Vec3i(-1, 0, 0),
            new Vec3i(0, 0, -1),
            new Vec3i(-1, 0, -1),
            new Vec3i(1, 0, -1),
            new Vec3i(-1, 0, 1)
    );

    @EventSubscriber
    public void onRender (RenderWorldLastEvent e){
        List<RenderBox> renderBoxes = new CopyOnWriteArrayList<>();
        if (nullCheck() || mc.player == null ||!mc.player.isOnGround()) return;
        for (Vec3i vec : phaseBlocksVectors) {
            blockPos = BlockPos.ofFloored(mc.player.getX() + vec.getX(), mc.player.getY(), mc.player.getZ() + vec.getZ());
            blockPosy = BlockPos.ofFloored(mc.player.getX() + vec.getX(), mc.player.getY() - 1, mc.player.getZ() + vec.getZ());
            Box box = BlockUtils.createBox(blockPos, 0.5, 0.5, 0.03, false);
            if (blockPos == null || mc.world.isAir(blockPos)){
                continue;
            }
            if (mc.world.isAir(blockPosy)) {
                renderBoxes.add(new RenderBox(box, 0.7f, 0, 0, outline.getValue() ? 0.6f : 0, 0.7f, 0, 0, fill.getValue() ? 0.3f : 0));
                continue;
            }
            if (BlockUtils.canBreak(blockPos) || BlockUtils.canBreak(blockPosy)) {
                renderBoxes.add(new RenderBox(box, 0, 0, 0.7f, outline.getValue() ? 0.6f : 0, 0, 0, 0.7f, fill.getValue() ? 0.3f : 0));
                continue;
            }
            if (!BlockUtils.canBreak(blockPos) && !BlockUtils.canBreak(blockPosy)) {
                renderBoxes.add(new RenderBox(box, 0, 0.7f, 0, outline.getValue() ? 0.6f : 0, 0, 0.7f, 0, fill.getValue() ? 0.3f : 0));

            }
        }
        boxes = renderBoxes;
        BThackRender.boxRender.prepareBoxRender();
        BThackRender.boxRender.renderBoxes(new ArrayList<>(boxes));
        BThackRender.boxRender.stopBoxRender();
    }
}
