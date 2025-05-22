package com.ferra13671.BThack.core.Client.Systems.ConfigSystem;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.core.Client.Client;
import com.ferra13671.BThack.managers.managers.Thread.ThreadManager;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Plugin.Plugin;
import com.ferra13671.BThack.api.Plugin.PluginSystem;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.ferra13671.TextureUtils.PathMode;
import com.google.gson.*;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

import static com.ferra13671.BThack.core.Client.Systems.FileSystem.JsonUtils.*;

public final class ConfigSystem {
    private static volatile boolean saving = false;

    public static void saveConfigThreaded() {
        if (saving) return;
        saving = true;

        ThreadManager.startNewThread((thread -> saveConfig()));

        saving = false;
    }

    public static void saveConfig() {
        saving = true;
        SubConfigs.getSubConfigs().forEach(SubConfig::save);
        saving = false;
    }

    public static void loadConfig() {
        SubConfigs.getSubConfigs().forEach(SubConfig::load);
        try {
            ConfigUtils.loadFromTxt("CurrentConfig", "Modules", Client.clientInfo::setCurrentConfigName);
        } catch (IOException ignored) {}
    }

    public static List<String> getAllConfigs() {
        List<String> results = new ArrayList<>();
        File folder = Paths.get("BThack/Configs").toFile();
        File[] files = folder.listFiles();
        if (files == null) return results;

        for (File file : files) {
            if (Objects.equals(FilenameUtils.getExtension(file.getName()), "json")) {
                results.add(file.getName().replace(".json", ""));
            }
        }

        return results;
    }

    public static void saveConfigFile(String fileName) throws IOException {
        ConfigUtils.saveInJson(fileName, "Configs", jsonObject -> {
            for (Module module : Client.getAllModules()) {
                JsonObject moduleObject = new JsonObject();
                JsonObject settingObject = new JsonObject();

                add(moduleObject, "Name", module.getName());
                add(moduleObject, "Enabled", module.isEnabled());
                add(moduleObject, "Bind", module.getKey());
                add(moduleObject, "Visible", module.isVisible());

                if (Managers.SETTINGS_MANAGER.getSettingsByModule(module) != null) {
                    for (Setting<?> s : Managers.SETTINGS_MANAGER.getSettingsByModule(module)) {
                        s.save(settingObject);
                    }
                }
                add(moduleObject, "Settings", settingObject);

                add(jsonObject, module.getName(), moduleObject);
            }
        });
    }

    public static void loadConfigFile(String fileName) throws IOException {
        ConfigUtils.loadFromJson(fileName, "Configs",
                jsonObject -> {

                    for (Module module : Client.getAllModules()) {
                        if (jsonObject.get(module.getName()) != null) {
                            JsonObject moduleObject = jsonObject.get(module.getName()).getAsJsonObject();
                            if (equalsNull(moduleObject, "Name", "Enabled", "Bind", "Visible")) continue;

                            JsonObject settingObject = moduleObject.get("Settings").getAsJsonObject();

                            if (Managers.SETTINGS_MANAGER.getSettingsByModule(module) != null) {
                                for (Setting<?> s : Managers.SETTINGS_MANAGER.getSettingsByModule(module)) {
                                    JsonElement settingValueObject;

                                    settingValueObject = settingObject.get(s.getName());

                                    if (settingValueObject != null) {
                                        try {
                                            s.load(settingObject, settingValueObject);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                            module.setEnabledQuietly(moduleObject.get("Enabled").getAsBoolean());
                            module.setKey(moduleObject.get("Bind").getAsInt());
                            module.setVisible(moduleObject.get("Visible").getAsBoolean());
                        }
                    }

                    try {
                        ConfigUtils.saveInTxt("CurrentConfig", "Modules", writer -> {
                            try {
                                writer.write(fileName);
                            } catch (IOException ignored) {}
                        });
                    } catch (IOException e) {
                        BThack.error(e.getMessage());
                    }
                },
                () -> {}
        );
    }

    public static void loadLanguages() {
        LanguageSystem.loadTranslations(ConfigUtils.newInputStream("assets/bthack/langs/EN.lng", PathMode.INSIDEJAR), "EN");
        LanguageSystem.loadTranslations(ConfigUtils.newInputStream("assets/bthack/langs/RU.lng", PathMode.INSIDEJAR), "RU");
        LanguageSystem.loadTranslations(ConfigUtils.newInputStream("assets/bthack/langs/PL.lng", PathMode.INSIDEJAR), "PL");

        PluginSystem.getLoadedPlugins().forEach(Plugin::onLoadLanguages);
    }
}
