package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Box.RenderBox;
import com.ferra13671.BThack.api.Events.Block.UseBlockEvent;
import com.ferra13671.BThack.api.Events.GuiOpenEvent;
import com.ferra13671.BThack.api.Events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class LastOpenChest extends Module {

    public final ColorSetting colorSet = new ColorSetting("Color", this, new Color(0, 255, 255)).withBlockedAlpha();

    public LastOpenChest() {
        super("LastOpenChest",
                "lang.module.LastOpenChest",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        initSettings(
                colorSet
        );
    }

    private BlockPos chestPos;
    private boolean needRender = false;

    @EventSubscriber
    public void onUseBlock(UseBlockEvent e) {
        if (isChest(e.getBlockHitResult().getBlockPos())) {
            chestPos = e.getBlockHitResult().getBlockPos();
            needRender = false;
        }
    }


    @EventSubscriber
    public void onGuiOpen(GuiOpenEvent e) {

        if (e.getScreen() instanceof GenericContainerScreen || e.getScreen() instanceof ShulkerBoxScreen) {
            if (chestPos != null) {
                if (MathUtils.getDistance(mc.player.getPos(), new Vec3d(chestPos.getX(), chestPos.getY(), chestPos.getZ())) > 20) {
                    chestPos = null;
                    needRender = false;
                } else {
                    needRender = true;
                }
            }
        }
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (chestPos != null) {
            if (!isChest(chestPos))
                chestPos = null;
        }
    }

    @EventSubscriber
    public void onRender(RenderWorldLastEvent e) {
        if (chestPos != null && needRender) {
            float _red = colorSet.getValue().getRed() / 255f;
            float _green = colorSet.getValue().getGreen() / 255f;
            float _blue = colorSet.getValue().getBlue() / 255f;

            Box box = BlockUtils.getBoundingBox(chestPos);
            if (box == null) return;

            BThackRender.boxRender.prepareBoxRender();
            BThackRender.boxRender.renderBoxes(new ArrayList<>(Arrays.asList(new RenderBox(box, _red, _green, _blue, 0.6f, _red, _green, _blue, 0.4f))));
            BThackRender.boxRender.stopBoxRender();
        }
    }

    private boolean isChest(BlockPos pos) {
        Block block = mc.world.getBlockState(pos).getBlock();
        return block == Blocks.CHEST || block == Blocks.ENDER_CHEST || block == Blocks.TRAPPED_CHEST || block == Blocks.BARREL || block instanceof ShulkerBoxBlock;
    }
}
