package com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotTasks;

import com.ferra13671.BThack.core.client.systems.file.JsonUtils;
import com.ferra13671.BThack.managers.impl.Break.BreakManager;
import com.ferra13671.BThack.managers.impl.Break.SimpleBreakThread;
import com.ferra13671.BThack.managers.impl.thread.ThreadClosedException;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotTask;
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
    @SuppressWarnings("DataFlowIssue")
    public void play() throws ThreadClosedException {
        SimpleBreakThread simpleDestroyThread = new SimpleBreakThread(BlockPos.ofFloored(mc.player.getX() + x, mc.player.getY() + y, mc.player.getZ() + z));
        simpleDestroyThread.start();
        BreakManager.isDestroying = true;
        while (BreakManager.isDestroying) {
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
