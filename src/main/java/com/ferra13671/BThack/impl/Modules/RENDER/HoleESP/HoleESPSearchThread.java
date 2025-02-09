package com.ferra13671.BThack.impl.Modules.RENDER.HoleESP;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.managers.Thread.BThackThread;
import com.ferra13671.BThack.api.Utils.HoleUtils;
import com.ferra13671.BThack.api.Utils.MathUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HoleESPSearchThread extends BThackThread implements Mc {

    @Override
    public void threadAction() {
        while (ModuleList.holeESP.isEnabled() && ModuleList.holeESP.updateMode.getValue().equals("Thread")) {
            List<BlockPos> obsHoles;
            List<BlockPos> bedHoles;
            if (ModuleList.holeESP.rangeMode.getValue().equals("Normal")) {
                obsHoles = bedHoles = getNearbyBlocks(mc.player, ModuleList.holeESP.range.getValue());
            } else {
                obsHoles = bedHoles = getSphere(new BlockPos(mc.player.getBlockPos()), (float) ModuleList.holeESP.rangeH.getValue(), (float) ModuleList.holeESP.rangeV.getValue(), ModuleList.holeESP.sphere.getValue());
            }
            ModuleList.holeESP.obsidianHoleList = obsHoles.stream().filter(blockPos -> HoleUtils.isMutableHole(blockPos, true))
                    .collect(Collectors.toList());
            ModuleList.holeESP.bedrockHoleList = bedHoles.stream().filter(HoleUtils::isBedrockHole)
                    .collect(Collectors.toList());

            sleepThread((int) ModuleList.holeESP.updateDelay.getValue());
        }
    }

    private List<BlockPos> getSphere(BlockPos loc, float radius, float height, boolean sphere) {
        List<BlockPos> circleBlocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int) radius; x <= cx + radius; x++) {
            for (int z = cz - (int) radius; z <= cz + radius; z++) {
                for (int y = (sphere ? cy - (int) height : cy); y < (cy + height); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < radius * radius) {
                        circleBlocks.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
        return circleBlocks;
    }

    private List<BlockPos> getNearbyBlocks(PlayerEntity entityPlayer, double blockRange) {
        List<BlockPos> nearbyBlocks = new ArrayList<>();

        int range = (int) MathUtils.roundNumber(blockRange, 0);

        if (entityPlayer != null) {
            for (int x = -range; x <= range; x++) {
                for (int y = -range; y <= range; y++) {
                    for (int z = -range; z <= range; z++) {
                        nearbyBlocks.add(entityPlayer.getBlockPos().add(x, y, z));
                    }
                }
            }
        }

        return nearbyBlocks;
    }
}
