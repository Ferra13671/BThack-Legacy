package com.ferra13671.BThack.api.Utils;

import com.ferra13671.BThack.api.Interfaces.Mc;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ChunkUtils implements Mc {

    public static Stream<WorldChunk> getLoadedChunks() {
        int radius = Math.max(2, mc.options.getClampedViewDistance()) + 3;
        int diameter = radius * 2 + 1;

        ChunkPos center = mc.player.getChunkPos();
        ChunkPos min = new ChunkPos(center.x - radius, center.z - radius);
        ChunkPos max = new ChunkPos(center.x + radius, center.z + radius);

        Stream<WorldChunk> stream = Stream.iterate(min, pos -> {

                    int x = pos.x;
                    int z = pos.z;

                    x++;

                    if(x > max.x)
                    {
                        x = min.x;
                        z++;
                    }

                    if(z > max.z)
                        throw new IllegalStateException("Stream limit didn't work.");

                    return new ChunkPos(x, z);

                }).limit(diameter * diameter)
                .filter(c -> mc.world.isChunkLoaded(c.x, c.z))
                .map(c -> mc.world.getChunk(c.x, c.z)).filter(Objects::nonNull);

        return stream;
    }

    public static Stream<BlockEntity> getLoadedBlockEntities() {
        return getLoadedChunks()
                .flatMap(chunk -> chunk.getBlockEntities().values().stream());
    }

    public static ArrayList<BlockEntity> getLoadedBlockEntitiesOnArrayList() {
        return getLoadedBlockEntities().collect(Collectors.toCollection(ArrayList::new));
    }
}
