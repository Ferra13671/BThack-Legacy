package com.ferra13671.BThack.impl.modules.movement;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.api.imixin.ModifyKeyBinding;
import com.ferra13671.BThack.managers.impl.place.PlaceManager;
import com.ferra13671.BThack.managers.impl.setting.Settings.ModeSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.Arrays;

@ModuleInfo(name = "SafeWalk", description = "lang.module.SafeWalk", category = "MOVEMENT")
public class SafeWalk extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Normal", "Shift", "Legit Shift")));
    public final NumberSetting edgeDistance = new NumberSetting("Edge Distance", this, 0.05, 0.05, 0.25, false);

    private boolean sneaking;

    @Override
    public void onEnable() {
        super.onEnable();

        sneaking = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (sneaking)
            sneaking = false;
        mc.options.sneakKey.setPressed(KeyboardUtils.isKeyDown(((ModifyKeyBinding) mc.options.sneakKey)._getBoundKey().getCode()));
    }

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onTick(ClientTickEvent e) {
        if (nullCheck() || !mode.getValue().equals("Legit Shift") || !mc.player.verticalCollision) return;

        if (PlaceManager.ignoreBlocks.contains(mc.world.getBlockState(BlockPos.ofFloored(mc.player.getX(), mc.player.getY() - 0.2, mc.player.getZ())).getBlock())) {
            mc.options.sneakKey.setPressed(true);
        } else {
            mc.options.sneakKey.setPressed(KeyboardUtils.isKeyDown(((ModifyKeyBinding) mc.options.sneakKey)._getBoundKey().getCode()));
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public void onClipAtLedge(boolean clipping) {
        if (mode.getValue().equals("Legit Shift")) {
            if (sneaking)
                setSneaking(false);
            return;
        }

        if(!isEnabled() || mode.getValue().equals("Normal") || !mc.player.isOnGround()) {
            if(sneaking)
                setSneaking(false);

            return;
        }

        Box box = mc.player.getBoundingBox();
        Box adjustedBox = box.stretch(0, -mc.player.getStepHeight(), 0)
                .expand(-edgeDistance.getValue(), 0, -edgeDistance.getValue());

        if(mc.world.isSpaceEmpty(mc.player, adjustedBox))
            clipping = true;

        setSneaking(clipping);
    }

    private void setSneaking(boolean sneaking) {
        KeyBinding sneakKey = mc.options.sneakKey;

        if(sneaking)
            sneakKey.setPressed(true);
        else
            sneakKey.setPressed(KeyboardUtils.isKeyDown(((ModifyKeyBinding) sneakKey)._getBoundKey().getCode()));

        this.sneaking = sneaking;
    }
}
