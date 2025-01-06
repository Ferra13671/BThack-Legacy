package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Box.RenderBox;
import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Events.Render.RenderWorldEvent;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
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
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.chunk.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Taken and modified from here   :3
//https://github.com/etianl/Trouser-Streak/blob/1.20.4/src/main/java/pwn/noobs/trouserstreak/modules/NewerNewChunks.java

public class NewChunks extends Module {

    public static ModeSetting page;

    //---------Search---------//
    //public static BooleanSetting oldChunksSearch; //Not Used
    //public static BooleanSetting newChunksSearch; //Not Used

    public static BooleanSetting blockUpdateSearch;
    public static BooleanSetting beingUpdatedSearch;
    public static BooleanSetting paletteSearch;
    public static BooleanSetting liquidSearch;
    public static BooleanSetting overworldOldCheck;
    public static BooleanSetting netherOldCheck;
    public static BooleanSetting endOldCheck;
    //------------------------//

    //---------Render---------//
    public static BooleanSetting newChunkRender;
    public static NumberSetting newDist;
    public static NumberSetting newY;
    public static NumberSetting newRed;
    public static NumberSetting newGreen;
    public static NumberSetting newBlue;
    public static NumberSetting newAlpha;
    public static NumberSetting newLRed;
    public static NumberSetting newLGreen;
    public static NumberSetting newLBlue;
    public static NumberSetting newLAlpha;

    public static BooleanSetting oldChunkRender;
    public static NumberSetting oldDist;
    public static NumberSetting oldY;
    public static NumberSetting oldRed;
    public static NumberSetting oldGreen;
    public static NumberSetting oldBlue;
    public static NumberSetting oldAlpha;
    public static NumberSetting oldLRed;
    public static NumberSetting oldLGreen;
    public static NumberSetting oldLBlue;
    public static NumberSetting oldLAlpha;
    //------------------------//


    public NewChunks() {
        super("NewChunks",
                "lang.module.NewChunks",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        page = new ModeSetting("Page", this, Arrays.asList("Search", "Render"));

        //---------Search---------//
        //oldChunksSearch = new BooleanSetting("Old Chunk", this, true, () -> page.getValue().equals("Search"));
        //newChunksSearch = new BooleanSetting("New Chunk", this, true, () -> page.getValue().equals("Search"));

        blockUpdateSearch = new BooleanSetting("Block Update", this, false, () -> page.getValue().equals("Search"));
        beingUpdatedSearch = new BooleanSetting("Being Update", this, true, () -> page.getValue().equals("Search"));
        paletteSearch = new BooleanSetting("Palette", this, true, () -> page.getValue().equals("Search"));
        liquidSearch = new BooleanSetting("Liquid", this, false, () -> page.getValue().equals("Search"));
        overworldOldCheck = new BooleanSetting("Overw. Old", this, true, () -> page.getValue().equals("Search"));
        netherOldCheck = new BooleanSetting("Nether Old", this, true, () -> page.getValue().equals("Search"));
        endOldCheck = new BooleanSetting("End Old", this, true, () -> page.getValue().equals("Search"));
        //------------------------//

        //---------Render---------//
        newChunkRender = new BooleanSetting("New Render", this, true, () -> page.getValue().equals("Render"));
        newDist = new NumberSetting("New Dist", this, 500, 100, 2000, false, () -> page.getValue().equals("Render") && newChunkRender.getValue());
        newY = new NumberSetting("New Y", this, 0, 0, 400, false, () -> page.getValue().equals("Render") && newChunkRender.getValue());
        newRed = new NumberSetting("New Red", this, 0, 0, 255, true, () -> page.getValue().equals("Render") && newChunkRender.getValue());
        newGreen = new NumberSetting("New Green", this, 255, 0, 255, true, () -> page.getValue().equals("Render") && newChunkRender.getValue());
        newBlue = new NumberSetting("New Blue", this, 0, 0, 255, true, () -> page.getValue().equals("Render") && newChunkRender.getValue());
        newAlpha = new NumberSetting("New Alpha", this, 100, 0, 255, true, () -> page.getValue().equals("Render") && newChunkRender.getValue());
        newLRed = new NumberSetting("New LRed", this, 0, 0, 255, true, () -> page.getValue().equals("Render") && newChunkRender.getValue());
        newLGreen = new NumberSetting("New LGreen", this, 255, 0, 255, true, () -> page.getValue().equals("Render") && newChunkRender.getValue());
        newLBlue = new NumberSetting("New LBlue", this, 0, 0, 255, true, () -> page.getValue().equals("Render") && newChunkRender.getValue());
        newLAlpha = new NumberSetting("New LAlpha", this, 255, 0, 255, true, () -> page.getValue().equals("Render") && newChunkRender.getValue());

        oldChunkRender = new BooleanSetting("Old Render", this, true, () -> page.getValue().equals("Render"));
        oldDist = new NumberSetting("Old Dist", this, 500, 100, 2000, false, () -> page.getValue().equals("Render") && oldChunkRender.getValue());
        oldY = new NumberSetting("Old Y", this, 0, 0, 400, false, () -> page.getValue().equals("Render") && oldChunkRender.getValue());
        oldRed = new NumberSetting("Old Red", this, 255, 0, 255, true, () -> page.getValue().equals("Render") && oldChunkRender.getValue());
        oldGreen = new NumberSetting("Old Green", this, 255, 0, 255, true, () -> page.getValue().equals("Render") && oldChunkRender.getValue());
        oldBlue = new NumberSetting("Old Blue", this, 0, 0, 255, true, () -> page.getValue().equals("Render") && oldChunkRender.getValue());
        oldAlpha = new NumberSetting("Old Alpha", this, 100, 0, 255, true, () -> page.getValue().equals("Render") && oldChunkRender.getValue());
        oldLRed = new NumberSetting("Old LRed", this, 255, 0, 255, true, () -> page.getValue().equals("Render") && oldChunkRender.getValue());
        oldLGreen = new NumberSetting("Old LGreen", this, 255, 0, 255, true, () -> page.getValue().equals("Render") && oldChunkRender.getValue());
        oldLBlue = new NumberSetting("Old LBlue", this, 0, 0, 255, true, () -> page.getValue().equals("Render") && oldChunkRender.getValue());
        oldLAlpha = new NumberSetting("Old LAlpha", this, 255, 0, 255, true, () -> page.getValue().equals("Render") && oldChunkRender.getValue());
        //------------------------//

        initSettings(
                page,



                //oldChunksSearch,
                //newChunksSearch,

                blockUpdateSearch,
                beingUpdatedSearch,
                paletteSearch,
                liquidSearch,
                overworldOldCheck,
                netherOldCheck,
                endOldCheck,



                newChunkRender,
                newDist,
                newY,
                newRed,
                newGreen,
                newBlue,
                newAlpha,
                newLRed,
                newLGreen,
                newLBlue,
                newLAlpha,

                oldChunkRender,
                oldDist,
                oldY,
                oldRed,
                oldGreen,
                oldBlue,
                oldAlpha,
                oldLRed,
                oldLGreen,
                oldLBlue,
                oldLAlpha
        );
    }

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
    private RegistryKey<World> prevDimension;

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
        if (!mc.world.getRegistryKey().equals(prevDimension)) {
            clearChunks();
        }
        prevDimension = mc.world.getRegistryKey();
    }

    @EventSubscriber
    public void onPacketReceive(PacketEvent.Receive e) {
        if (e.getPacket() instanceof AcknowledgeChunksC2SPacket) return;

        if (e.getPacket() instanceof ChunkDeltaUpdateS2CPacket packet && liquidSearch.getValue()) {
            packet.visitUpdates((pos, state) -> {
                ChunkPos chunkPos = new ChunkPos(pos);
                if (!state.getFluidState().isEmpty() && !state.getFluidState().isStill()) {
                    liquidDirsSearchAction(pos, chunkPos);
                }
            });
        } else if (e.getPacket() instanceof BlockUpdateS2CPacket packet) {
            ChunkPos chunkPos = new ChunkPos(packet.getPos());
            if (blockUpdateSearch.getValue()){
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

            if (overworldOldCheck.getValue() && mc.world.getRegistryKey() == World.OVERWORLD && chunk.getStatus().isAtLeast(ChunkStatus.FULL) && !chunk.isEmpty()) {
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

            if (netherOldCheck.getValue() && mc.world.getRegistryKey() == World.NETHER && chunk.getStatus().isAtLeast(ChunkStatus.FULL) && !chunk.isEmpty())
                if (!isOldGeneration && !isNewNetherGeneration(sections)) isOldGeneration = true;

            if (endOldCheck.getValue() && mc.world.getRegistryKey() == World.END && chunk.getStatus().isAtLeast(ChunkStatus.FULL) && !chunk.isEmpty()) {
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
                                        if (loops == 0 && mc.world.getRegistryKey() != World.END)
                                            firstChunkAppearsNew = true;
                                        if (mc.world.getRegistryKey() != World.NETHER && mc.world.getRegistryKey() != World.END)
                                            isNewSection++;
                                    }
                                    if (i2 == 1 && (blockPaletteEntry.getBlock() == Blocks.WATER || blockPaletteEntry.getBlock() == Blocks.STONE || blockPaletteEntry.getBlock() == Blocks.GRASS_BLOCK || blockPaletteEntry.getBlock() == Blocks.SNOW_BLOCK) && mc.world.getRegistryKey() != World.NETHER && mc.world.getRegistryKey() != World.END)
                                        isNewSection++;
                                    if (i2 == 2 && (blockPaletteEntry.getBlock() == Blocks.SNOW_BLOCK || blockPaletteEntry.getBlock() == Blocks.DIRT || blockPaletteEntry.getBlock() == Blocks.POWDER_SNOW) && mc.world.getRegistryKey() != World.NETHER && mc.world.getRegistryKey() != World.END)
                                        isNewSection++;
                                    if (loops == 4 && blockPaletteEntry.getBlock() == Blocks.BEDROCK && mc.world.getRegistryKey() != World.NETHER && mc.world.getRegistryKey() != World.END) {
                                        if (beingUpdatedSearch.getValue())
                                            chunkIsBeingUpdated = true;
                                    }
                                    if (blockPaletteEntry.getBlock() == Blocks.AIR && (mc.world.getRegistryKey() == World.NETHER || mc.world.getRegistryKey() == World.END))
                                        isBeingUpdatedSection++;
                                }
                                if (isBeingUpdatedSection >= 2) oldChunkQuantifier++;
                                if (isNewSection >= 2) newChunkQuantifier++;
                            }
                            if (mc.world.getRegistryKey() == World.END) {
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
                        if (beingUpdatedSearch.getValue() && (mc.world.getRegistryKey() == World.NETHER || mc.world.getRegistryKey() == World.END)) {
                            if ((((double) oldChunkQuantifier / loops) * 100) >= 25) chunkIsBeingUpdated = true; //oldPercentage >= 25
                        } else if (mc.world.getRegistryKey() != World.NETHER && mc.world.getRegistryKey() != World.END){
                            if ((((double) newChunkQuantifier / loops) * 100) >= 51) isNewChunk = true; //percentage >= 51
                        }
                    }
                } catch (Exception ex) {
                    if (beingUpdatedSearch.getValue() && (mc.world.getRegistryKey() == World.NETHER || mc.world.getRegistryKey() == World.END)) {
                        if ((((double) oldChunkQuantifier / loops) * 100) >= 25) chunkIsBeingUpdated = true; //oldPercentage >= 25
                    } else if (mc.world.getRegistryKey() != World.NETHER && mc.world.getRegistryKey() != World.END) {
                        if ((((double) newChunkQuantifier / loops) * 100) >= 51) isNewChunk = true; //percentage >= 51
                    }
                }

                if (firstChunkAppearsNew) isNewChunk = true;
                if (isNewChunk && !chunkIsBeingUpdated && ((mc.world.getRegistryKey() == World.END) ? isNewChunk : !isOldGeneration)) {
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
                    for (int y = mc.world.getBottomY(); y < mc.world.getTopY(); y++) {
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
    public void onRender(RenderWorldEvent.Last e) {
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
                        (float) (newLRed.getValue() / 255d),
                        (float) (newLGreen.getValue() / 255d),
                        (float) (newLBlue.getValue() / 255d),
                        (float) (newLAlpha.getValue() / 255d),
                        (float) (newRed.getValue() / 255d),
                        (float) (newGreen.getValue() / 255d),
                        (float) (newBlue.getValue() / 255d),
                        (float) (newAlpha.getValue() / 255d)
                ));
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void addOldChunkBoxes(Set<ChunkPos> chunks, List<RenderBox> renderBoxes, double oldRenderY) {
        for (ChunkPos c : chunks) {
            if (mc.getCameraEntity().getBlockPos().isWithinDistance(c.getStartPos(), oldDist.getValue() * 16)) {
                Box box = new Box(
                        c.getStartX(), oldRenderY, c.getStartZ(),
                        c.getStartX() + 16, oldRenderY, c.getStartZ() + 16);
                renderBoxes.add(new RenderBox(
                        box,
                        (float) (oldLRed.getValue() / 255d),
                        (float) (oldLGreen.getValue() / 255d),
                        (float) (oldLBlue.getValue() / 255d),
                        (float) (oldLAlpha.getValue() / 255d),
                        (float) (oldRed.getValue() / 255d),
                        (float) (oldGreen.getValue() / 255d),
                        (float) (oldBlue.getValue() / 255d),
                        (float) (oldAlpha.getValue() / 255d)
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
