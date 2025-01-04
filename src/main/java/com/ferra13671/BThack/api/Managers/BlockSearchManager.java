package com.ferra13671.BThack.api.Managers;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

public class BlockSearchManager implements Mc {

    private final HashSet<Block> searchBlocks = new HashSet<>();
    private final CopyOnWriteArrayList<BlockPos> results = new CopyOnWriteArrayList<>();

    public void start() {
        ThreadManager.startNewThread(thread -> {
            while (ModuleList.search.isEnabled()) {
                try {
                    if (!Module.nullCheck()) {
                        ArrayList<BlockPos> bloks = new ArrayList<>();
                        for (int x = (int) Math.floor(mc.player.getX() - 100); x <= Math.ceil(mc.player.getX() + 100); x++) {
                            for (int y = mc.world.getBottomY() + 1; y <= mc.world.getTopY(); y++) {
                                for (int z = (int) Math.floor(mc.player.getZ() - 100); z <= Math.ceil(mc.player.getZ() + 100); z++) {
                                    BlockPos pos = new BlockPos(x, y, z);
                                    if (mc.world.isAir(pos)) continue;
                                    if (needAdd(pos)) {
                                        bloks.add(pos);
                                    }
                                }
                            }
                        }

                        results.clear();
                        results.addAll(bloks);
                    } else Thread.yield();
                } catch (Exception ignored) {
                }
            }
        });
    }

    public void addBlockToSearch(Block block) {
        searchBlocks.add(block);
    }

    public void removeBlockToSearch(Block block) {
        searchBlocks.remove(block);
    }

    public void clearSearchBlocks() {
        searchBlocks.clear();
    }

    public Set<Block> getSearchBlocks() {
        return searchBlocks;
    }


    public boolean needAdd(BlockPos pos) {
        return searchBlocks.contains(mc.world.getBlockState(pos).getBlock());
    }

    public ArrayList<BlockPos> getResults() {
        return new ArrayList<>(results);
    }

    public void removeResultIf(Predicate<? super BlockPos> predicate) {
        results.removeIf(predicate);
    }
}
