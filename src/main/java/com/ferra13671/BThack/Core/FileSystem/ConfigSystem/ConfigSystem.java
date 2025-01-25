package com.ferra13671.BThack.Core.FileSystem.ConfigSystem;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.FileSystem.FileSystem;
import com.ferra13671.BThack.api.Category.Categories;
import com.ferra13671.BThack.api.Gui.ClickGui.component.Frame;
import com.ferra13671.BThack.api.Gui.MainMenu.BThackMainMenuScreen;
import com.ferra13671.BThack.api.Gui.MainMenu.SelectWallpaper.SelectWallpaperScreen;
import com.ferra13671.BThack.api.Gui.MainMenu.SelectWallpaper.Wallpaper;
import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.*;
import com.ferra13671.BThack.api.Managers.managers.Waypoint.Waypoint;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Plugin.Plugin;
import com.ferra13671.BThack.api.Plugin.PluginSystem;
import com.ferra13671.BThack.api.Social.Clans.Clan;
import com.ferra13671.BThack.api.Social.Clans.ClanManager;
import com.ferra13671.BThack.api.Social.Clans.ClanSettingsBuilder;
import com.ferra13671.BThack.api.Utils.DataList.DataLists;
import com.ferra13671.BThack.impl.Modules.MISC.AutoAuth;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.ActionBotTaskData;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.ferra13671.TextureUtils.GLTexture;
import com.ferra13671.TextureUtils.PathMode;
import com.google.gson.*;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigUtils.*;
import static com.ferra13671.BThack.Core.FileSystem.JsonUtils.*;

public final class ConfigSystem {
    static final Gson gson = (new GsonBuilder()).setPrettyPrinting().create();

    public static void saveConfig() {
        try {
            saveModules();
            saveFrames();
            saveClans();
            DataLists.forEach(dataList -> {
                try {
                    dataList.saveInFile();
                } catch (IOException e) {
                    BThack.error(e.getMessage());
                }
            });
            saveActionBotTasks();
            saveAutoAuthPasswords();
            saveClientInfo();
            saveWaypoints();
        } catch (IOException e) {
            BThack.error(e.getMessage());
        }
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
            loadAutoAuthPasswords();
            loadClientInfo();
            loadWaypoints();
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
                add(jsonObject, "Visible", module.visible);

                if (Managers.SETTINGS_MANAGER.getSettingsByMod(module) != null) {
                    for (Setting s : Managers.SETTINGS_MANAGER.getSettingsByMod(module)) {
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
                    for (Setting s : Managers.SETTINGS_MANAGER.getSettingsByMod(module)) {
                        JsonElement settingValueObject;

                        settingValueObject = settingObject.get(s.getName());

                        if (settingValueObject != null) {
                            s.load(settingObject, settingValueObject);
                        }
                    }
                }
                module.setToggled(jsonObject.get("Enabled").getAsBoolean());
                module.setKey(jsonObject.get("Bind").getAsInt());
                module.visible = jsonObject.get("Visible").getAsBoolean();
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
                add(moduleObject, "Visible", module.visible);

                if (Managers.SETTINGS_MANAGER.getSettingsByMod(module) != null) {
                    for (Setting s : Managers.SETTINGS_MANAGER.getSettingsByMod(module)) {
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
                    try {
                        saveConfigFile(Client.clientInfo.getCurrentConfigName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    for (Module module : Client.getAllModules()) {
                        if (jsonObject.get(module.getName()) != null) {
                            JsonObject moduleObject = jsonObject.get(module.getName()).getAsJsonObject();
                            if (equalsNull(moduleObject, "Name", "Enabled", "Bind", "Visible")) continue;

                            JsonObject settingObject = moduleObject.get("Settings").getAsJsonObject();

                            if (Managers.SETTINGS_MANAGER.getSettingsByMod(module) != null) {
                                for (Setting s : Managers.SETTINGS_MANAGER.getSettingsByMod(module)) {
                                    JsonElement settingValueObject;

                                    settingValueObject = settingObject.get(s.getName());

                                    if (settingValueObject != null) {
                                        s.load(settingObject, settingValueObject);
                                    }
                                }
                            }

                            module.setQuietlyToggled(moduleObject.get("Enabled").getAsBoolean());
                            module.setKey(moduleObject.get("Bind").getAsInt());
                            module.visible = moduleObject.get("Visible").getAsBoolean();
                        }
                    }

                    try {
                        ConfigUtils.saveInTxt("CurrentConfig", "Modules", writer -> {
                            try {
                                writer.write(fileName);
                            } catch (IOException ignored) {}
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
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

                add(jsonObject, frame.getFrameName(), frameObject);
            }
        });
    }

    public static void loadFrames() throws IOException {
        ConfigUtils.loadFromJson("Frames", "", jsonObject -> {
            for (Frame frame : Frame.getGlobalFrames()) {
                JsonElement jsonElement = jsonObject.get(frame.getFrameName());
                if (jsonElement == null) return;
                JsonObject settingObject = jsonElement.getAsJsonObject();
                if (settingObject == null) return;

                if (equalsNull(settingObject, "x", "y", "opened")) return;

                frame.setX(settingObject.get("x").getAsInt());
                frame.setY(settingObject.get("y").getAsInt());
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
                    for (Setting s : Managers.SETTINGS_MANAGER.getSettingsByMod(module)) {
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
                                    for (Setting s : Managers.SETTINGS_MANAGER.getSettingsByMod(module)) {
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

        loadOldHudInfo();
    }

    //TODO:
    /** Will need to remove this in the next versions */
    @Deprecated
    private static void loadOldHudInfo() throws IOException {
        ConfigUtils.loadFromJson("HUD", "Modules", jsonObject -> {
            for (Module module : Client.getModulesInCategory(Categories.HUD)) {
                HudComponent hudComponent = (HudComponent) module;
                JsonObject settingsObject = jsonObject.get("Settings").getAsJsonObject();
                if (!_null(settingsObject, hudComponent.getName()))
                    hudComponent.setToggled(settingsObject.get(hudComponent.getName()).getAsBoolean());
            }
        }, () -> {});
    }

    public static void saveClans() throws IOException {
        FileSystem.deleteDirectory(new File("BThack/Social/Clans"));
        FileSystem.registerFolder("Clans", "/Social");
        for (Clan clan : ClanManager.getClans()) {
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
        ClanManager.getClans().clear();
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

                            if (_null(clanOject, "Members")) loadClanMembersOld(clan);
                            else {
                                JsonArray list = clanOject.get("Members").getAsJsonArray();
                                clan.getMembers().addAll(list.asList().stream().map(JsonElement::getAsString).toList());
                            }

                            ClanManager.getClans().add(clan);
                        }
                        inputStream.close();
                    }
                }
            }
        }

        ClanSettingsBuilder.reloadSettings();
    }

    /** The old method of loading clan members */
    @Deprecated
    public static void loadClanMembersOld(Clan clan) throws IOException {
        File clanMembersFolder = new File(Paths.get("BThack/Social/Clans/" + clan.getName() + "_Members").toUri());
        File[] clanMembers = clanMembersFolder.listFiles();

        if (clanMembers != null) {
            for (File memberFile : clanMembers) {
                if (memberFile.isFile()) {
                    if (Objects.equals(FilenameUtils.getExtension(memberFile.getName()), "json")) {
                        String memberName = memberFile.getName();
                        InputStream memberInputStream = Files.newInputStream(Paths.get("BThack/Social/Clans/" + clan.getName() + "_Members/" + memberName));
                        JsonObject memberObject = JsonParser.parseReader(new InputStreamReader(memberInputStream)).getAsJsonObject();

                        if (memberObject.get("name") != null) {
                            String member = memberObject.get("name").getAsString();
                            clan.getMembers().add(member);
                        }

                        memberInputStream.close();
                    }
                }
            }
        }
    }


    public static void saveActionBotTasks() throws IOException {
        Path configInfoFile = Paths.get("BThack/ActionBot/ConfigInfo.txt");

        BufferedWriter writer = Files.newBufferedWriter(configInfoFile, StandardCharsets.UTF_8);
        ArrayList<ActionBotTask> tasks = new ArrayList<>(ActionBotConfig.tasks);
        tasks.remove(ActionBotConfig.startTask);
        tasks.remove(ActionBotConfig.endTask);

        int taskNumber = 1;

        boolean m = false;

        for (ActionBotTask task : tasks) {
            String fileName = taskNumber + ". " + task.getName();

            registerFiles(fileName, "ActionBot/DefaultConfig");

            OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(Files.newOutputStream(Paths.get("BThack/ActionBot/DefaultConfig/" + fileName + ".json")), StandardCharsets.UTF_8);

            JsonObject taskObject = new JsonObject();


            add(taskObject, "Mode", task.mode);
            task.save(taskObject);


            if (!m) {
                writer.write(fileName);
                m = true;
            } else {
                writer.write(System.lineSeparator() + fileName);
            }

            taskNumber++;

            String jsonString = gson.toJson(JsonParser.parseString(taskObject.toString()));
            fileOutputStreamWriter.write(jsonString);
            fileOutputStreamWriter.close();
        }

        writer.close();
    }

    public static void loadActionBotTasks() throws IOException {
        Path configInfoFile = Paths.get("BThack/ActionBot/ConfigInfo.txt");

        ActionBotConfig.tasks.add(ActionBotConfig.startTask);
        if (Files.exists(configInfoFile)) {

            BufferedReader readerConfigInfo = Files.newBufferedReader(configInfoFile, StandardCharsets.UTF_8);
            String line = readerConfigInfo.readLine();

            ArrayList<String> tasksNames = new ArrayList<>();

            while (line != null) {
                tasksNames.add(line);
                line = readerConfigInfo.readLine();
            }

            readerConfigInfo.close();

            for (String taskName : tasksNames) {
                Path taskPath = Paths.get("BThack/ActionBot/DefaultConfig/" + taskName + ".json");

                if (Files.exists(taskPath)) {
                    InputStream inputStream = Files.newInputStream(taskPath);

                    JsonObject taskObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();

                    if (!_null(taskObject, "Mode")) {
                        for (ActionBotTaskData data : ActionBotConfig.getFullActionBotTasks()) {
                            if (data.getTask().mode.equals(taskObject.get("Mode").getAsString())) {
                                data.getTask().load(taskObject);
                            }
                        }
                    }
                }
            }
        }
        ActionBotConfig.tasks.add(ActionBotConfig.endTask);
    }

    public static void saveAutoAuthPasswords() throws IOException {
        ConfigUtils.saveInJson("AutoAuthPasswords", "", jsonObject -> {
            JsonArray jsonElements = new JsonArray();
            AutoAuth.passwords.forEach((playerName, password) -> {
                JsonObject info = new JsonObject();
                add(info, playerName, password);
                jsonElements.add(info);
            });
            add(jsonObject, "info", jsonElements);
        });
    }

    public static void loadAutoAuthPasswords() throws IOException {
        ConfigUtils.loadFromJson("AutoAuthPasswords", "", jsonObject -> {
            if (_null(jsonObject, "info")) return;
            for (JsonElement jsonElement : jsonObject.get("info").getAsJsonArray().asList()) {
                jsonElement.getAsJsonObject().asMap().forEach((playerName, password) -> AutoAuth.passwords.put(playerName, password.getAsString()));
            }
        }, () -> {});
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

                    if (!_null(waypoint, "name")) name = waypoint.get("name").getAsString();
                    if (!_null(waypoint, "position")) {
                        JsonArray jsonList = waypoint.get("position").getAsJsonArray();
                        position = new Vec3d(jsonList.get(0).getAsDouble(), jsonList.get(1).getAsDouble(), jsonList.get(2).getAsDouble());
                    }
                    if (!_null(waypoint, "visible")) visible = waypoint.get("visible").getAsBoolean();
                    if (!_null(waypoint, "dimension")) dimension = Waypoint.WaypointDimension.valueOf(waypoint.get("dimension").getAsString());
                    if (!_null(waypoint, "color")) color = waypoint.get("color").getAsInt();

                    Managers.WAYPOINT_MANAGER.addWaypoint(new Waypoint(name, position, visible, dimension, color));
                });
            }
        }
        , () -> {});
    }


    public static void loadColourThemes() throws IOException {
        //Default themes
        Client.addCTheme("Default", 0x191CFF, 0xFF111111, -14540254, 0x191CFF, 0xFFFFFF, 0x191CFF);
        Client.addCTheme("PurpleNight", 0xD902EE, 0x320D3E, 0x360E42, 0xF162FF, 0xFFD79D, 0xD902EE);
        Client.addCTheme("PinkAndBrown", 0xBE1558, 0x322514, 0x3A2B17, 0xE75874, 0xFBCBC9, 0xBE1558);
        Client.addCTheme("Chestnut", 0xDD8F2E, 0x2E1104, 0x341406, 0xDDB869, 0x956429, 0xDD8F2E);
        Client.addCTheme("ChocolateBrownie", 0xB6452C, 0x301B28, 0x381F2E, 0xDDC5A2, 0x523634, 0xB6452C);
        Client.addCTheme("CitrusMix", 0x902B04, 0x2B2E0E, 0x333611, 0xD9901C, 0xF7E9B3, 0xD9901C);
        Client.addCTheme("CopperMountain", 0x206F7C, 0x211B1A, 0x251F1D, 0x77341D, 0xB17365, 0x206F7C);
        Client.addCTheme("Crocuses", 0xDFBD99, 0x4C1905, 0x561D07, 0xBA4A1B, 0xC4A189, 0xDFBD99);
        Client.addCTheme("Freshness", 0x62A77C, 0x153626, 0x183D2B, 0x7CC398, 0xADCBB5, 0x62A77C);
        Client.addCTheme("Ocean", 0x5A89B9, 0x0A1C34, 0x0B1F3A, 0xC1D9F9, 0x2E4553, 0x5A89B9);
        Client.addCTheme("Spice", 0xA96946, 0x271007, 0x2F1308, 0xA96946, 0xECCDB1, 0xA96946);

        //Custom themes
        PluginSystem.getLoadedPlugins().forEach(Plugin::onLoadColourThemes);

        File folder = Paths.get("BThack/Themes/ColourThemes").toFile();
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (Objects.equals(FilenameUtils.getExtension(file.getName()), "json")) {
                        String name = file.getName().replace(".json", "");
                        ConfigUtils.loadFromJson(name, "Themes/ColourThemes", colourThemeObject -> {
                            if (_null(colourThemeObject, "Name")) return;

                            JsonObject colourObject = colourThemeObject.get("Colours").getAsJsonObject();

                            Client.addCTheme(
                                    colourThemeObject.get("Name").getAsString(),
                                    colourObject.get("color").getAsInt(),
                                    colourObject.get("backgroundColor").getAsInt(),
                                    colourObject.get("backgroundHoveredColor").getAsInt(),
                                    colourObject.get("moduleEnabledColor").getAsInt(),
                                    colourObject.get("moduleDisabledColor").getAsInt(),
                                    colourObject.get("arrayListColor").getAsInt()
                            );
                        }, () -> {});
                    }
                }
            }
        }
    }

    public static void refreshWallpapers() {
        for (Wallpaper wallpaper : SelectWallpaperScreen.wallpapers) {
            if (wallpaper.texture().getTexId() != Client.clientInfo.getDefaultMainMenuImage().getTexId())
                wallpaper.texture().deleteTexture();
        }
        SelectWallpaperScreen.wallpapers.clear();

        File imagesFolder = Paths.get("BThack/Wallpapers").toFile();
        File[] files = imagesFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (imageFormats.contains(FilenameUtils.getExtension(file.getName()))) {
                        SelectWallpaperScreen.wallpapers.add(new Wallpaper(file.getName(), GLTexture.fromPath("BThack/Wallpapers/" + file.getName(), PathMode.OUTSIDEJAR, GLTexture.ColorMode.RGBA)));
                    }
                }
            }
        }
    }

    public static void saveClientInfo() throws IOException {
        ConfigUtils.saveInJson("ClientInfo", "", jsonObject -> {
            add(jsonObject, "prefix", Client.clientInfo.getChatPrefix());
            add(jsonObject, "wallpaper", Client.clientInfo.getWallpaper());
            add(jsonObject, "font", Client.clientInfo.getFont());
        });
    }

    public static void loadClientInfo() throws IOException {
        ConfigUtils.loadFromJson("ClientInfo", "", jsonObject -> {
            if (!_null(jsonObject, "prefix")) Client.clientInfo.setChatPrefix(jsonObject.get("prefix").getAsString());
            if (!_null(jsonObject, "wallpaper")) Client.clientInfo.setWallpaper(jsonObject.get("wallpaper").getAsString());
            if (!_null(jsonObject, "font")) Client.clientInfo.setFont(jsonObject.get("font").getAsString());
            if (!Client.clientInfo.getWallpaper().equals("default") && Files.exists(Paths.get("BThack/Wallpapers/" + Client.clientInfo.getWallpaper())))
                BThackMainMenuScreen.mainMenuTexture = GLTexture.fromPath("BThack/Wallpapers/" + Client.clientInfo.getWallpaper(), PathMode.OUTSIDEJAR, GLTexture.ColorMode.RGBA);
                }
        ,() -> {});
    }

    private static final Set<String> imageFormats = new HashSet<>(Arrays.asList(
            "png", "jpg", "gif"
    ));

    public static void loadLanguages() {
        LanguageSystem.loadTranslations(ConfigUtils.newInputStream("assets/bthack/langs/EN.lng", PathMode.INSIDEJAR), "EN");
        LanguageSystem.loadTranslations(ConfigUtils.newInputStream("assets/bthack/langs/RU.lng", PathMode.INSIDEJAR), "RU");
        LanguageSystem.loadTranslations(ConfigUtils.newInputStream("assets/bthack/langs/PL.lng", PathMode.INSIDEJAR), "PL");

        PluginSystem.getLoadedPlugins().forEach(Plugin::onLoadLanguages);
    }
}
