package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Box.RenderBox;
import com.ferra13671.BThack.Core.Render.Line.RenderLine;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Events.DisconnectEvent;
import com.ferra13671.BThack.api.Events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.PlayerUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.block.enums.TrialSpawnerState;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

import java.awt.*;
import java.util.*;
import java.util.List;

//Taken and modified from here   :3
//https://github.com/etianl/Trouser-Streak/blob/main/src/main/java/pwn/noobs/trouserstreak/modules/ActivatedSpawnerDetector.java

public class ActiveSpawnerDetect extends Module {

    public final ModeSetting page = new ModeSetting("Page", this, Arrays.asList("General", "Render"));

    //GENERAL
    public final BooleanSetting chatFeedback = new BooleanSetting("Chat Feedback", this, true, () -> page.getValue().equals("General"));
    public final BooleanSetting displayCords = new BooleanSetting("Display Cords", this, true, () -> page.getValue().equals("General"));
    public final BooleanSetting extraMessage = new BooleanSetting("Extra Message", this, true, () -> page.getValue().equals("General"));
    public final BooleanSetting lessSpam = new BooleanSetting("Less Spam", this, true, () -> page.getValue().equals("General"));
    public final BooleanSetting airChecker = new BooleanSetting("Check Air Disturbances", this, true, () -> page.getValue().equals("General"));
    public final BooleanSetting deactivatedSpawners = new BooleanSetting("Deactiv. Spawners", this, true, () -> page.getValue().equals("General"));
    public final BooleanSetting trialSpawners = new BooleanSetting("Trial Spawners", this, true, () -> page.getValue().equals("General"));
    public final NumberSetting deactivatedSpawnerDistance = new NumberSetting("Torch Scan Dist", this, 1, 1, 10, true, () -> page.getValue().equals("General"));

    //RENDER
    public final BooleanSetting lessRenderSpam = new BooleanSetting("Less Render Spam", this, true, () -> page.getValue().equals("Render"));
    public final NumberSetting renderDistance = new NumberSetting("Render Dist", this, 32, 6, 32, true, () -> page.getValue().equals("Render"));
    public final BooleanSetting removerenderdist = new BooleanSetting("No Outside Render", this, true, () -> page.getValue().equals("Render"));
    public final BooleanSetting trcr = new BooleanSetting("Tracers", this, true, () -> page.getValue().equals("Render"));
    //Colors
    public final ColorSetting spawnerColor = new ColorSetting("Spawner Color", this, new Color(251, 5, 5, 70), () -> page.getValue().equals("Render"));
    public final ColorSetting trialColor = new ColorSetting("Trial Color", this, new Color(255, 100, 0, 235), () -> page.getValue().equals("Render"));
    public final ColorSetting despawnerColor = new ColorSetting("Despawner Color", this, new Color(251, 5, 251, 235), () -> page.getValue().equals("Render"));

    public ActiveSpawnerDetect() {
        super("ActiveSpawnerDetect",
                "lang.module.ActiveSpawnerDetect",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        initSettings(
                page,

                chatFeedback,
                displayCords,
                extraMessage,
                lessSpam,
                airChecker,
                deactivatedSpawners,
                trialSpawners,
                deactivatedSpawnerDistance,

                lessRenderSpam,
                renderDistance,
                removerenderdist,
                trcr,
                spawnerColor,
                trialColor,
                despawnerColor
        );
    }
    private final Set<Block> goodBlocks = Sets.newHashSet(
            Blocks.CHEST,
            Blocks.BARREL,
            Blocks.HOPPER,
            Blocks.DISPENSER
    );

    private final Set<BlockPos> scannedPositions = Collections.synchronizedSet(new HashSet<>());
    private final Set<BlockPos> spawnerPositions = Collections.synchronizedSet(new HashSet<>());
    private final Set<BlockPos> trialspawnerPositions = Collections.synchronizedSet(new HashSet<>());
    private final Set<BlockPos> deactivatedSpawnerPositions = Collections.synchronizedSet(new HashSet<>());
    private final Set<BlockPos> noRenderPositions = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void onEnable() {
        super.onEnable();
        clearChunkData();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        clearChunkData();
    }

    @EventSubscriber
    public void onDisconnect(DisconnectEvent e) {
        clearChunkData();
    }

    @EventSubscriber
    public void onTick(ClientTickEvent event) {
        if (nullCheck()) return;

        int renderdistance = mc.options.getViewDistance().getValue();
        ChunkPos playerChunkPos = new ChunkPos(mc.player.getBlockPos());
        for (int chunkX = playerChunkPos.x - renderdistance; chunkX <= playerChunkPos.x + renderdistance; chunkX++) {
            for (int chunkZ = playerChunkPos.z - renderdistance; chunkZ <= playerChunkPos.z + renderdistance; chunkZ++) {
                WorldChunk chunk = mc.world.getChunk(chunkX, chunkZ);
                List<BlockEntity> blockEntities = new ArrayList<>(chunk.getBlockEntities().values());

                for (BlockEntity blockEntity : blockEntities) {
                    if (blockEntity instanceof MobSpawnerBlockEntity spawner){
                        boolean activatedSpawnerFound = false;
                        BlockPos pos = spawner.getPos();
                        BlockPos playerPos = new BlockPos(mc.player.getBlockX(), pos.getY(), mc.player.getBlockZ());
                        String monster = null;
                        if (spawner.getLogic().spawnEntry != null && spawner.getLogic().spawnEntry.getNbt().get("id") != null) monster = spawner.getLogic().spawnEntry.getNbt().get("id").toString();
                        if (playerPos.isWithinDistance(pos, renderDistance.getValue() * 16) && !trialspawnerPositions.contains(pos) && !noRenderPositions.contains(pos) && !deactivatedSpawnerPositions.contains(pos) && !spawnerPositions.contains(pos)){
                            if (airChecker.getValue() && (spawner.getLogic().spawnDelay == 20 || spawner.getLogic().spawnDelay == 0)){
                                boolean airFound = false;
                                boolean caveAirFound = false;
                                if (monster != null && !scannedPositions.contains(pos)){
                                    if (monster.contains("zombie") || monster.contains("skeleton") || monster.contains(":spider")) {
                                        for (int x = -2; x < 2; x++) {
                                            for (int y = -1; y < 3; y++) {
                                                for (int z = -2; z < 2; z++) {
                                                    BlockPos bpos = new BlockPos(pos.getX()+x,pos.getY()+y,pos.getZ()+z);
                                                    if (mc.world.getBlockState(bpos).getBlock() == Blocks.AIR) airFound = true;
                                                    if (mc.world.getBlockState(bpos).getBlock() == Blocks.CAVE_AIR) caveAirFound = true;
                                                    if (caveAirFound && airFound) break;
                                                }
                                            }
                                        }
                                        if (caveAirFound && airFound) {
                                            spawnerPositions.add(pos);
                                            activatedSpawnerFound = true;
                                            if (monster == ":spider") displayMessage("dungeon", pos, ":spider");
                                            else displayMessage("dungeon", pos, "null");
                                        }
                                    } else if (monster.contains("cave_spider")) {
                                        for (int x = -1; x < 2; x++) {
                                            for (int y = 0; y < 2; y++) {
                                                for (int z = -1; z < 2; z++) {
                                                    BlockPos bpos = new BlockPos(pos.getX()+x,pos.getY()+y,pos.getZ()+z);
                                                    if (mc.world.getBlockState(bpos).getBlock() == Blocks.AIR) airFound = true;
                                                    if (mc.world.getBlockState(bpos).getBlock() == Blocks.CAVE_AIR) caveAirFound = true;
                                                    if (caveAirFound && airFound) break;
                                                }
                                            }
                                        }
                                        if (caveAirFound && airFound) {
                                            spawnerPositions.add(pos);
                                            activatedSpawnerFound = true;
                                            displayMessage("cave_spider", pos, "null");
                                        }
                                    } else if (monster.contains("silverfish")) {
                                        for (int x = -3; x < 3+1; x++) {
                                            for (int y = -2; y < 3+1; y++) {
                                                for (int z = -3; z < 3+1; z++) {
                                                    BlockPos bpos = new BlockPos(pos.getX()+x,pos.getY()+y,pos.getZ()+z);
                                                    if (mc.world.getBlockState(bpos).getBlock() == Blocks.AIR) airFound = true;
                                                    if (mc.world.getBlockState(bpos).getBlock() == Blocks.CAVE_AIR) caveAirFound = true;
                                                    if (caveAirFound && airFound) break;
                                                }
                                            }
                                        }
                                        if (caveAirFound && airFound) {
                                            spawnerPositions.add(pos);
                                            activatedSpawnerFound = true;
                                            displayMessage("silverfish", pos, "null");
                                        }
                                    }
                                }
                                scannedPositions.add(pos);
                            } else if (spawner.getLogic().spawnDelay != 20) {
                                if (PlayerUtils.isInNether() && spawner.getLogic().spawnDelay == 0) return;
                                if (chatFeedback.getValue()) {
                                    if (monster != null){
                                        if (monster.contains("zombie") || monster.contains("skeleton") || monster.contains(":spider")) {
                                            if (monster == ":spider") displayMessage("dungeon", pos, ":spider");
                                            else displayMessage("dungeon", pos, "null");
                                        } else if (monster.contains("cave_spider")) {
                                            displayMessage("cave_spider", pos, "null");
                                        } else if (monster.contains("silverfish")) {
                                            displayMessage("silverfish", pos, "null");
                                        } else if (monster.contains("blaze")) {
                                            displayMessage("blaze", pos, "null");
                                        } else if (monster.contains("magma")) {
                                            displayMessage("magma", pos, "null");
                                        } else {
                                            if (displayCords.getValue()) ChatUtils.sendMessage("Detected Activated Spawner! Block Position: " + pos);
                                            else ChatUtils.sendMessage("Detected Activated Spawner!");
                                        }
                                    } else {
                                        if (displayCords.getValue()) ChatUtils.sendMessage("Detected Activated Spawner! Block Position: " + pos);
                                        else ChatUtils.sendMessage("Detected Activated Spawner!");
                                    }
                                }
                                spawnerPositions.add(pos);
                                activatedSpawnerFound = true;
                            }
                            if (activatedSpawnerFound) {
                                if (deactivatedSpawners.getValue()){
                                    boolean lightsFound = false;
                                    for (int x = (int) -deactivatedSpawnerDistance.getValue(); x < deactivatedSpawnerDistance.getValue() + 1; x++) {
                                        for (int y = (int) -deactivatedSpawnerDistance.getValue(); y < deactivatedSpawnerDistance.getValue() + 1; y++) {
                                            for (int z = (int) -deactivatedSpawnerDistance.getValue(); z < deactivatedSpawnerDistance.getValue() + 1; z++) {
                                                BlockPos bpos = new BlockPos(pos.getX()+x,pos.getY()+y,pos.getZ()+z);
                                                if (mc.world.getBlockState(bpos).getBlock() == Blocks.TORCH || mc.world.getBlockState(bpos).getBlock() == Blocks.SOUL_TORCH || mc.world.getBlockState(bpos).getBlock() == Blocks.REDSTONE_TORCH || mc.world.getBlockState(bpos).getBlock() == Blocks.JACK_O_LANTERN || mc.world.getBlockState(bpos).getBlock() == Blocks.GLOWSTONE || mc.world.getBlockState(bpos).getBlock() == Blocks.SHROOMLIGHT || mc.world.getBlockState(bpos).getBlock() == Blocks.OCHRE_FROGLIGHT || mc.world.getBlockState(bpos).getBlock() == Blocks.PEARLESCENT_FROGLIGHT || mc.world.getBlockState(bpos).getBlock() == Blocks.PEARLESCENT_FROGLIGHT || mc.world.getBlockState(bpos).getBlock() == Blocks.SEA_LANTERN || mc.world.getBlockState(bpos).getBlock() == Blocks.LANTERN || mc.world.getBlockState(bpos).getBlock() == Blocks.SOUL_LANTERN || mc.world.getBlockState(bpos).getBlock() == Blocks.CAMPFIRE || mc.world.getBlockState(bpos).getBlock() == Blocks.SOUL_CAMPFIRE){
                                                    lightsFound = true;
                                                    deactivatedSpawnerPositions.add(pos);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    if (chatFeedback.getValue() && lightsFound) ChatUtils.sendMessage("The Spawner has torches or other light blocks!");
                                }

                                boolean chestfound = false;
                                for (int x = -16; x < 17; x++) {
                                    for (int y = -16; y < 17; y++) {
                                        for (int z = -16; z < 17; z++) {
                                            BlockPos bpos = new BlockPos(pos.getX()+x, pos.getY()+y, pos.getZ()+z);
                                            if (goodBlocks.contains(mc.world.getBlockState(bpos).getBlock())) {
                                                chestfound = true;
                                                break;
                                            }
                                            Box box = new Box(bpos);
                                            List<ChestMinecartEntity> minecarts = mc.world.getEntitiesByClass(ChestMinecartEntity.class, box, entity -> true);
                                            if (!minecarts.isEmpty()) {
                                                chestfound = true;
                                                break;
                                            }
                                        }
                                        if (chestfound) break;
                                    }
                                    if (chestfound) break;
                                }
                                if (!chestfound && lessRenderSpam.getValue()){
                                    noRenderPositions.add(pos);
                                }
                                if (chatFeedback.getValue()) {
                                    if (lessSpam.getValue() && chestfound && extraMessage.getValue()) ChatUtils.sendMessage(Formatting.YELLOW + "There may be stashed items in the storage near the spawners!");
                                    else if (!lessSpam.getValue() && extraMessage.getValue()) ChatUtils.sendMessage(Formatting.YELLOW + "There may be stashed items in the storage near the spawners!");
                                }
                            }
                        }
                    }
                    if (blockEntity instanceof TrialSpawnerBlockEntity){
                        TrialSpawnerBlockEntity trialspawner = (TrialSpawnerBlockEntity) blockEntity;
                        BlockPos tPos = trialspawner.getPos();
                        BlockPos playerPos = new BlockPos(mc.player.getBlockX(), tPos.getY(), mc.player.getBlockZ());
                        if (playerPos.isWithinDistance(tPos, renderDistance.getValue() * 16) && trialSpawners.getValue() && !trialspawnerPositions.contains(tPos) && !noRenderPositions.contains(tPos) && !deactivatedSpawnerPositions.contains(tPos) && !spawnerPositions.contains(tPos) && trialspawner.getSpawnerState() != TrialSpawnerState.WAITING_FOR_PLAYERS) {
                            if (chatFeedback.getValue()) {
                                if (displayCords.getValue()) ChatUtils.sendMessage("Detected Activated " + Formatting.RED + "TRIAL" + Formatting.RESET + " Spawner! Block Position: " + tPos);
                                else ChatUtils.sendMessage("Detected Activated " + Formatting.RED + "TRIAL" + Formatting.RESET + " Spawner!");
                            }
                            trialspawnerPositions.add(tPos);
                            boolean chestfound = false;
                            for (int x = -14; x < 15; x++) {
                                for (int y = -14; y < 15; y++) {
                                    for (int z = -14; z < 15; z++) {
                                        BlockPos bpos = new BlockPos(tPos.getX()+x, tPos.getY()+y, tPos.getZ()+z);
                                        if (goodBlocks.contains(mc.world.getBlockState(bpos).getBlock())) {
                                            chestfound = true;
                                            break;
                                        }
                                        Box box = new Box(bpos);
                                        List<ChestMinecartEntity> minecarts = mc.world.getEntitiesByClass(ChestMinecartEntity.class, box, entity -> true);
                                        if (!minecarts.isEmpty()) {
                                            chestfound = true;
                                            break;
                                        }
                                    }
                                    if (chestfound) break;
                                }
                                if (chestfound) break;
                            }
                            if (!chestfound && lessRenderSpam.getValue()){
                                noRenderPositions.add(tPos);
                            }
                            if (chatFeedback.getValue()) {
                                if (lessSpam.getValue() && chestfound && extraMessage.getValue()) ChatUtils.sendMessage(Formatting.YELLOW + "There may be stashed items in the storage near the spawners!");
                                else if (!lessSpam.getValue() && extraMessage.getValue()) ChatUtils.sendMessage(Formatting.YELLOW + "There may be stashed items in the storage near the spawners!");
                            }
                        }
                    }
                }
            }
        }
        if (removerenderdist.getValue()) removeChunksOutsideRenderDistance();
    }

    @EventSubscriber
    public void onRender(RenderWorldLastEvent e) {
        List<RenderBox> renderBoxes = new ArrayList<>();
        List<RenderLine> renderLines = new ArrayList<>();

        if (spawnerColor.getValue().getAlpha() > 5 || despawnerColor.getValue().getAlpha() > 5) {
            synchronized (spawnerPositions) {
                for (BlockPos pos : spawnerPositions) {
                    BlockPos playerPos = new BlockPos(mc.player.getBlockX(), pos.getY(), mc.player.getBlockZ());
                    if (pos != null && playerPos.isWithinDistance(pos, renderDistance.getValue() * 16)) {
                        if (deactivatedSpawnerPositions.contains(pos)) {
                            renderBoxes.add(new RenderBox(
                                    BlockUtils.createBox(pos, 0.5, 0.5, 1, false),
                                    despawnerColor.getValue().getRed() / 255f,
                                    despawnerColor.getValue().getGreen() / 255f,
                                    despawnerColor.getValue().getBlue() / 255f,
                                    despawnerColor.getValue().getAlpha() / 255f,
                                    despawnerColor.getValue().getRed() / 255f,
                                    despawnerColor.getValue().getGreen() / 255f,
                                    despawnerColor.getValue().getBlue() / 255f,
                                    despawnerColor.getValue().getAlpha() / 255f
                            ));
                            if (trcr.getValue()) renderLines.add(new RenderLine(pos.toCenterPos(),
                                    despawnerColor.getValue().getRed() / 255f,
                                    despawnerColor.getValue().getGreen() / 255f,
                                    despawnerColor.getValue().getBlue() / 255f,
                                    1
                            ));
                        }
                        else {
                            renderBoxes.add(new RenderBox(
                                    BlockUtils.createBox(pos, 0.5, 0.5, 1, false),
                                    spawnerColor.getValue().getRed() / 255f,
                                    spawnerColor.getValue().getGreen() / 255f,
                                    spawnerColor.getValue().getBlue() / 255f,
                                    spawnerColor.getValue().getAlpha() / 255f,
                                    spawnerColor.getValue().getRed() / 255f,
                                    spawnerColor.getValue().getGreen() / 255f,
                                    spawnerColor.getValue().getBlue() / 255f,
                                    spawnerColor.getValue().getAlpha() / 255f
                            ));
                            if (trcr.getValue()) renderLines.add(new RenderLine(pos.toCenterPos(),
                                    spawnerColor.getValue().getRed() / 255f,
                                    spawnerColor.getValue().getGreen() / 255f,
                                    spawnerColor.getValue().getBlue() / 255f,
                                    1
                            ));
                        }
                    }
                }
            }
        }
        if (trialColor.getValue().getAlpha() > 5) {
            synchronized (trialspawnerPositions) {
                for (BlockPos pos : trialspawnerPositions) {
                    BlockPos playerPos = new BlockPos(mc.player.getBlockX(), pos.getY(), mc.player.getBlockZ());
                    if (pos != null && playerPos.isWithinDistance(pos, renderDistance.getValue() * 16)) {
                        renderBoxes.add(new RenderBox(BlockUtils.createBox(pos, 0.5, 0.5, 1, false),
                                trialColor.getValue().getRed() / 255f,
                                trialColor.getValue().getGreen() / 255f,
                                trialColor.getValue().getBlue() / 255f,
                                trialColor.getValue().getAlpha() / 255f,
                                trialColor.getValue().getRed() / 255f,
                                trialColor.getValue().getGreen() / 255f,
                                trialColor.getValue().getBlue() / 255f,
                                trialColor.getValue().getAlpha() / 255f
                        ));
                        if (trcr.getValue()) renderLines.add(new RenderLine(pos.toCenterPos(),
                                trialColor.getValue().getRed() / 255f,
                                trialColor.getValue().getGreen() / 255f,
                                trialColor.getValue().getBlue() / 255f,
                                1));
                    }
                }
            }
        }

        if (!renderBoxes.isEmpty()) {
            BThackRender.boxRender.prepareBoxRender();
            BThackRender.boxRender.renderBoxes(renderBoxes);
            BThackRender.boxRender.stopBoxRender();
        }
        if (!renderLines.isEmpty()) {
            BThackRender.lineRender.prepareLineRenderer();
            BThackRender.lineRender.renderLines(renderLines);
            BThackRender.lineRender.stopLineRenderer();
        }
    }

    private void displayMessage(String key, BlockPos pos, String key2) {
        if (chatFeedback.getValue()){
            switch (key) {
                case "dungeon" -> {
                    if (key2.equals(":spider")) {
                        if (mc.world.getBlockState(pos.down()).getBlock() == Blocks.BIRCH_PLANKS) {
                            if (displayCords.getValue())
                                ChatUtils.sendMessage("Detected Activated " + Formatting.RED + "WOODLAND MANSION" + Formatting.RESET + " Spawner! Block Position: " + pos);
                            else
                                ChatUtils.sendMessage("Detected Activated " + Formatting.RED + "WOODLAND MANSION" + Formatting.RESET + " Spawner!");
                        } else {
                            if (displayCords.getValue())
                                ChatUtils.sendMessage("Detected Activated " + Formatting.RED + "DUNGEON" + Formatting.RESET + " Spawner! Block Position: " + pos);
                            else
                                ChatUtils.sendMessage("Detected Activated " + Formatting.RED + "DUNGEON" + Formatting.RESET + " Spawner!");
                        }
                    } else {
                        if (displayCords.getValue())
                            ChatUtils.sendMessage("Detected Activated " + Formatting.RED + "DUNGEON" + Formatting.RESET + " Spawner! Block Position: " + pos);
                        else
                            ChatUtils.sendMessage("Detected Activated " + Formatting.RED + "DUNGEON" + Formatting.RESET + " Spawner!");
                    }
                }
                case "cave_spider" -> {
                    if (displayCords.getValue())
                        ChatUtils.sendMessage("Detected Activated " + Formatting.RED + "MINESHAFT" + Formatting.RESET + " Spawner! Block Position: " + pos);
                    else
                        ChatUtils.sendMessage("Detected Activated " + Formatting.RED + "MINESHAFT" + Formatting.RESET + " Spawner!");
                }
                case "silverfish" -> {
                    if (displayCords.getValue())
                        ChatUtils.sendMessage("Detected Activated " + Formatting.RED + "STRONGHOLD" + Formatting.RESET + " Spawner! Block Position: " + pos);
                    else
                        ChatUtils.sendMessage("Detected Activated " + Formatting.RED + "STRONGHOLD" + Formatting.RESET + " Spawner!");
                }
                case "blaze" -> {
                    if (displayCords.getValue())
                        ChatUtils.sendMessage("Detected Activated " + Formatting.RED + "FORTRESS" + Formatting.RESET + " Spawner! Block Position: " + pos);
                    else
                        ChatUtils.sendMessage("Detected Activated " + Formatting.RED + "FORTRESS" + Formatting.RESET + " Spawner!");
                }
                case "magma" -> {
                    if (displayCords.getValue())
                        ChatUtils.sendMessage("Detected Activated " + Formatting.RED + "BASTION" + Formatting.RESET + " Spawner! Block Position: " + pos);
                    else
                        ChatUtils.sendMessage("Detected Activated " + Formatting.RED + "BASTION" + Formatting.RESET + " Spawner!");
                }
                case null, default -> {
                    if (displayCords.getValue())
                        ChatUtils.sendMessage("Detected Activated Spawner! Block Position: " + pos);
                    else ChatUtils.sendMessage("Detected Activated Spawner!");
                }
            }
        }
    }

    private void removeChunksOutsideRenderDistance() {
        double renderDistanceBlocks = renderDistance.getValue() * 16;

        removeChunksOutsideRenderDistance(scannedPositions, renderDistanceBlocks);
        removeChunksOutsideRenderDistance(spawnerPositions, renderDistanceBlocks);
        removeChunksOutsideRenderDistance(deactivatedSpawnerPositions, renderDistanceBlocks);
        removeChunksOutsideRenderDistance(trialspawnerPositions, renderDistanceBlocks);
        removeChunksOutsideRenderDistance(noRenderPositions, renderDistanceBlocks);
    }

    private void removeChunksOutsideRenderDistance(Set<BlockPos> chunkSet, double renderDistanceBlocks) {
        chunkSet.removeIf(blockPos -> {
            BlockPos playerPos = new BlockPos(mc.player.getBlockX(), blockPos.getY(), mc.player.getBlockZ());
            return !playerPos.isWithinDistance(blockPos, renderDistanceBlocks);
        });
    }

    private void clearChunkData(){
        scannedPositions.clear();
        spawnerPositions.clear();
        deactivatedSpawnerPositions.clear();
        noRenderPositions.clear();
        trialspawnerPositions.clear();
    }
}
