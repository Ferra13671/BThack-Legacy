package com.ferra13671.BThack.core.client.systems.config;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.core.client.Client;
import com.ferra13671.BThack.core.client.ClientInfo;
import com.ferra13671.BThack.core.client.systems.file.FileSystem;
import com.ferra13671.BThack.api.category.Categories;
import com.ferra13671.BThack.gui.screen.clickgui.component.Frame;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.impl.cape.Cape;
import com.ferra13671.BThack.managers.impl.clans.Clan;
import com.ferra13671.BThack.managers.impl.clans.ClanSettingsBuilder;
import com.ferra13671.BThack.managers.impl.macros.Macro;
import com.ferra13671.BThack.managers.impl.setting.Settings.Setting;
import com.ferra13671.BThack.managers.impl.waypoint.Waypoint;
import com.ferra13671.BThack.api.module.HudComponent;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.utils.datalist.DataLists;
import com.ferra13671.BThack.impl.modules.misc.AutoAnvilEnchant;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotTask;
import com.google.gson.*;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ferra13671.BThack.core.client.systems.file.JsonUtils.*;

public final class SubConfigs {
    @SuppressWarnings("unused")
    public static final SubConfig MODULES = new SubConfig() {
        @Override
        protected void saveSubConfig() throws Exception {
            for (Module module : Client.getAllModules()) {
                ConfigUtils.saveInJson(module.getName(), "Modules", jsonObject -> {
                    JsonObject settingObject = new JsonObject();


                    add(jsonObject, "Name", module.getName());
                    add(jsonObject, "Enabled", module.isEnabled());
                    add(jsonObject, "Bind", module.getKey());
                    add(jsonObject, "Visible", module.isVisible());

                    if (Managers.SETTINGS_MANAGER.getSettingsByModule(module) != null) {
                        for (Setting<?> s : Managers.SETTINGS_MANAGER.getSettingsByModule(module)) {
                            s.save(settingObject);
                        }
                    }
                    add(jsonObject, "Settings", settingObject);
                });
            }
        }

        @Override
        protected void loadSubConfig() throws Exception {
            for (Module module : Client.getAllModules()) {
                ConfigUtils.loadFromJson(module.getName(), "Modules", jsonObject -> {
                    if (equalsNull(jsonObject, "Name", "Enabled", "Bind", "Visible")) return;

                    JsonObject settingObject = jsonObject.get("Settings").getAsJsonObject();

                    if (Managers.SETTINGS_MANAGER.getSettingsByModule(module) != null) {
                        for (Setting<?> s : Managers.SETTINGS_MANAGER.getSettingsByModule(module)) {
                            JsonElement settingValueObject;

                            settingValueObject = settingObject.get(s.getName());

                            if (settingValueObject != null) {
                                try {
                                    s.load(settingObject, settingValueObject);
                                } catch (Exception e) {
                                    //noinspection CallToPrintStackTrace
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    module.setEnabled(jsonObject.get("Enabled").getAsBoolean());
                    module.setKey(jsonObject.get("Bind").getAsInt());
                    module.setVisible(jsonObject.get("Visible").getAsBoolean());
                }, () -> {
                    if (module.isAutoEnabled()) {
                        module.setEnabled(true);
                    }
                });
            }
        }
    };
    @SuppressWarnings("unused")
    public static final SubConfig FRAMES = new SubConfig() {
        @Override
        protected void saveSubConfig() throws Exception {
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

        @Override
        protected void loadSubConfig() throws Exception {
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
    };
    public static final SubConfig HUD_COMPONENTS = new SubConfig() {
        @Override
        protected void saveSubConfig() throws Exception {
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

                    if (Managers.SETTINGS_MANAGER.getSettingsByModule(module) != null) {
                        for (Setting<?> s : Managers.SETTINGS_MANAGER.getSettingsByModule(module)) {
                            s.save(settingObject);
                        }
                    }
                    add(jsonObject, "Settings", settingObject);
                });
            }
        }

        @Override
        protected void loadSubConfig() throws Exception {
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
                                    hudComponent.setEnabled(jsonObject.get("Enabled").getAsBoolean());
                                }

                                if (!_null(jsonObject, "Settings")) {
                                    JsonObject settingObject = jsonObject.get("Settings").getAsJsonObject();

                                    if (Managers.SETTINGS_MANAGER.getSettingsByModule(module) != null) {
                                        for (Setting<?> s : Managers.SETTINGS_MANAGER.getSettingsByModule(module)) {
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
    };
    public static final SubConfig CLANS = new SubConfig() {
        @Override
        protected void saveSubConfig() throws Exception {
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

        @Override
        protected void loadSubConfig() throws Exception {
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
    };
    @SuppressWarnings("unused")
    public static final SubConfig ACTION_BOT_TASKS = new SubConfig() {
        @Override
        protected void saveSubConfig() throws Exception {
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

        @Override
        protected void loadSubConfig() throws Exception {
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
    };
    public static final SubConfig WAYPOINTS = new SubConfig() {
        @Override
        protected void saveSubConfig() throws Exception {
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

        @Override
        protected void loadSubConfig() throws Exception {
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
    };
    @SuppressWarnings("unused")
    public static final SubConfig MACROS = new SubConfig() {
        @Override
        protected void saveSubConfig() throws Exception {
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

        @Override
        protected void loadSubConfig() throws Exception {
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
    };
    public static final SubConfig TWO_FA_KEYS = new SubConfig() {
        @Override
        protected void saveSubConfig() throws Exception {
            Managers.TWOFA_MANAGER.save();
        }

        @Override
        protected void loadSubConfig() throws Exception {
            Managers.TWOFA_MANAGER.load();
        }
    };
    @SuppressWarnings("unused")
    public static final SubConfig DATA_LISTS = new SubConfig() {
        @Override
        protected void saveSubConfig() {
            DataLists.forEach(dataList -> {
                try {
                    dataList.saveInFile();
                } catch (IOException e) {
                    //noinspection CallToPrintStackTrace
                    e.printStackTrace();
                }
            });
        }

        @Override
        protected void loadSubConfig() {
            DataLists.forEach(dataList -> {
                try {
                    dataList.loadFromFile();
                } catch (IOException e) {
                    BThack.error(e.getMessage());
                }
            });
        }
    };
    @SuppressWarnings("unused")
    public static final SubConfig AUTO_AUTH = new SubConfig() {
        @Override
        protected void saveSubConfig() throws Exception {
            Managers.AUTO_AUTH_MANAGER.save();
        }

        @Override
        protected void loadSubConfig() throws Exception {
            Managers.AUTO_AUTH_MANAGER.load();
        }
    };
    @SuppressWarnings("unused")
    public static final SubConfig ACCOUNTS = new SubConfig() {
        @Override
        protected void saveSubConfig() throws Exception {
            Managers.ACCOUNT_MANAGER.save();
        }

        @Override
        protected void loadSubConfig() throws Exception {
            Managers.ACCOUNT_MANAGER.load();
        }
    };
    @SuppressWarnings("unused")
    public static final SubConfig AUTO_ANVIL_ENCHANT = new SubConfig() {
        @Override
        protected void saveSubConfig() throws Exception {
            AutoAnvilEnchant.save();
        }

        @Override
        protected void loadSubConfig() throws Exception {
            AutoAnvilEnchant.load();
        }
    };
    public static final SubConfig CLIENT_INFO = new SubConfig() {
        @Override
        protected void saveSubConfig() throws Exception {
            ConfigUtils.saveInJson("ClientInfo", "", jsonObject -> {
                add(jsonObject, "prefix", Client.clientInfo.getChatPrefix());
                add(jsonObject, "font", Client.clientInfo.getFont());
                JsonObject capeInfoObject = new JsonObject();
                add(capeInfoObject, "dataPath", Client.clientInfo.getCapeInfo().dataPath());
                add(capeInfoObject, "type", Client.clientInfo.getCapeInfo().dataType().name());
                add(jsonObject, "capeInfo", capeInfoObject);
            });
        }

        @Override
        protected void loadSubConfig() throws Exception {
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

        @SuppressWarnings("resource")
        private static void loadCape() throws Exception {
            InputStream stream = switch (Client.clientInfo.getCapeInfo().dataType()) {
                case NONE -> null;
                case FILE -> Files.newInputStream(Paths.get(Client.clientInfo.getCapeInfo().dataPath()));
                case URL -> new URI(Client.clientInfo.getCapeInfo().dataPath()).toURL().openStream();
            };
            if (stream != null)
                Managers.CAPE_MANAGER.setCape(Cape.fromInputStream(stream));
        }
    };


    private static final List<SubConfig> subConfigs = new ArrayList<>();
    static {
        try {
            for (Field f : SubConfigs.class.getFields()) {
                Object field = f.get(null);
                if (field instanceof SubConfig subConfig)
                    subConfigs.add(subConfig);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<SubConfig> getSubConfigs() {
        return subConfigs;
    }
}
