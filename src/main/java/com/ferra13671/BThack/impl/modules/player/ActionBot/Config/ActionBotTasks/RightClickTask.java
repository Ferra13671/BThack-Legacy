package com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotTasks;

import com.ferra13671.BThack.core.client.systems.file.JsonUtils;
import com.ferra13671.BThack.managers.impl.thread.ThreadClosedException;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotTask;
import com.ferra13671.BThack.mixins.accessor.IMinecraftClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.hit.HitResult;

import java.util.Arrays;

public class RightClickTask extends ActionBotTask {

    private final float yaw;
    private final float pitch;

    public RightClickTask(float yaw, float pitch) {
        super("RightClick");
        mode = "RightClick";

        this.yaw = yaw;
        this.pitch = pitch;

        this.taskDescription = Arrays.asList(
                "When enabled, it simulates a right click at the selected yaw and pitch.",
                "Coordinates should be entered by the ratio of the player's coordinates."
        );
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void play() throws ThreadClosedException {
        thread.sleepThread(300);
        mc.player.setYaw(yaw);
        mc.player.setPitch(pitch);
        mc.gameRenderer.updateCrosshairTarget(mc.getRenderTickCounter().getTickDelta(false));
        HitResult hitResult = mc.crosshairTarget;
        thread.sleepThread(100);
        mc.crosshairTarget = hitResult;
        ((IMinecraftClient) mc).useItem();
    }

    @Override
    public String getButtonName() {
        return getName() + ":  Yaw: " + yaw + "  Pitch: " + pitch;
    }

    @Override
    public void save(JsonObject jsonObject) {
        jsonObject.add("Yaw", new JsonPrimitive(yaw));
        jsonObject.add("Pitch", new JsonPrimitive(pitch));
    }

    @Override
    public void load(JsonObject jsonObject) {
        if (JsonUtils.equalsNull(jsonObject, "Yaw", "Pitch")) return;

        ActionBotConfig.tasks.add(new RightClickTask(jsonObject.get("Yaw").getAsFloat(), jsonObject.get("Pitch").getAsFloat()));
    }
}
