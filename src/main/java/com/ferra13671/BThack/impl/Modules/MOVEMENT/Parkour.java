package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.ModifyBlockPos;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.FluidBlock;
import net.minecraft.util.math.BlockPos;

public class Parkour extends Module {

    public Parkour() {
        super("Parkour",
                "lang,module.Parkour",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );
    }

    private boolean needReset = false;

    @Override
    public void onEnable() {
        super.onEnable();

        needReset = false;
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.options.jumpKey.isPressed() && needReset) {
            mc.options.jumpKey.setPressed(false);
            needReset = false;
        }

        if (mc.player.isSneaking() || !isMoving()) return;

        if (mc.player.verticalCollision) {
            BlockPos pos = new ModifyBlockPos(mc.player.getX(), mc.player.getY() - 0.5, mc.player.getZ());
            if (mc.world.isAir(pos) || mc.world.getBlockState(pos).getBlock() instanceof FluidBlock) {
                mc.options.jumpKey.setPressed(true);
                needReset = true;
            }
        }
    }

    public boolean isMoving() {
        return mc.options.forwardKey.isPressed() ||
                mc.options.backKey.isPressed() ||
                mc.options.leftKey.isPressed() ||
                mc.options.rightKey.isPressed();
    }
}
