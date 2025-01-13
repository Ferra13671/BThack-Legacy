package com.ferra13671.BThack.impl.Modules.MOVEMENT;


import com.ferra13671.BThack.api.Managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.Thread.BThackThread;
import com.ferra13671.BThack.api.Managers.Thread.IThread;
import com.ferra13671.BThack.api.Managers.Thread.ThreadClosedException;
import com.ferra13671.BThack.api.Managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.ModifyBlockPos;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

//TODO: rewrite it, maybe.
public class NinjaBridge extends Module {

    public final NumberSetting actionDelay = new NumberSetting("Action delay", this, 50, 0, 200, true);
    public final NumberSetting placeTime = new NumberSetting("Place block time", this, 175, 50, 300, true);
    public final NumberSetting placeFactor = new NumberSetting("Place block factor", this, 25, 1, 100, true);
    public final NumberSetting airCheck = new NumberSetting("Extra air check",this, 0.03, 0.0, 0.2, false);

    public NinjaBridge() {
        super("NinjaBridge",
                "lang.module.NinjaBridge",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        initSettings(
                actionDelay,
                placeTime,
                placeFactor,
                airCheck
        );
    }

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

            int yaw = AimBotUtils.getRoundedToCornersEntityRotation(mc.player);
            float pitch = 77.7f;

            while (isEnabled()) {

                //Retrieving settings from the module
                int _actionDelay = (int) actionDelay.getValue();
                int _placeTime = (int) placeTime.getValue();
                int _placeFactor = (int) placeFactor.getValue();
                float extraAirDistance = (float) airCheck.getValue();
                //

                BlockPos block = getBlockPos(yaw, extraAirDistance);

                BlockState blockState = mc.world.getBlockState(block);

                if (mc.player.horizontalCollision) {
                    close("");
                }

                checkBlocks();

                mc.player.yaw = yaw;
                mc.player.pitch = pitch;

                while (!BuildManager.ignoreBlocks.contains(blockState.getBlock()) && isEnabled()) {
                    mc.options.backKey.setPressed(true);
                    mc.options.rightKey.setPressed(true);

                    //Delay between actions
                    if (_actionDelay != 0) {
                        try {
                            thread.sleep(_actionDelay);
                        } catch (InterruptedException ignored) {}
                    }
                    //

                    checkBlocks();

                    block = getBlockPos(yaw, extraAirDistance);
                    blockState = mc.world.getBlockState(block);
                }
                while (BuildManager.ignoreBlocks.contains(blockState.getBlock()) && isEnabled()) {
                    mc.options.sneakKey.setPressed(true);
                    placeBlock(_placeTime, _placeFactor, thread);

                    //Delay between actions
                    if (_actionDelay != 0) {
                        try {
                            thread.sleep(_actionDelay);
                        } catch (InterruptedException ignored) {}
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
        private void placeBlock(long time, long factor, Thread thread) {
            long a = time;
            while (a > 0) {
                mc.options.useKey.setPressed(true);
                try {
                    thread.sleep(factor);
                } catch (InterruptedException ignored) {}
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
                case -135:
                    block = new ModifyBlockPos(mc.player.getX(), mc.player.getY() - 0.5F, mc.player.getZ() + extraRange);
                    break;
                case -45:
                    block = new ModifyBlockPos(mc.player.getX() - extraRange, mc.player.getY() - extraRange, mc.player.getZ());
                    break;
                case 45:
                    block = new ModifyBlockPos(mc.player.getX(), mc.player.getY() - 0.5F, mc.player.getZ() - extraRange);
                    break;
                case 135:
                    block = new ModifyBlockPos(mc.player.getX() + extraRange, mc.player.getY() - 0.5F, mc.player.getZ());
            }

            return block;
        }

        private void close(String message) {
            if (!message.isEmpty())
                ChatUtils.sendMessage(Formatting.YELLOW + message);
            setToggled(false);
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
