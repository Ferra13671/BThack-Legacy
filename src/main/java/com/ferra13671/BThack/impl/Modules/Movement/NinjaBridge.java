package com.ferra13671.BThack.impl.Modules.Movement;


import com.ferra13671.BThack.managers.managers.Place.PlaceManager;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.managers.managers.Thread.BThackThread;
import com.ferra13671.BThack.managers.managers.Thread.IThread;
import com.ferra13671.BThack.managers.managers.Thread.ThreadClosedException;
import com.ferra13671.BThack.managers.managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.*;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

//TODO: rewrite it, maybe.
@ModuleInfo(name = "NinjaBridge", description = "lang.module.NinjaBridge", category = "MOVEMENT")
public class NinjaBridge extends Module {

    public final NumberSetting actionDelay = new NumberSetting("Action delay", this, 50, 0, 200, true);
    public final NumberSetting placeTime = new NumberSetting("Place block time", this, 175, 50, 300, true);
    public final NumberSetting placeFactor = new NumberSetting("Place block factor", this, 25, 1, 100, true);
    public final NumberSetting airCheck = new NumberSetting("Extra air check",this, 0.03, 0.0, 0.2, false);


    private boolean close = false;

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }
        close = false;
        ThreadManager.startNewThread(iThread);
    }

    private final IThread iThread = new IThread() {

        @Override
        public void start(BThackThread thread) throws ThreadClosedException {
            //float x = (float)(mc.player.posX - ((int) mc.player.posX));
            //float z = (float)(mc.player.posZ - ((int) mc.player.posZ));
            //if ((x < 0.1f || x > 0.3f) && (z < 0.1f || z > 0.3f)) {
            //    ChatUtils.sendMessage(ChatFormatting.YELLOW + "Stand on the edge of the block where you want to start building.");
            //    Client.getModuleByName(moduleName).setToggled(false);
            //    stopMotion();
            //    stop();
            //}

            int yaw = RotateUtils.getRoundedToCornersEntityRotation(mc.player);
            float pitch = 77.7f;

            while (isEnabled()) {

                //Retrieving settings from the module
                int _actionDelay = actionDelay.getValue().intValue();
                int _placeTime = placeTime.getValue().intValue();
                int _placeFactor = placeFactor.getValue().intValue();
                float extraAirDistance = airCheck.getValue().floatValue();
                //

                BlockPos block = getBlockPos(yaw, extraAirDistance);

                BlockState blockState = mc.world.getBlockState(block);

                if (mc.player.horizontalCollision) {
                    close("");
                }

                checkBlocks();

                mc.player.yaw = yaw;
                mc.player.pitch = pitch;

                while (!PlaceManager.ignoreBlocks.contains(blockState.getBlock()) && isEnabled()) {
                    mc.options.backKey.setPressed(true);
                    mc.options.rightKey.setPressed(true);

                    //Delay between actions
                    if (_actionDelay != 0) {
                        thread.sleepThread(_actionDelay);
                    }
                    //

                    checkBlocks();

                    block = getBlockPos(yaw, extraAirDistance);
                    blockState = mc.world.getBlockState(block);
                }
                while (PlaceManager.ignoreBlocks.contains(blockState.getBlock()) && isEnabled()) {
                    mc.options.sneakKey.setPressed(true);
                    placeBlock(_placeTime, _placeFactor, thread);

                    //Delay between actions
                    if (_actionDelay != 0) {
                        thread.sleepThread(_actionDelay);
                    }
                    //

                    checkBlocks();

                    block = getBlockPos(yaw, extraAirDistance);
                    blockState = mc.world.getBlockState(block);
                }
                mc.options.sneakKey.setPressed(false);

                //Checks if the thread needs to be closed, and if it does, closes it after executing the necessary logic.
                if (close) {
                    stopMotion();
                    thread.stopOnException();
                }
            }
            stopMotion();
        }

        //Spams a keybind to use an item to put a block
        private void placeBlock(long time, long factor, BThackThread thread) {
            long a = time;
            while (a > 0) {
                mc.options.useKey.setPressed(true);
                thread.sleepThread(factor);
                mc.options.useKey.setPressed(false);
                a -= factor;
            }
        }

        //Stops the player's movement (needed when the module is turned off)
        private void stopMotion() {
            mc.options.backKey.setPressed(false);
            mc.options.rightKey.setPressed(false);
            mc.options.useKey.setPressed(false);
        }

        //Gets BlockPos under the player + extra distance
        private BlockPos getBlockPos(int yaw, float extraRange) {
            BlockPos block = null;
            switch (yaw) {
                case -135 -> block = BlockPos.ofFloored(mc.player.getX(), mc.player.getY() - 0.5F, mc.player.getZ() + extraRange);
                case -45 -> block = BlockPos.ofFloored(mc.player.getX() - extraRange, mc.player.getY() - extraRange, mc.player.getZ());
                case 45 -> block = BlockPos.ofFloored(mc.player.getX(), mc.player.getY() - 0.5F, mc.player.getZ() - extraRange);
                case 135 -> block = BlockPos.ofFloored(mc.player.getX() + extraRange, mc.player.getY() - 0.5F, mc.player.getZ());
            }

            return block;
        }

        private void close(String message) {
            if (!message.isEmpty())
                ChatUtils.sendMessage(Formatting.YELLOW + message);
            setEnabled(false);
            close = true;
        }

        /*
        Checks if there are blocks in the hand, if not, it searches for them in the hotbar.
        If no blocks are found, it disables the module and displays a message in the chat.
         */
        private void checkBlocks() {
            int itemBlock;
            if (!(mc.player.getInventory().getStack(mc.player.getInventory().selectedSlot).getItem() instanceof BlockItem)) {
                itemBlock = ItemUtils.getBlock();

                if (itemBlock == -1) {
                    close(LanguageSystem.translate("lang.module.Scaffold.noBlocks"));
                } else {
                    if (itemBlock < 9)
                        InventoryUtils.swapItem(itemBlock);
                    else
                        InventoryUtils.swapItemOnInventory(mc.player.getInventory().selectedSlot, itemBlock);
                }
            }
        }
    };
}
