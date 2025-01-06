package com.ferra13671.BThack.impl.Modules.RENDER.HoleESP;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.HoleUtils;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.BThack.api.Utils.ModifyBlockPos;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HoleESPSearchThread extends Thread implements Mc {

    @Override
    public void run() {
        while (ModuleList.holeESP.isEnabled() && HoleESP.updateMode.getValue().equals("Thread")) {
            List<BlockPos> obsHoles;
            List<BlockPos> bedHoles;
            if (HoleESP.rangeMode.getValue().equals("Normal")) {
                obsHoles = bedHoles = getNearbyBlocks(mc.player, HoleESP.range.getValue());
            } else {
                obsHoles = bedHoles = getSphere(new ModifyBlockPos(mc.player.getBlockPos()), (float) HoleESP.rangeH.getValue(), (float) HoleESP.rangeV.getValue(), HoleESP.sphere.getValue());
            }
            HoleESP.obsidianHoleList = obsHoles.stream().filter(blockPos -> HoleUtils.isMutableHole(blockPos, true))
                    .collect(Collectors.toList());
            HoleESP.bedrockHoleList = bedHoles.stream().filter(HoleUtils::isBedrockHole)
                    .collect(Collectors.toList());

            applySleep((int) HoleESP.updateDelay.getValue());
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

    private void applySleep(int millis) {
        try {
            sleep(millis);
        } catch (Exception ignored) {}
    }
}
