package com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTasks;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.FileSystem.JsonUtils;
import com.ferra13671.BThack.api.Category.Categories;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
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
        if (quietly) m.setQuietlyToggled(false);
        else m.setToggled(false);
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
