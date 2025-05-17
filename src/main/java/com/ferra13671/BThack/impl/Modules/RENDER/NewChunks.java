package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Box.RenderBox;
import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.*;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.PlayerUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.c2s.play.AcknowledgeChunksC2SPacket;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.chunk.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Taken and modified from here   :3
//https://github.com/etianl/Trouser-Streak/blob/1.20.4/src/main/java/pwn/noobs/trouserstreak/modules/NewerNewChunks.java
@ModuleInfo(name = "NewChunks", description = "lang.module.NewChunks", category = "RENDER")
public class NewChunks extends Module {

    public final CategorySetting searchCategory = new CategorySetting("Search", this);
    public final BooleanSetting blockUpdateSearch = new BooleanSetting("Block Update", this, false).inCategory(searchCategory);
    public final BooleanSetting beingUpdatedSearch = new BooleanSetting("Being Update", this, true).inCategory(searchCategory);
    public final BooleanSetting paletteSearch = new BooleanSetting("Palette", this, true).inCategory(searchCategory);
    public final BooleanSetting liquidSearch = new BooleanSetting("Liquid", this, false).inCategory(searchCategory);
    public final BooleanSetting overworldOldCheck = new BooleanSetting("Overw. Old", this, true).inCategory(searchCategory);
    public final BooleanSetting netherOldCheck = new BooleanSetting("Nether Old", this, true).inCategory(searchCategory);
    public final BooleanSetting endOldCheck = new BooleanSetting("End Old", this, true).inCategory(searchCategory);

    public final CategorySetting renderCategory = new CategorySetting("Render", this);
    public final CategorySetting newChunksCategory = new CategorySetting("New Chunks", this).inCategory(renderCategory);
    public final BooleanSetting newChunkRender = new BooleanSetting("New Render", this, true).inCategory(newChunksCategory);
    public final NumberSetting newDist = new NumberSetting("New Dist", this, 350, 100, 2000, false, newChunkRender::getValue).inCategory(newChunksCategory);
    public final NumberSetting newY = new NumberSetting("New Y", this, 0, 0, 400, false, newChunkRender::getValue).inCategory(newChunksCategory);
    public final ColorSetting newColor = new ColorSetting("New Color", this, new Color(0, 255, 0, 100), newChunkRender::getValue).inCategory(newChunksCategory);
    public final ColorSetting newLineColor = new ColorSetting("New Line Color", this, new Color(0, 255, 0, 255), newChunkRender::getValue).inCategory(newChunksCategory);
    public final CategorySetting oldChunksCategory = new CategorySetting("Old Chunks", this).inCategory(renderCategory);
    public final BooleanSetting oldChunkRender = new BooleanSetting("Old Render", this, true).inCategory(oldChunksCategory);
    public final NumberSetting oldDist = new NumberSetting("Old Dist", this, 350, 100, 2000, false, oldChunkRender::getValue).inCategory(oldChunksCategory);
    public final NumberSetting oldY = new NumberSetting("Old Y", this, 0, 0, 400, false, oldChunkRender::getValue).inCategory(oldChunksCategory);
    public final ColorSetting oldColor = new ColorSetting("Old Color", this, new Color(255, 255, 0, 100), oldChunkRender::getValue).inCategory(oldChunksCategory);
    public final ColorSetting oldLineColor = new ColorSetting("Old Line Color", this, new Color(255, 255, 0, 255), oldChunkRender::getValue).inCategory(oldChunksCategory);


    private static final Direction[] searchDirs = new Direction[] { Direction.EAST, Direction.NORTH, Direction.WEST, Direction.SOUTH, Direction.UP };

    private final Set<ChunkPos> newChunks = Collections.synchronizedSet(new HashSet<>());
    private final Set<ChunkPos> tickExploitChunks = Collections.synchronizedSet(new HashSet<>());

    private final Set<ChunkPos> oldChunks = Collections.synchronizedSet(new HashSet<>());
    private final Set<ChunkPos> beingUpdatedOldChunks = Collections.synchronizedSet(new HashSet<>());
    private final Set<ChunkPos> oldGenerationOldChunks = Collections.synchronizedSet(new HashSet<>());

    private final ExecutorService taskExecutor = Executors.newCachedThreadPool();

    private final Set<Block> oreBlocks = new HashSet<>(Arrays.asList(
            Blocks.COAL_ORE,
            Blocks.DEEPSLATE_COAL_ORE,
            Blocks.COPPER_ORE,
            Blocks.DEEPSLATE_COPPER_ORE,
            Blocks.IRON_ORE,
            Blocks.DEEPSLATE_IRON_ORE,
            Blocks.GOLD_ORE,
            Blocks.DEEPSLATE_GOLD_ORE,
            Blocks.LAPIS_ORE,
            Blocks.DEEPSLATE_LAPIS_ORE,
            Blocks.DIAMOND_ORE,
            Blocks.DEEPSLATE_DIAMOND_ORE,
            Blocks.REDSTONE_ORE,
            Blocks.DEEPSLATE_REDSTONE_ORE,
            Blocks.EMERALD_ORE,
            Blocks.DEEPSLATE_EMERALD_ORE
    ));
    private final Set<Block> deepslateBlocks = new HashSet<>(Arrays.asList(
            Blocks.DEEPSLATE,
            Blocks.DEEPSLATE_COPPER_ORE,
            Blocks.DEEPSLATE_IRON_ORE,
            Blocks.DEEPSLATE_COAL_ORE,
            Blocks.DEEPSLATE_REDSTONE_ORE,
            Blocks.DEEPSLATE_EMERALD_ORE,
            Blocks.DEEPSLATE_GOLD_ORE,
            Blocks.DEEPSLATE_LAPIS_ORE,
            Blocks.DEEPSLATE_DIAMOND_ORE
    ));
    private final Set<Block> newOverworldBlocks = new HashSet<>(Arrays.asList(
            Blocks.DEEPSLATE,
            Blocks.AMETHYST_BLOCK,
            Blocks.BUDDING_AMETHYST,
            Blocks.AZALEA,
            Blocks.FLOWERING_AZALEA,
            Blocks.BIG_DRIPLEAF,
            Blocks.BIG_DRIPLEAF_STEM,
            Blocks.SMALL_DRIPLEAF,
            Blocks.CAVE_VINES,
            Blocks.CAVE_VINES_PLANT,
            Blocks.SPORE_BLOSSOM,
            Blocks.COPPER_ORE,
            Blocks.DEEPSLATE_COPPER_ORE,
            Blocks.DEEPSLATE_IRON_ORE,
            Blocks.DEEPSLATE_COAL_ORE,
            Blocks.DEEPSLATE_REDSTONE_ORE,
            Blocks.DEEPSLATE_EMERALD_ORE,
            Blocks.DEEPSLATE_GOLD_ORE,
            Blocks.DEEPSLATE_LAPIS_ORE,
            Blocks.DEEPSLATE_DIAMOND_ORE,
            Blocks.GLOW_LICHEN,
            Blocks.RAW_COPPER_BLOCK,
            Blocks.RAW_IRON_BLOCK,
            Blocks.DRIPSTONE_BLOCK,
            Blocks.MOSS_BLOCK,
            Blocks.MOSS_CARPET,
            Blocks.POINTED_DRIPSTONE,
            Blocks.SMOOTH_BASALT,
            Blocks.TUFF,
            Blocks.CALCITE,
            Blocks.HANGING_ROOTS,
            Blocks.ROOTED_DIRT,
            Blocks.AZALEA_LEAVES,
            Blocks.FLOWERING_AZALEA_LEAVES,
            Blocks.POWDER_SNOW
    ));
    private final Set<Block> newNetherBlocks = new HashSet<>(Arrays.asList(
            Blocks.ANCIENT_DEBRIS,
            Blocks.BASALT,
            Blocks.BLACKSTONE,
            Blocks.GILDED_BLACKSTONE,
            Blocks.POLISHED_BLACKSTONE_BRICKS,
            Blocks.CRIMSON_STEM,
            Blocks.CRIMSON_NYLIUM,
            Blocks.NETHER_GOLD_ORE,
            Blocks.WARPED_NYLIUM,
            Blocks.WARPED_STEM,
            Blocks.TWISTING_VINES,
            Blocks.WEEPING_VINES,
            Blocks.BONE_BLOCK,
            Blocks.CHAIN,
            Blocks.OBSIDIAN,
            Blocks.CRYING_OBSIDIAN,
            Blocks.SOUL_SOIL,
            Blocks.SOUL_FIRE
    ));
    private String prevDimension;

    @Override
    public void onEnable() {
        super.onEnable();
        clearChunks();
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) {
            clearChunks();
            setToggled(false);
            return;
        }
        if (!PlayerUtils.getDimension().equals(prevDimension)) {
            clearChunks();
        }
        prevDimension = PlayerUtils.getDimension();
    }

    @EventSubscriber
    public void onPacketReceive(PacketEvent.Receive e) {
        if (e.getPacket() instanceof AcknowledgeChunksC2SPacket) return;

        if (e.getPacket() instanceof ChunkDeltaUpdateS2CPacket packet && liquidSearch.getValue()) {
            packet.visitUpdates((pos, state) -> {
                ChunkPos chunkPos = new ChunkPos(pos);
                if (newChunks.contains(chunkPos) || oldChunks.contains(chunkPos)) return;
                if (!state.getFluidState().isEmpty() && !state.getFluidState().isStill()) {
                    liquidDirsSearchAction(pos, chunkPos);
                }
            });
        } else if (e.getPacket() instanceof BlockUpdateS2CPacket packet) {
            ChunkPos chunkPos = new ChunkPos(packet.getPos());
            if (newChunks.contains(chunkPos) || oldChunks.contains(chunkPos)) return;
            if (blockUpdateSearch.getValue()) {
                blockUpdateSearchAction(chunkPos);
            }
            if (!packet.getState().getFluidState().isEmpty() && !packet.getState().getFluidState().isStill() && liquidSearch.getValue()) {
                liquidDirsSearchAction(packet.getPos(), chunkPos);
            }
        } else if (!(e.getPacket() instanceof AcknowledgeChunksC2SPacket) && e.getPacket() instanceof ChunkDataS2CPacket packet && mc.world != null) {
            chunkDataSearchAction(packet);
        }
    }

    public void liquidDirsSearchAction(BlockPos pos, ChunkPos chunkPos) {
        for (Direction dir: searchDirs) {
            try {
                if (mc.world.getBlockState(pos.offset(dir)).getFluidState().isStill() && (!oldGenerationOldChunks.contains(chunkPos) && !beingUpdatedOldChunks.contains(chunkPos) && !newChunks.contains(chunkPos) && !oldChunks.contains(chunkPos))) {
                    tickExploitChunks.remove(chunkPos);
                    newChunks.add(chunkPos);
                    return;
                }
            } catch (Exception ignored) {}
        }
    }

    public void blockUpdateSearchAction(ChunkPos chunkPos) {
        try {
            if (!oldGenerationOldChunks.contains(chunkPos) && !beingUpdatedOldChunks.contains(chunkPos) && !tickExploitChunks.contains(chunkPos) && !oldChunks.contains(chunkPos) && !newChunks.contains(chunkPos)){
                tickExploitChunks.add(chunkPos);
            }
        } catch (Exception ignored) {}
    }

    public void chunkDataSearchAction(ChunkDataS2CPacket packet) {
        ChunkPos oldPos = new ChunkPos(packet.getChunkX(), packet.getChunkZ());
        if (newChunks.contains(oldPos) || oldChunks.contains(oldPos)) return;

        if (mc.world.getChunkManager().getChunk(packet.getChunkX(), packet.getChunkZ()) == null) {
            WorldChunk chunk = new WorldChunk(mc.world, oldPos);
            try {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    chunk.loadFromPacket(packet.getChunkData().getSectionsDataBuf(), new NbtCompound(),
                            packet.getChunkData().getBlockEntities(packet.getChunkX(), packet.getChunkZ()));
                }, taskExecutor);
                future.join();
            } catch (CompletionException ignored) {}

            boolean isOldGeneration = false;
            boolean chunkIsBeingUpdated = false;
            ChunkSection[] sections = chunk.getSectionArray();

            if (overworldOldCheck.getValue() && PlayerUtils.isInOverworld() && chunk.getStatus().isAtLeast(ChunkStatus.FULL) && !chunk.isEmpty()) {
                boolean foundAnyOre = false;
                boolean isNewOverworldGeneration = false;

                for (int i = 0; i < 17; i++) {
                    ChunkSection section = sections[i];
                    if (section != null && !section.isEmpty()) {
                        for (int x = 0; x < 16; x++) {
                            for (int y = 0; y < 16; y++) {
                                for (int z = 0; z < 16; z++) {
                                    if (!foundAnyOre && oreBlocks.contains(section.getBlockState(x, y, z).getBlock())) foundAnyOre = true; //prevent false flags in flat world
                                    if (((y >= 5 && i == 4) || i > 4) && !isNewOverworldGeneration && (newOverworldBlocks.contains(section.getBlockState(x, y, z).getBlock()) || deepslateBlocks.contains(section.getBlockState(x, y, z).getBlock()))) {
                                        isNewOverworldGeneration = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if (foundAnyOre && !isNewOverworldGeneration) isOldGeneration = true;
            }

            if (netherOldCheck.getValue() && PlayerUtils.isInNether() && chunk.getStatus().isAtLeast(ChunkStatus.FULL) && !chunk.isEmpty())
                if (!isOldGeneration && !isNewNetherGeneration(sections)) isOldGeneration = true;

            if (endOldCheck.getValue() && PlayerUtils.isInEnd() && chunk.getStatus().isAtLeast(ChunkStatus.FULL) && !chunk.isEmpty()) {
                ChunkSection section = chunk.getSection(0);
                ReadableContainer<RegistryEntry<Biome>> biomesContainer = section.getBiomeContainer();
                if (biomesContainer instanceof PalettedContainer<RegistryEntry<Biome>> biomesPaletteContainer) {
                    Palette<RegistryEntry<Biome>> biomePalette = biomesPaletteContainer.data.palette();
                    for (int i = 0; i < biomePalette.getSize(); i++) {
                        if (biomePalette.get(i).getKey().get() == BiomeKeys.THE_END) {
                            isOldGeneration = true;
                            break;
                        }
                    }
                }
            }

            if (paletteSearch.getValue()) {
                boolean isNewChunk = false;

                boolean firstChunkAppearsNew = false;
                int loops = 0;
                int newChunkQuantifier = 0;
                int oldChunkQuantifier = 0;
                try {
                    for (ChunkSection section : sections) {
                        if (section != null) {
                            if (!section.isEmpty()) {
                                int isNewSection = 0;
                                int isBeingUpdatedSection = 0;

                                PalettedContainer<BlockState> blockStatesContainer = section.getBlockStateContainer();
                                Palette<BlockState> blockStatePalette = blockStatesContainer.data.palette();
                                int blockPaletteLength = blockStatePalette.getSize();

                                if (blockStatePalette instanceof BiMapPalette<BlockState>) {
                                    Set<BlockState> blockStates = new HashSet<>();

                                    for (int x = 0; x < 16; x++)
                                        for (int y = 0; y < 16; y++)
                                            for (int z = 0; z < 16; z++)
                                                blockStates.add(blockStatesContainer.get(x, y, z));

                                    int bstatesSize = blockStates.size();
                                    if (bstatesSize <= 1) bstatesSize = blockPaletteLength;
                                    if (bstatesSize < blockPaletteLength) isNewSection = 2;
                                }

                                for (int i2 = 0; i2 < blockPaletteLength; i2++) {
                                    BlockState blockPaletteEntry = blockStatePalette.get(i2);

                                    if (i2 == 0 && blockPaletteEntry.getBlock() == Blocks.AIR) {
                                        if (loops == 0 && !PlayerUtils.isInEnd())
                                            firstChunkAppearsNew = true;
                                        if (!PlayerUtils.isInNether() && !PlayerUtils.isInEnd())
                                            isNewSection++;
                                    }
                                    if (i2 == 1 && (blockPaletteEntry.getBlock() == Blocks.WATER || blockPaletteEntry.getBlock() == Blocks.STONE || blockPaletteEntry.getBlock() == Blocks.GRASS_BLOCK || blockPaletteEntry.getBlock() == Blocks.SNOW_BLOCK) && !PlayerUtils.isInNether() && !PlayerUtils.isInEnd())
                                        isNewSection++;
                                    if (i2 == 2 && (blockPaletteEntry.getBlock() == Blocks.SNOW_BLOCK || blockPaletteEntry.getBlock() == Blocks.DIRT || blockPaletteEntry.getBlock() == Blocks.POWDER_SNOW) && !PlayerUtils.isInNether() && !PlayerUtils.isInEnd())
                                        isNewSection++;
                                    if (loops == 4 && blockPaletteEntry.getBlock() == Blocks.BEDROCK && !PlayerUtils.isInNether() && !PlayerUtils.isInEnd()) {
                                        if (beingUpdatedSearch.getValue())
                                            chunkIsBeingUpdated = true;
                                    }
                                    if (blockPaletteEntry.getBlock() == Blocks.AIR && (PlayerUtils.isInNether() || PlayerUtils.isInEnd()))
                                        isBeingUpdatedSection++;
                                }
                                if (isBeingUpdatedSection >= 2) oldChunkQuantifier++;
                                if (isNewSection >= 2) newChunkQuantifier++;
                            }
                            if (PlayerUtils.isInEnd()) {
                                ReadableContainer<RegistryEntry<Biome>> biomesContainer = section.getBiomeContainer();
                                if (biomesContainer instanceof PalettedContainer<RegistryEntry<Biome>> biomesPaletteContainer) {
                                    Palette<RegistryEntry<Biome>> biomePalette = biomesPaletteContainer.data.palette();
                                    for (int i3 = 0; i3 < biomePalette.getSize(); i3++)
                                        if (i3 == 0 && biomePalette.get(i3).getKey().get() == BiomeKeys.PLAINS) isNewChunk = true;
                                      //if (!isNewChunk && i3 == 0 && biomePalette.get(i3).getKey().get() != BiomeKeys.THE_END) isNewChunk = false;
                                }
                            }
                            if (!section.isEmpty()) loops++;
                        }
                    }

                    if (loops > 0) {
                        if (beingUpdatedSearch.getValue() && (PlayerUtils.isInNether() || PlayerUtils.isInEnd())) {
                            if ((((double) oldChunkQuantifier / loops) * 100) >= 25) chunkIsBeingUpdated = true; //oldPercentage >= 25
                        } else if (!PlayerUtils.isInNether() && !PlayerUtils.isInEnd()){
                            if ((((double) newChunkQuantifier / loops) * 100) >= 51) isNewChunk = true; //percentage >= 51
                        }
                    }
                } catch (Exception ex) {
                    if (beingUpdatedSearch.getValue() && (PlayerUtils.isInNether() || PlayerUtils.isInEnd())) {
                        if ((((double) oldChunkQuantifier / loops) * 100) >= 25) chunkIsBeingUpdated = true; //oldPercentage >= 25
                    } else if (!PlayerUtils.isInNether() && !PlayerUtils.isInEnd()) {
                        if ((((double) newChunkQuantifier / loops) * 100) >= 51) isNewChunk = true; //percentage >= 51
                    }
                }

                if (firstChunkAppearsNew) isNewChunk = true;
                if (isNewChunk && !chunkIsBeingUpdated && ((PlayerUtils.isInEnd()) ? isNewChunk : !isOldGeneration)) {
                    if (addChunkWithCheck(oldPos, newChunks)) return;
                } else if (!isNewChunk && !chunkIsBeingUpdated && isOldGeneration) {
                    if (addChunkWithCheck(oldPos, oldGenerationOldChunks)) return;
                } else if (chunkIsBeingUpdated) {
                    if (addChunkWithCheck(oldPos, beingUpdatedOldChunks)) return;
                } else if (!isNewChunk) {
                    if (addChunkWithCheck(oldPos, oldChunks)) return;
                }
            }
            if (liquidSearch.getValue()) {
                for (int x = 0; x < 16; x++) {
                    for (int y = mc.world.getBottomY(); y < mc.world.getBottomY() + mc.world.getHeight(); y++) {
                        for (int z = 0; z < 16; z++) {
                            FluidState fluid = chunk.getFluidState(x, y, z);
                            try {
                                if (!oldGenerationOldChunks.contains(oldPos) && !beingUpdatedOldChunks.contains(oldPos) && !oldChunks.contains(oldPos) && !tickExploitChunks.contains(oldPos) && !newChunks.contains(oldPos) && !fluid.isEmpty() && !fluid.isStill()) {
                                    oldChunks.add(oldPos);
                                    return;
                                }
                            } catch (Exception ignored) {}
                        }
                    }
                }
            }
        }
    }

    private boolean isNewNetherGeneration(ChunkSection[] sections) {
        boolean isNewNetherGeneration = false;

        for (int i = 0; i < 8; i++) {
            ChunkSection section = sections[i];
            if (section != null && !section.isEmpty()) {
                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        for (int z = 0; z < 16; z++) {
                            if (!isNewNetherGeneration && newNetherBlocks.contains(section.getBlockState(x, y, z).getBlock())) {
                                isNewNetherGeneration = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return isNewNetherGeneration;
    }

    public boolean addChunkWithCheck(ChunkPos oldPos, Set<ChunkPos> set) {
        try {
            if (!oldGenerationOldChunks.contains(oldPos) && !beingUpdatedOldChunks.contains(oldPos) && !tickExploitChunks.contains(oldPos) && !oldChunks.contains(oldPos) && !newChunks.contains(oldPos)) {
                set.add(oldPos);
                return true;
            }
        } catch (Exception ignored) {}
        return false;
    }

    @EventSubscriber
    @SuppressWarnings({"unused", "ConstantConditions"})
    public void onRender(RenderWorldLastEvent e) {
        double newRenderY = mc.world.getBottomY() + newY.getValue();
        double oldRenderY = mc.world.getBottomY() + oldY.getValue();

        ArrayList<RenderBox> renderBoxes = new ArrayList<>();

        if (newChunkRender.getValue()) {
            synchronized (newChunks) {
                addNewChunkBoxes(newChunks, renderBoxes, newRenderY);
            }
            synchronized (tickExploitChunks) {
                addNewChunkBoxes(tickExploitChunks, renderBoxes, newRenderY);
            }
        }
        if (oldChunkRender.getValue()) {
            synchronized (oldChunks) {
                addOldChunkBoxes(oldChunks, renderBoxes, oldRenderY);
            }
            synchronized (beingUpdatedOldChunks) {
                addOldChunkBoxes(beingUpdatedOldChunks, renderBoxes, oldRenderY);
            }
            synchronized (oldGenerationOldChunks) {
                addOldChunkBoxes(oldGenerationOldChunks, renderBoxes, oldRenderY);
            }
        }

        if (!renderBoxes.isEmpty()) {
            BThackRender.boxRender.prepareBoxRender();
            BThackRender.boxRender.renderBoxes(renderBoxes);
            BThackRender.boxRender.stopBoxRender();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void addNewChunkBoxes(Set<ChunkPos> chunks, List<RenderBox> renderBoxes, double newRenderY) {
        for (ChunkPos c : chunks) {
            if (mc.getCameraEntity().getBlockPos().isWithinDistance(c.getStartPos(), newDist.getValue() * 16)) {
                Box box = new Box(
                        c.getStartX(), newRenderY, c.getStartZ(),
                        c.getStartX() + 16, newRenderY, c.getStartZ() + 16);
                renderBoxes.add(new RenderBox(
                        box,
                        (float) (newLineColor.getValue().getRed() / 255d),
                        (float) (newLineColor.getValue().getGreen() / 255d),
                        (float) (newLineColor.getValue().getBlue() / 255d),
                        (float) (newLineColor.getValue().getAlpha() / 255d),
                        (float) (newColor.getValue().getRed() / 255d),
                        (float) (newColor.getValue().getGreen() / 255d),
                        (float) (newColor.getValue().getBlue() / 255d),
                        (float) (newColor.getValue().getAlpha() / 255d)
                ));
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void addOldChunkBoxes(Set<ChunkPos> chunks, List<RenderBox> renderBoxes, double oldRenderY) {
        for (ChunkPos c : chunks) {
            if (mc.getCameraEntity().getBlockPos().isWithinDistance(c.getStartPos(), oldDist.getValue())) {
                Box box = new Box(
                        c.getStartX(), oldRenderY, c.getStartZ(),
                        c.getStartX() + 16, oldRenderY, c.getStartZ() + 16);
                renderBoxes.add(new RenderBox(
                        box,
                        (oldLineColor.getValue().getRed() / 255f),
                        (oldLineColor.getValue().getGreen() / 255f),
                        (oldLineColor.getValue().getBlue() / 255f),
                        (oldLineColor.getValue().getAlpha() / 255f),
                        (oldColor.getValue().getRed() / 255f),
                        (oldColor.getValue().getGreen() / 255f),
                        (oldColor.getValue().getBlue() / 255f),
                        (oldColor.getValue().getAlpha() / 255f)
                ));
            }
        }
    }

    public void clearChunks() {
        newChunks.clear();
        oldChunks.clear();
        beingUpdatedOldChunks.clear();
        oldGenerationOldChunks.clear();
        tickExploitChunks.clear();
    }
}
