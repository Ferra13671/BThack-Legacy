package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Box.RenderBox;
import com.ferra13671.BThack.Core.Render.Line.RenderLine;
import com.ferra13671.BThack.api.Events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.*;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.*;
import net.minecraft.block.enums.ChestType;
import net.minecraft.util.math.Box;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ChestESP extends Module {

    public final BooleanSetting tracers = new BooleanSetting("Tracers", this, false);
    public final ModeSetting traceMode = new ModeSetting("Trace Mode", this, Arrays.asList("All", "Select"), tracers::getValue);

    public final BooleanSetting chests = new BooleanSetting("Chests", this, true);
    public final ColorSetting chestColor = new ColorSetting("Chest Color", this, new Color(255, 135, 0), chests::getValue).withBlockedAlpha();
    public final BooleanSetting chestsTrace = new BooleanSetting("Chests Trace", this, true, () -> tracers.getValue() && traceMode.getValue().equals("Select"));

    public final BooleanSetting enderChests = new BooleanSetting("EnderChests", this, true);
    public final ColorSetting eChestColor = new ColorSetting("Ender Chest Color", this, new Color(204, 0, 204), enderChests::getValue).withBlockedAlpha();
    public final BooleanSetting eChestsTrace = new BooleanSetting("EChests Trace", this, true, () -> tracers.getValue() && traceMode.getValue().equals("Select"));

    public final BooleanSetting shulkers = new BooleanSetting("Shulkers", this, true);
    public final ColorSetting shulkerColor = new ColorSetting("Shulker Color", this, new Color(255, 76, 255), shulkers::getValue).withBlockedAlpha();
    public final BooleanSetting shulkersTrace = new BooleanSetting("Shulk. Trace", this, true, () -> tracers.getValue() && traceMode.getValue().equals("Select"));

    public final BooleanSetting barrels = new BooleanSetting("Barrels", this, true);
    public final ColorSetting barrelColor = new ColorSetting("Barrel Color", this, new Color(165, 64, 0), barrels::getValue).withBlockedAlpha();
    public final BooleanSetting barrelsTrace = new BooleanSetting("Barrels Trace", this, true, () -> tracers.getValue() && traceMode.getValue().equals("Select"));

    public final BooleanSetting hoppers = new BooleanSetting("Hoppers", this, false);
    public final ColorSetting hopperColor = new ColorSetting("Hopper Color", this, new Color(127, 127, 127), hoppers::getValue).withBlockedAlpha();
    public final BooleanSetting hoppersTrace = new BooleanSetting("Hoppers Trace", this, true, () -> tracers.getValue() && traceMode.getValue().equals("Select"));

    public final BooleanSetting droppers = new BooleanSetting("Droppers", this, false);
    public final ColorSetting dropperColor = new ColorSetting("Dropper Color", this, new Color(178, 178, 178), droppers::getValue).withBlockedAlpha();
    public final BooleanSetting droppersTrace = new BooleanSetting("Dropp. Trace", this, true, () -> tracers.getValue() && traceMode.getValue().equals("Select"));

    public final BooleanSetting dispensers = new BooleanSetting("Dispensers", this, false);
    public final ColorSetting dispenserColor = new ColorSetting("Dispenser Color", this, new Color(178, 178, 178), dispensers::getValue).withBlockedAlpha();
    public final BooleanSetting dispensersTrace = new BooleanSetting("Disp. Trace", this, true, () -> tracers.getValue() && traceMode.getValue().equals("Select"));

    public final NumberSetting renderRange = new NumberSetting("Range", this, 200, 50, 600, false);

    public ChestESP() {
        super("ChestESP",
                "lang.module.ChestESP",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        initSettings(
                chests,
                chestColor,
                chestsTrace,

                enderChests,
                eChestColor,
                eChestsTrace,

                shulkers,
                shulkerColor,
                shulkersTrace,

                barrels,
                barrelColor,
                barrelsTrace,

                hoppers,
                hopperColor,
                hoppersTrace,

                droppers,
                dropperColor,
                droppersTrace,

                dispensers,
                dispenserColor,
                dispensersTrace,


                tracers,
                traceMode,


                renderRange
        );
    }

    @EventSubscriber
    public void onRender(RenderWorldLastEvent e) {
        if (nullCheck()) return;

        ArrayList<RenderBox> boxes = new ArrayList<>();

        for (BlockEntity entity : ChunkUtils.getLoadedBlockEntitiesOnArrayList()) {
            if (MathUtils.getDistance(mc.player.getPos(), entity.getPos().toCenterPos()) > renderRange.getValue()) continue;

            Box box;

            box = BlockUtils.getBox(entity);
            if (box == null) continue;

            if (entity instanceof ChestBlockEntity && chests.getValue()) {
                BlockState state = entity.getCachedState();
                ChestType chestType = state.get(ChestBlock.CHEST_TYPE);

                if (chestType == ChestType.LEFT) continue;

                boxes.add(new RenderBox(
                        box,
                        chestColor.getValue().getRed() / 255f,
                        chestColor.getValue().getGreen() / 255f,
                        chestColor.getValue().getBlue() / 255f,
                        0.6f,
                        chestColor.getValue().getRed() / 255f,
                        chestColor.getValue().getGreen() / 255f,
                        chestColor.getValue().getBlue() / 255f,
                        0.4f
                ));
            }
            if (entity instanceof EnderChestBlockEntity && enderChests.getValue()) {
                boxes.add(new RenderBox(
                        box,
                        eChestColor.getValue().getRed() / 255f,
                        eChestColor.getValue().getGreen() / 255f,
                        eChestColor.getValue().getBlue() / 255f,
                        0.6f,
                        eChestColor.getValue().getRed() / 255f,
                        eChestColor.getValue().getGreen() / 255f,
                        eChestColor.getValue().getBlue() / 255f,
                        0.4f
                ));
            }
            if (entity instanceof ShulkerBoxBlockEntity && shulkers.getValue()) {
                boxes.add(new RenderBox(
                        box,
                        shulkerColor.getValue().getRed() / 255f,
                        shulkerColor.getValue().getGreen() / 255f,
                        shulkerColor.getValue().getBlue() / 255f,
                        0.6f,
                        shulkerColor.getValue().getRed() / 255f,
                        shulkerColor.getValue().getGreen() / 255f,
                        shulkerColor.getValue().getBlue() / 255f,
                        0.4f
                ));
            }
            if (entity instanceof BarrelBlockEntity && barrels.getValue()) {
                boxes.add(new RenderBox(
                        box,
                        barrelColor.getValue().getRed() / 255f,
                        barrelColor.getValue().getGreen() / 255f,
                        barrelColor.getValue().getBlue() / 255f,
                        0.6f,
                        barrelColor.getValue().getRed() / 255f,
                        barrelColor.getValue().getGreen() / 255f,
                        barrelColor.getValue().getBlue() / 255f,
                        0.4f
                ));
            }
            if (entity instanceof HopperBlockEntity && hoppers.getValue()) {
                boxes.add(new RenderBox(
                        box,
                        hopperColor.getValue().getRed() / 255f,
                        hopperColor.getValue().getGreen() / 255f,
                        hopperColor.getValue().getBlue() / 255f,
                        0.6f,
                        hopperColor.getValue().getRed() / 255f,
                        hopperColor.getValue().getGreen() / 255f,
                        hopperColor.getValue().getBlue() / 255f,
                        0.4f
                ));
            }
            if (entity instanceof DropperBlockEntity && droppers.getValue()) {
                boxes.add(new RenderBox(
                        box,
                        dropperColor.getValue().getRed() / 255f,
                        dropperColor.getValue().getGreen() / 255f,
                        dropperColor.getValue().getBlue() / 255f,
                        0.6f,
                        dropperColor.getValue().getRed() / 255f,
                        dropperColor.getValue().getGreen() / 255f,
                        dropperColor.getValue().getBlue() / 255f,
                        0.4f
                ));
            }
            if (entity instanceof DispenserBlockEntity && dispensers.getValue()) {
                boxes.add(new RenderBox(
                        box,
                        dispenserColor.getValue().getRed() / 255f,
                        dispenserColor.getValue().getGreen() / 255f,
                        dispenserColor.getValue().getBlue() / 255f,
                        0.6f,
                        dispenserColor.getValue().getRed() / 255f,
                        dispenserColor.getValue().getGreen() / 255f,
                        dispenserColor.getValue().getBlue() / 255f,
                        0.4f
                ));
            }
        }

        BThackRender.boxRender.prepareBoxRender();
        BThackRender.boxRender.renderBoxes(boxes);
        BThackRender.boxRender.stopBoxRender();
    }

    @EventSubscriber(priority = -1)
    public void onRender2(RenderWorldLastEvent e) {
        if (!tracers.getValue()) return;

        ArrayList<RenderLine> lines = new ArrayList<>();

        if (traceMode.getValue().equals("All")) {
            for (BlockEntity entity : ChunkUtils.getLoadedBlockEntitiesOnArrayList()) {
                if (entity instanceof ChestBlockEntity && chests.getValue()) {
                    lines.add(new RenderLine(
                            entity,
                            chestColor.getValue().getRed() / 255f,
                            chestColor.getValue().getGreen() / 255f,
                            chestColor.getValue().getBlue() / 255f,
                            1f
                    ));
                }
                if (entity instanceof EnderChestBlockEntity && enderChests.getValue()) {
                    lines.add(new RenderLine(
                            entity,
                            eChestColor.getValue().getRed() / 255f,
                            eChestColor.getValue().getGreen() / 255f,
                            eChestColor.getValue().getBlue() / 255f,
                            1f
                    ));
                }
                if (entity instanceof ShulkerBoxBlockEntity && shulkers.getValue()) {
                    lines.add(new RenderLine(
                            entity,
                            shulkerColor.getValue().getRed() / 255f,
                            shulkerColor.getValue().getGreen() / 255f,
                            shulkerColor.getValue().getBlue() / 255f,
                            1f
                    ));
                }
                if (entity instanceof BarrelBlockEntity && barrels.getValue()) {
                    lines.add(new RenderLine(
                            entity,
                            barrelColor.getValue().getRed() / 255f,
                            barrelColor.getValue().getGreen() / 255f,
                            barrelColor.getValue().getBlue() / 255f,
                            1f
                    ));
                }
                if (entity instanceof HopperBlockEntity && hoppers.getValue()) {
                    lines.add(new RenderLine(
                            entity,
                            hopperColor.getValue().getRed() / 255f,
                            hopperColor.getValue().getGreen() / 255f,
                            hopperColor.getValue().getBlue() / 255f,
                            1
                    ));
                }
                if (entity instanceof DropperBlockEntity && droppers.getValue()) {
                    lines.add(new RenderLine(
                            entity,
                            dropperColor.getValue().getRed() / 255f,
                            dropperColor.getValue().getGreen() / 255f,
                            dropperColor.getValue().getBlue() / 255f,
                            1f
                    ));
                }
                if (entity instanceof DispenserBlockEntity && dispensers.getValue()) {
                    lines.add(new RenderLine(
                            entity,
                            dispenserColor.getValue().getRed() / 255f,
                            dispenserColor.getValue().getGreen() / 255f,
                            dispenserColor.getValue().getBlue() / 255f,
                            1
                    ));
                }
            }
        } else {
            for (BlockEntity entity : ChunkUtils.getLoadedBlockEntitiesOnArrayList()) {
                if (chestsTrace.getValue()) {
                    if (entity instanceof ChestBlockEntity && chests.getValue()) {
                        lines.add(new RenderLine(
                                entity,
                                chestColor.getValue().getRed() / 255f,
                                chestColor.getValue().getGreen() / 255f,
                                chestColor.getValue().getBlue() / 255f,
                                1f
                        ));
                    }
                }
                if (eChestsTrace.getValue()) {
                    if (entity instanceof EnderChestBlockEntity && enderChests.getValue()) {
                        lines.add(new RenderLine(
                                entity,
                                eChestColor.getValue().getRed() / 255f,
                                eChestColor.getValue().getGreen() / 255f,
                                eChestColor.getValue().getBlue() / 255f,
                                1f
                        ));
                    }
                }
                if (shulkersTrace.getValue()) {
                    if (entity instanceof ShulkerBoxBlockEntity && shulkers.getValue()) {
                        lines.add(new RenderLine(
                                entity,
                                shulkerColor.getValue().getRed() / 255f,
                                shulkerColor.getValue().getGreen() / 255f,
                                shulkerColor.getValue().getBlue() / 255f,
                                1f
                        ));
                    }
                }
                if (barrelsTrace.getValue()) {
                    if (entity instanceof BarrelBlockEntity && barrels.getValue()) {
                        lines.add(new RenderLine(
                                entity,
                                barrelColor.getValue().getRed() / 255f,
                                barrelColor.getValue().getGreen() / 255f,
                                barrelColor.getValue().getBlue() / 255f,
                                1f
                        ));
                    }
                }
                if (hoppersTrace.getValue()) {
                    if (entity instanceof HopperBlockEntity && hoppers.getValue()) {
                        lines.add(new RenderLine(
                                entity,
                                hopperColor.getValue().getRed() / 255f,
                                hopperColor.getValue().getGreen() / 255f,
                                hopperColor.getValue().getBlue() / 255f,
                                1
                        ));
                    }
                }
                if (droppersTrace.getValue()) {
                    if (entity instanceof DropperBlockEntity && droppers.getValue()) {
                        lines.add(new RenderLine(
                                entity,
                                dropperColor.getValue().getRed() / 255f,
                                dropperColor.getValue().getGreen() / 255f,
                                dropperColor.getValue().getBlue() / 255f,
                                1f
                        ));
                    }
                }
                if (dispensersTrace.getValue()) {
                    if (entity instanceof DispenserBlockEntity && dispensers.getValue()) {
                        lines.add(new RenderLine(
                                entity,
                                dispenserColor.getValue().getRed() / 255f,
                                dispenserColor.getValue().getGreen() / 255f,
                                dispenserColor.getValue().getBlue() / 255f,
                                1
                        ));
                    }
                }
            }
        }

        if (!lines.isEmpty()) {
            BThackRender.lineRender.prepareLineRenderer();
            BThackRender.lineRender.renderLines(lines);
            BThackRender.lineRender.stopLineRenderer();
        }
    }
}