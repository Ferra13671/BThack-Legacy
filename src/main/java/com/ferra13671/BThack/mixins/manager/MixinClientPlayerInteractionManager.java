package com.ferra13671.BThack.mixins.manager;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.events.Block.AttackBlockEvent;
import com.ferra13671.BThack.events.Block.UseBlockEvent;
import com.ferra13671.BThack.events.Entity.AttackEntityEvent;
import com.ferra13671.BThack.api.IMixin.ModifyClientPlayerInteractionManager;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ClientPlayerInteractionManager.class, priority = Integer.MAX_VALUE)
public abstract class MixinClientPlayerInteractionManager implements ModifyClientPlayerInteractionManager {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow public abstract void syncSelectedSlot();

    @Shadow private int blockBreakingCooldown;

    @Shadow private GameMode gameMode;

    @Shadow protected abstract void sendSequencedPacket(ClientWorld world, SequencedPacketCreator packetCreator);

    @Shadow public abstract boolean breakBlock(BlockPos pos);

    @Shadow protected abstract boolean isCurrentlyBreaking(BlockPos pos);

    @Shadow private boolean breakingBlock;

    @Shadow private float currentBreakingProgress;

    @Shadow private float blockBreakingSoundCooldown;

    @Shadow private BlockPos currentBreakingPos;

    @Shadow public abstract int getBlockBreakingProgress();

    @Shadow public abstract boolean attackBlock(BlockPos pos, Direction direction);

    @Shadow private ItemStack selectedStack;

    @Inject(method = "attackEntity", at = @At("HEAD"), cancellable = true)
    public void modifyAttackEntity(PlayerEntity player, Entity entity, CallbackInfo ci) {
        Event event = new AttackEntityEvent(player, entity);
        BThack.EVENT_BUS.activate(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    public void modifyInteractBlock(ClientPlayerEntity player, Hand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<ActionResult> ci) {

        UseBlockEvent e = new UseBlockEvent(player, player.getWorld(), hand, blockHitResult);
        BThack.EVENT_BUS.activate(e);


        if (e.isCancelled()) {
            ci.setReturnValue(ActionResult.FAIL);
            ci.cancel();
        }
    }

    @Inject(method = "attackBlock", at = @At("HEAD"), cancellable = true)
    public void modifyAttackBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> ci) {
        Event event = new AttackBlockEvent(client.player, pos, direction);
        BThack.EVENT_BUS.activate(event);

        ci.cancel();
        if (event.isCancelled()) {
            ci.setReturnValue(false);
            return;
        }

        ci.setReturnValue(internalAttackBlock(pos, direction));
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public void attackBlockNoEvent(BlockPos pos, Direction direction) {
        internalAttackBlock(pos, direction);
    }

    @SuppressWarnings("DataFlowIssue")
    @Unique
    public boolean internalAttackBlock(BlockPos pos, Direction direction) {
        if (client.player.isBlockBreakingRestricted(client.world, pos, gameMode)) {
            return false;
        } else if (!client.world.getWorldBorder().contains(pos)) {
            return false;
        } else {
            BlockState blockState;
            if (gameMode.isCreative()) {
                blockState = client.world.getBlockState(pos);
                client.getTutorialManager().onBlockBreaking(client.world, pos, blockState, 1.0F);
                sendSequencedPacket(client.world, (sequence) -> {
                    breakBlock(pos);
                    return new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, direction, sequence);
                });
                getBreakDelay(5);
            } else if (!breakingBlock || !isCurrentlyBreaking(pos)) {
                if (breakingBlock)
                    Managers.NETWORK_MANAGER.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, currentBreakingPos, direction));

                blockState = client.world.getBlockState(pos);
                client.getTutorialManager().onBlockBreaking(client.world, pos, blockState, 0.0F);
                sendSequencedPacket(client.world, (sequence) -> {
                    boolean bl = !blockState.isAir();
                    if (bl && currentBreakingProgress == 0.0F)
                        blockState.onBlockBreakStart(client.world, pos, client.player);

                    if (bl && checkDelta(blockState.calcBlockBreakingDelta(client.player, client.player.getWorld(), pos)) >= 1.0F) {
                        breakBlock(pos);
                    } else {
                        breakingBlock = true;
                        currentBreakingPos = pos;
                        selectedStack = client.player.getMainHandStack();
                        currentBreakingProgress = 0.0F;
                        blockBreakingSoundCooldown = 0.0F;
                        client.world.setBlockBreakingInfo(client.player.getId(), currentBreakingPos, getBlockBreakingProgress());
                    }

                    return new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, direction, sequence);
                });
            }

            return true;
        }
    }

    @Inject(method = "breakBlock", at = @At("HEAD"), cancellable = true)
    public void modifyBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (ModuleList.noGlitchBlocks.isEnabled())
            ModuleList.noGlitchBlocks.onBreakBlock(pos, cir);
    }

    @SuppressWarnings("DataFlowIssue")
    @Inject(method = "updateBlockBreakingProgress", at = @At("HEAD"), cancellable = true)
    public void modifyUpdateBlockBreakingProgress(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        cir.cancel();
        syncSelectedSlot();
        if (blockBreakingCooldown > 0) {
            --blockBreakingCooldown;
            cir.setReturnValue(true);
        } else {
            BlockState blockState;
            if (gameMode.isCreative() && client.world.getWorldBorder().contains(pos)) {
                blockBreakingCooldown = getBreakDelay(5);
                blockState = client.world.getBlockState(pos);
                client.getTutorialManager().onBlockBreaking(client.world, pos, blockState, 1.0F);
                sendSequencedPacket(client.world, (sequence) -> {
                    breakBlock(pos);
                    return new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, direction, sequence);
                });
                cir.setReturnValue(true);
            } else if (isCurrentlyBreaking(pos)) {
                blockState = client.world.getBlockState(pos);
                if (blockState.isAir()) {
                    breakingBlock = false;
                    cir.setReturnValue(false);
                } else {
                    currentBreakingProgress += blockState.calcBlockBreakingDelta(client.player, client.player.getWorld(), pos);
                    if (blockBreakingSoundCooldown % 4.0F == 0.0F) {
                        BlockSoundGroup blockSoundGroup = blockState.getSoundGroup();
                        client.getSoundManager().play(new PositionedSoundInstance(blockSoundGroup.getHitSound(), SoundCategory.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 8.0F, blockSoundGroup.getPitch() * 0.5F, SoundInstance.createRandom(), pos));
                    }

                    ++blockBreakingSoundCooldown;
                    client.getTutorialManager().onBlockBreaking(client.world, pos, blockState, MathHelper.clamp(currentBreakingProgress, 0.0F, 1.0F));
                    if (currentBreakingProgress >= 1.0F) {
                        breakingBlock = false;
                        sendSequencedPacket(client.world, (sequence) -> {
                            breakBlock(pos);
                            return new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, pos, direction, sequence);
                        });
                        currentBreakingProgress = 0.0F;
                        blockBreakingSoundCooldown = 0.0F;
                        blockBreakingCooldown = getBreakDelay(5);
                    }

                    if (currentBreakingPos != null)
                        client.world.setBlockBreakingInfo(client.player.getId(), currentBreakingPos, getBlockBreakingProgress());
                    cir.setReturnValue(true);
                }
            } else {
                cir.setReturnValue(attackBlock(pos, direction));
            }
        }
    }

    @Unique
    private boolean breakBlockCooldown = false;

    @Unique
    public int getBreakDelay(int value) {
        if (ModuleList.noBreakDelay.isEnabled()) {
            if (breakBlockCooldown) {
                breakBlockCooldown = false;
                return value;
            } else
                return 0;
        } else
            return value;
    }

    @Unique
    public float checkDelta(float value) {
        if (value >= 1) {
            if (ModuleList.noBreakDelay.isEnabled() && ModuleList.noBreakDelay.noInstant.getValue()) {
                blockBreakingCooldown = getBreakDelay(blockBreakingCooldown);
                return 0;
            }
        }
        return value;
    }
}
