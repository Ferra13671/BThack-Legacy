package com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotTasks;

import com.ferra13671.BThack.core.client.Client;
import com.ferra13671.BThack.core.client.systems.file.JsonUtils;
import com.ferra13671.BThack.api.category.Categories;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.utils.ChatUtils;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotTask;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.Formatting;

import java.util.Arrays;

public class DisableModuleTask extends ActionBotTask {
    private final String module;
    private final boolean quietly;

    public DisableModuleTask(String module, boolean quietly) {
        super("DisableModule");
        mode = "DisableModule";

        this.module = module;
        this.quietly = quietly;

        taskDescription = Arrays.asList(
                "Finds a module by its name and disables it.",
                "It can also disable modules from plugins."
        );
    }

    @Override
    public void play() {
        Module m = Client.getModuleByName(module);
        if (m.getCategory().equals(Categories.HUD)) m = null;

        if (m == null) {
            ChatUtils.sendMessage("[ActionBot: DisableModuleTask] " + Formatting.YELLOW + "Module was not found. Skipping a task.");
            return;
        }
        if (quietly) m.setEnabledQuietly(false);
        else m.setEnabled(false);
    }

    @Override
    public String getButtonName() {
        return super.getName() + ": " + module + "  Quietly: " + quietly;
    }

    @Override
    public void save(JsonObject jsonObject) {
        jsonObject.add("Module", new JsonPrimitive(module));
        jsonObject.add("Quietly", new JsonPrimitive(quietly));
    }

    @Override
    public void load(JsonObject jsonObject) {
        if (JsonUtils.equalsNull(jsonObject, "Module", "Quietly")) return;

        ActionBotConfig.tasks.add(new DisableModuleTask(jsonObject.get("Module").getAsString(), jsonObject.get("Quietly").getAsBoolean()));
    }
}
