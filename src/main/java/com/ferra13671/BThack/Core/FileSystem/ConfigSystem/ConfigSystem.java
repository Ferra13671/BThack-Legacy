package com.ferra13671.BThack.Core.FileSystem.ConfigSystem;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ClientInfo;
import com.ferra13671.BThack.Core.FileSystem.FileSystem;
import com.ferra13671.BThack.api.Category.Categories;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.Frame;
import com.ferra13671.BThack.api.Managers.managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.HudComponent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Cape.Cape;
import com.ferra13671.BThack.api.Managers.managers.Macros.Macro;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.*;
import com.ferra13671.BThack.api.Managers.managers.Waypoint.Waypoint;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Plugin.Plugin;
import com.ferra13671.BThack.api.Plugin.PluginSystem;
import com.ferra13671.BThack.api.Managers.managers.Clans.Clan;
import com.ferra13671.BThack.api.Managers.managers.Clans.ClanSettingsBuilder;
import com.ferra13671.BThack.api.Utils.DataList.DataLists;
import com.ferra13671.BThack.impl.Modules.MISC.AutoAnvilEnchant;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.ferra13671.TextureUtils.PathMode;
import com.google.gson.*;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.ferra13671.BThack.Core.FileSystem.JsonUtils.*;

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
        try {
            saveModules();
            saveFrames();
            saveClans();
            DataLists.forEach(dataList -> {
                try {
                    dataList.saveInFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            saveActionBotTasks();
            Managers.AUTO_AUTH_MANAGER.save();
            Managers.ACCOUNT_MANAGER.save();
            AutoAnvilEnchant.save();
            saveClientInfo();
            saveWaypoints();
            saveMacros();
            save2FAKeys();
        } catch (IOException e) {
            e.printStackTrace();
        }
        saving = false;
    }

    public static void loadConfig() {
        try {
            loadModules();
            loadClans();
            DataLists.forEach(dataList -> {
                try {
                    dataList.loadFromFile();
                } catch (IOException e) {
                    BThack.error(e.getMessage());
                }
            });
            loadFrames();
            loadActionBotTasks();
            Managers.AUTO_AUTH_MANAGER.load();
            Managers.ACCOUNT_MANAGER.load();
            AutoAnvilEnchant.load();
            loadClientInfo();
            loadWaypoints();
            loadMacros();
            load2FAKeys();
        } catch (IOException e) {
            BThack.error(e.getMessage());
        }
        try {
            ConfigUtils.loadFromTxt("CurrentConfig", "Modules", Client.clientInfo::setCurrentConfigName);
        } catch (IOException ignored) {}
    }

    public static void saveModules() throws IOException {
        for (Module module : Client.getAllModules()) {
            ConfigUtils.saveInJson(module.getName(), "Modules", jsonObject -> {
                JsonObject settingObject = new JsonObject();


                add(jsonObject, "Name", module.getName());
                add(jsonObject, "Enabled", module.isEnabled());
                add(jsonObject, "Bind", module.getKey());
                add(jsonObject, "Visible", module.isVisible());

                if (Managers.SETTINGS_MANAGER.getSettingsByMod(module) != null) {
                    for (Setting<?> s : Managers.SETTINGS_MANAGER.getSettingsByMod(module)) {
                        s.save(settingObject);
                    }
                }
                add(jsonObject, "Settings", settingObject);
            });
        }
    }

    public static void loadModules() throws IOException {
        for (Module module : Client.getAllModules()) {
            ConfigUtils.loadFromJson(module.getName(), "Modules", jsonObject -> {
                if (equalsNull(jsonObject, "Name", "Enabled", "Bind", "Visible")) return;

                JsonObject settingObject = jsonObject.get("Settings").getAsJsonObject();

                if (Managers.SETTINGS_MANAGER.getSettingsByMod(module) != null) {
                    for (Setting<?> s : Managers.SETTINGS_MANAGER.getSettingsByMod(module)) {
                        JsonElement settingValueObject;

                        settingValueObject = settingObject.get(s.getName());

                        if (settingValueObject != null) {
                            s.load(settingObject, settingValueObject);
                        }
                    }
                }
                module.setToggled(jsonObject.get("Enabled").getAsBoolean());
                module.setKey(jsonObject.get("Bind").getAsInt());
                module.setVisible(jsonObject.get("Visible").getAsBoolean());
            }, () -> {
                if (module.isAutoEnabled()) {
                    module.setToggled(true);
                }
            });
        }
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

                if (Managers.SETTINGS_MANAGER.getSettingsByMod(module) != null) {
                    for (Setting<?> s : Managers.SETTINGS_MANAGER.getSettingsByMod(module)) {
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

                            if (Managers.SETTINGS_MANAGER.getSettingsByMod(module) != null) {
                                for (Setting<?> s : Managers.SETTINGS_MANAGER.getSettingsByMod(module)) {
                                    JsonElement settingValueObject;

                                    settingValueObject = settingObject.get(s.getName());

                                    if (settingValueObject != null) {
                                        s.load(settingObject, settingValueObject);
                                    }
                                }
                            }

                            module.setQuietlyToggled(moduleObject.get("Enabled").getAsBoolean());
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

    public static void saveFrames() throws IOException {
        ConfigUtils.saveInJson("Frames", "", jsonObject -> {
            for (Frame frame : Frame.getGlobalFrames()) {
                JsonObject frameObject = new JsonObject();

                add(frameObject, "x", frame.getX());
                add(frameObject, "y", frame.getY());
                add(frameObject, "opened", frame.isOpen());

                add(jsonObject, frame.getName().toLowerCase(), frameObject);
            }
        });
    }

    public static void loadFrames() throws IOException {
        ConfigUtils.loadFromJson("Frames", "", jsonObject -> {
            for (Frame frame : Frame.getGlobalFrames()) {
                JsonElement jsonElement = jsonObject.get(frame.getName().toLowerCase());
                if (jsonElement == null) return;
                JsonObject settingObject = jsonElement.getAsJsonObject();
                if (settingObject == null) return;

                if (equalsNull(settingObject, "x", "y", "opened")) return;

                frame.setPosition(settingObject.get("x").getAsInt(), settingObject.get("y").getAsInt());
                frame.setOpen(settingObject.get("opened").getAsBoolean());
            }
        }, () -> {});
    }

    public static void saveHudComponents() throws IOException {
        for (Module module : Client.getModulesInCategory(Categories.HUD)) {
            HudComponent hudComponent = (HudComponent) module;
            ConfigUtils.saveInJson(hudComponent.getName(), "HudComponents", jsonObject -> {
                JsonObject settingObject = new JsonObject();

                add(jsonObject, "Name", hudComponent.getName());
                add(jsonObject, "X", hudComponent.getNoScaledX());
                add(jsonObject, "Y", hudComponent.getNoScaledY());
                add(jsonObject, "ScaledWidth", hudComponent.getScaledWidth());
                add(jsonObject, "ScaledHeight", hudComponent.getScaledHeight());
                add(jsonObject, "Enabled", hudComponent.isEnabled());

                if (Managers.SETTINGS_MANAGER.getSettingsByMod(module) != null) {
                    for (Setting<?> s : Managers.SETTINGS_MANAGER.getSettingsByMod(module)) {
                        s.save(settingObject);
                    }
                }
                add(jsonObject, "Settings", settingObject);
            });
        }
    }

    public static void loadHudComponents() throws IOException {
        File folder = Paths.get("BThack/HudComponents").toFile();
        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (Objects.equals(FilenameUtils.getExtension(file.getName()), "json")) {
                ConfigUtils.loadFromJson(file.getName().replace(".json", ""), "HudComponents", jsonObject -> {
                    if (equalsNull(jsonObject, "Name", "X", "Y", "ScaledWidth", "ScaledHeight")) return;

                    String name = jsonObject.get("Name").getAsString();
                    for (Module module : Client.getModulesInCategory(Categories.HUD)) {
                        HudComponent hudComponent = (HudComponent) module;
                        if (hudComponent.getName().equals(name)) {
                            hudComponent.setX(jsonObject.get("X").getAsFloat(), jsonObject.get("ScaledWidth").getAsInt());
                            hudComponent.setY(jsonObject.get("Y").getAsFloat(), jsonObject.get("ScaledHeight").getAsInt());
                            if (jsonObject.get("Enabled") != null) {
                                hudComponent.setToggled(jsonObject.get("Enabled").getAsBoolean());
                            }

                            if (!_null(jsonObject, "Settings")) {
                                JsonObject settingObject = jsonObject.get("Settings").getAsJsonObject();

                                if (Managers.SETTINGS_MANAGER.getSettingsByMod(module) != null) {
                                    for (Setting<?> s : Managers.SETTINGS_MANAGER.getSettingsByMod(module)) {
                                        JsonElement settingValueObject;

                                        settingValueObject = settingObject.get(s.getName());

                                        if (settingValueObject != null) {
                                            s.load(settingObject, settingValueObject);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }, () -> {});
            }
        }
    }

    public static void saveClans() throws IOException {
        FileSystem.deleteDirectory(new File("BThack/Social/Clans"));
        FileSystem.registerFolder("Clans", "/Social");
        for (Clan clan : Managers.CLAN_MANAGER.getClans()) {
            JsonArray list = new JsonArray();
            for (String ally : clan.getMembers()) {
                list.add(new JsonPrimitive(ally));
            }
            ConfigUtils.saveInJson(clan.getName(), "Social/Clans", clansObject -> {
                add(clansObject, "ClanName", clan.getName());
                add(clansObject, "R", clan.getR());
                add(clansObject, "G", clan.getG());
                add(clansObject, "B", clan.getB());
                add(clansObject, "Members", list);
            });
        }

        ClanSettingsBuilder.reloadSettings();
    }

    public static void loadClans() throws IOException {
        Managers.CLAN_MANAGER.getClans().clear();
        File folder = new File(Paths.get("BThack/Social/Clans").toUri());
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (Objects.equals(FilenameUtils.getExtension(file.getName()), "json")) {
                        String name = file.getName();
                        InputStream inputStream = Files.newInputStream(Paths.get("BThack/Social/Clans/" + name));
                        JsonObject clanOject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();

                        if (!equalsNull(clanOject, "ClanName", "R", "G", "B")) {
                            String clanName = clanOject.get("ClanName").getAsString();
                            float r = clanOject.get("R").getAsFloat();
                            float g = clanOject.get("G").getAsFloat();
                            float b = clanOject.get("B").getAsFloat();

                            Clan clan = Clan.of(clanName, r, g, b);

                            if (!_null(clanOject, "Members")) {
                                JsonArray list = clanOject.get("Members").getAsJsonArray();
                                clan.getMembers().addAll(list.asList().stream().map(JsonElement::getAsString).toList());
                            }

                            Managers.CLAN_MANAGER.getClans().add(clan);
                        }
                        inputStream.close();
                    }
                }
            }
        }

        ClanSettingsBuilder.reloadSettings();
    }


    public static void saveActionBotTasks() throws IOException {
        ConfigUtils.saveInJson("Default", "ActionBotConfigs", jsonObject -> {
            ArrayList<ActionBotTask> tasks = new ArrayList<>(ActionBotConfig.tasks);
            tasks.remove(ActionBotConfig.startTask);
            tasks.remove(ActionBotConfig.endTask);

            JsonArray jsonList = new JsonArray();
            for (ActionBotTask task : tasks) {
                JsonObject taskObject = new JsonObject();
                add(taskObject, "Mode", task.mode);
                task.save(taskObject);

                jsonList.add(taskObject);
            }
            add(jsonObject, "Tasks", jsonList);
        });
    }

    public static void loadActionBotTasks() throws IOException {
        ActionBotConfig.tasks.add(ActionBotConfig.startTask);
        ConfigUtils.loadFromJson("Default", "ActionBotConfigs", jsonObject -> {
            if (!_null(jsonObject, "Tasks")) {
                JsonArray jsonList = jsonObject.get("Tasks").getAsJsonArray();
                jsonList.asList().forEach(jsonElement -> {
                    JsonObject taskObject = jsonElement.getAsJsonObject();
                    if (!_null(taskObject, "Mode")) {
                        String mode = taskObject.get("Mode").getAsString();
                        ActionBotConfig.getFullActionBotTasks().forEach(actionBotTaskData -> {
                            if (actionBotTaskData.getTask().mode.equals(mode))
                                actionBotTaskData.getTask().load(taskObject);
                        });
                    }
                });
            }
                }
        , () -> {});
        ActionBotConfig.tasks.add(ActionBotConfig.endTask);
    }

    public static void saveWaypoints() throws IOException {
        ConfigUtils.saveInJson("Waypoints", "", jsonObject -> {
            JsonArray jsonList = new JsonArray();
            for (Waypoint waypoint : Managers.WAYPOINT_MANAGER.getWaypoints()) {
                JsonObject wpObject = new JsonObject();
                JsonArray wpPosition = new JsonArray();
                wpPosition.add(waypoint.getPosition().getX());
                wpPosition.add(waypoint.getPosition().getY());
                wpPosition.add(waypoint.getPosition().getZ());

                add(wpObject, "name", waypoint.getName());
                add(wpObject, "position", wpPosition);
                add(wpObject, "visible", waypoint.isVisible());
                add(wpObject, "dimension", waypoint.getDimension().name());
                add(wpObject, "color", waypoint.getColor());
                add(wpObject, "server", waypoint.getServer());

                jsonList.add(wpObject);
            }
            add(jsonObject, "waypoints", jsonList);
        });
    }

    public static void loadWaypoints() throws IOException {
        ConfigUtils.loadFromJson("Waypoints", "", jsonObject -> {
            if (!_null(jsonObject, "waypoints")) {
                JsonArray waypoints = jsonObject.get("waypoints").getAsJsonArray();
                waypoints.asList().forEach(jsonElement -> {
                    JsonObject waypoint = jsonElement.getAsJsonObject();

                    String name = Waypoint.DEFAULT_NAME.get();
                    Vec3d position = new Vec3d(Waypoint.DEFAULT_POSITION[0], Waypoint.DEFAULT_POSITION[1], Waypoint.DEFAULT_POSITION[2]);
                    boolean visible = Waypoint.DEFAULT_VISIBLE;
                    Waypoint.WaypointDimension dimension = Waypoint.DEFAULT_DIMENSION;
                    int color = Waypoint.DEFAULT_COLOR;
                    String server = "";

                    if (!_null(waypoint, "name")) name = waypoint.get("name").getAsString();
                    if (!_null(waypoint, "position")) {
                        JsonArray jsonList = waypoint.get("position").getAsJsonArray();
                        position = new Vec3d(jsonList.get(0).getAsDouble(), jsonList.get(1).getAsDouble(), jsonList.get(2).getAsDouble());
                    }
                    if (!_null(waypoint, "visible")) visible = waypoint.get("visible").getAsBoolean();
                    if (!_null(waypoint, "dimension")) dimension = Waypoint.WaypointDimension.valueOf(waypoint.get("dimension").getAsString());
                    if (!_null(waypoint, "color")) color = waypoint.get("color").getAsInt();
                    if (!_null(waypoint, "server")) server = waypoint.get("server").getAsString();

                    Managers.WAYPOINT_MANAGER.addWaypoint(new Waypoint(name, position, visible, dimension, color, server));
                });
            }
        }
        , () -> {});
    }

    public static void saveMacros() throws IOException {
        ConfigUtils.saveInJson("Macros", "", jsonObject -> {
            JsonArray jsonList = new JsonArray();
            Managers.MACROS_MANAGER.forEach(macro -> {
                JsonObject macroObject = new JsonObject();
                add(macroObject, "Name", macro.getName());
                add(macroObject, "Key", macro.getKey());
                add(macroObject, "Action", macro.getAction());
                jsonList.add(macroObject);
            });
            add(jsonObject, "Macros", jsonList);
        });
    }

    public static void loadMacros() throws IOException {
        ConfigUtils.loadFromJson("Macros", "", jsonObject -> {
            if (!_null(jsonObject, "Macros")) {
                JsonArray jsonList = jsonObject.get("Macros").getAsJsonArray();
                jsonList.asList().forEach(element -> {
                    JsonObject macroObject = element.getAsJsonObject();
                    if (!equalsNull(macroObject, "Name", "Key", "Action")) {
                        Managers.MACROS_MANAGER.addMacro(new Macro(macroObject.get("Name").getAsString(), macroObject.get("Key").getAsInt(), macroObject.get("Action").getAsString()));
                    }
                });
            }
                }
        , () -> {});
    }

    public static void save2FAKeys() throws IOException {
        Managers.TWOFA_MANAGER.save();
    }

    public static void load2FAKeys() throws IOException {
        Managers.TWOFA_MANAGER.load();
    }

    public static void saveClientInfo() throws IOException {
        ConfigUtils.saveInJson("ClientInfo", "", jsonObject -> {
            add(jsonObject, "prefix", Client.clientInfo.getChatPrefix());
            add(jsonObject, "font", Client.clientInfo.getFont());
            JsonObject capeInfoObject = new JsonObject();
            add(capeInfoObject, "dataPath", Client.clientInfo.getCapeInfo().dataPath());
            add(capeInfoObject, "type", Client.clientInfo.getCapeInfo().dataType().name());
            add(jsonObject, "capeInfo", capeInfoObject);
        });
    }

    public static void loadClientInfo() throws IOException {
        ConfigUtils.loadFromJson("ClientInfo", "", jsonObject -> {
            if (!_null(jsonObject, "prefix")) Client.clientInfo.setChatPrefix(jsonObject.get("prefix").getAsString());
            if (!_null(jsonObject, "font")) Client.clientInfo.setFont(jsonObject.get("font").getAsString());
            if (!_null(jsonObject, "capeInfo")) {
                JsonObject capeInfoObject = jsonObject.get("capeInfo").getAsJsonObject();
                String dataPath = "";
                ClientInfo.CapeDataType dataType = ClientInfo.CapeDataType.NONE;
                if (!_null(capeInfoObject, "dataPath")) dataPath = capeInfoObject.get("dataPath").getAsString();
                if (!_null(capeInfoObject, "type")) dataType = ClientInfo.CapeDataType.valueOf(capeInfoObject.get("type").getAsString());
                Client.clientInfo.setCapeInfo(new ClientInfo.CapeInfo(dataPath, dataType));
            }
            }
        ,() -> {});
        loadCape();
    }

    public static void loadCape() {
        switch (Client.clientInfo.getCapeInfo().dataType()) {
            case FILE -> {
                try {
                    InputStream stream = Files.newInputStream(Paths.get(Client.clientInfo.getCapeInfo().dataPath()));
                    Managers.CAPE_MANAGER.setCape(Cape.fromInputStream(stream));
                } catch (IOException e) {
                    BThack.error(e.getMessage());
                }
            }
            case URL -> {
                try {
                    InputStream stream = new URI(Client.clientInfo.getCapeInfo().dataPath()).toURL().openStream();
                    Managers.CAPE_MANAGER.setCape(Cape.fromInputStream(stream));
                } catch (IOException | URISyntaxException e) {
                    BThack.error(e.getMessage());
                }
            }
        }
    }

    public static void loadLanguages() {
        LanguageSystem.loadTranslations(ConfigUtils.newInputStream("assets/bthack/langs/EN.lng", PathMode.INSIDEJAR), "EN");
        LanguageSystem.loadTranslations(ConfigUtils.newInputStream("assets/bthack/langs/RU.lng", PathMode.INSIDEJAR), "RU");
        LanguageSystem.loadTranslations(ConfigUtils.newInputStream("assets/bthack/langs/PL.lng", PathMode.INSIDEJAR), "PL");

        PluginSystem.getLoadedPlugins().forEach(Plugin::onLoadLanguages);
    }
}
