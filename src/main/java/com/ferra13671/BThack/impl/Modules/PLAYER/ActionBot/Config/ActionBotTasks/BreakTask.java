package com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTasks;

import com.ferra13671.BThack.Core.FileSystem.JsonUtils;
import com.ferra13671.BThack.api.Managers.Destroy.DestroyManager;
import com.ferra13671.BThack.api.Managers.Destroy.SimpleDestroyThread;
import com.ferra13671.BThack.api.Managers.Thread.ThreadClosedException;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;

public class BreakTask extends ActionBotTask {

    private final int x;
    private final int y;
    private final int z;

    public BreakTask(int x, int y, int z) {
        super("Break");
        mode = "Break";

        this.x = x;
        this.y = y;
        this.z = z;

        this.taskDescription = Arrays.asList(
                "When enabled, it breaks the block at the selected coordinates.",
                "Coordinates should be entered by the ratio of the player's coordinates."
        );
    }

    @Override
    public void play() throws ThreadClosedException {
        SimpleDestroyThread simpleDestroyThread = new SimpleDestroyThread(BlockPos.ofFloored(mc.player.getX() + x, mc.player.getY() + y, mc.player.getZ() + z));
        simpleDestroyThread.start();
        DestroyManager.isDestroying = true;
        while (DestroyManager.isDestroying) {
            if (thread.isThreadClosed()) {
                simpleDestroyThread.closeThread();
                thread.stopOnException();
                return;
            }
            sleepThread(50);
        }
    }

    @Override
    public String getButtonName() {
        return getName() + ":  X: " + x + "  Y: " + y + "  Z: " + z;
    }

    @Override
    public void save(JsonObject jsonObject) {
        jsonObject.add("X", new JsonPrimitive(x));
        jsonObject.add("Y", new JsonPrimitive(y));
        jsonObject.add("Z", new JsonPrimitive(z));
    }

    @Override
    public void load(JsonObject jsonObject) {
        if (JsonUtils.equalsNull(jsonObject, "X", "Y", "Z")) return;

        ActionBotConfig.tasks.add(new BreakTask(jsonObject.get("X").getAsInt(), jsonObject.get("Y").getAsInt(), jsonObject.get("Z").getAsInt()));
    }
}
