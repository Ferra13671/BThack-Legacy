package com.ferra13671.BThack;

import com.ferra13671.BThack.core.client.Client;
import com.ferra13671.BThack.core.client.ModuleList;
import com.ferra13671.BThack.core.client.systems.config.ConfigSystem;
import com.ferra13671.BThack.core.client.systems.config.ConfigUtils;
import com.ferra13671.BThack.core.client.systems.file.FileSystem;
import com.ferra13671.BThack.core.client.systems.file.JsonUtils;
import com.ferra13671.BThack.core.DeviceSystem;
import com.ferra13671.BThack.core.render.BThackRender;
import com.ferra13671.BThack.core.ShutdownSystem;
import com.ferra13671.BThack.core.client.systems.gui.BThackWidgets;
import com.ferra13671.BThack.api.utils.Mc;
import com.ferra13671.BThack.api.plugin.Plugin;
import com.ferra13671.BThack.api.plugin.PluginSystem;
import com.ferra13671.BThack.core.client.systems.gui.BThackScreens;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThackData;
import com.ferra13671.MegaEvents.Base.IEventBus;
import com.ferra13671.MegaEvents.Base.EventBus;
import com.google.gson.JsonPrimitive;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public final class BThack implements ModInitializer, Mc {
    public static final IEventBus EVENT_BUS = new EventBus();
    public static final VersionInfo VERSION_INFO = new VersionInfo();
    public static final String MC_VERSION = BThackData.MC_VERSION;
    public static final String VERSION = BThackData.VERSION;
    public static final String RELEASE_VERSION = BThackData.RELEASE_VERSION;
    public static final boolean WITH_BARITONE = BThackData.WITH_BARITONE;

    public static BThack instance;

    public static void log(String message) {
        Constants.BTHACK_LOGGER.info(message);
    }

    public static void error(String message) {
        Constants.BTHACK_LOGGER.error(message);
    }

    public static void debug(String message) {
        if (VERSION_INFO.isSendDebug())
            Constants.BTHACK_LOGGER.info("[DEBUG] {}", message);
    }

    public static boolean isBaritonePresent() {
        FabricLoader fl = FabricLoader.getInstance();
        return fl.getModContainer("baritone").isPresent() || fl.getModContainer("baritone-meteor").isPresent();
    }

    public static boolean isFuturePresent() {
        return FabricLoader.getInstance().getModContainer("future").isPresent();
    }

    @Override
    public void onInitialize() {
        logBThackLogo();
        initLog("BThack initialization has begun. Your nickname: " + mc.getSession().getUsername());
        instance = this;

        checkForOutdate();
        loadVersionInfo();

        DeviceSystem.check();

        PluginSystem.loadPlugins();

        try {
            initDebug("Starting to create BThack directory...");
            FileSystem.start();
            initDebug("BThack directory successfully created!");
        } catch (IOException e) {
            initErr("There was an error when creating the BThack directory.");
            throw new RuntimeException(e);
        }

        initDebug("Starting loading languages...");
        try {
            ConfigSystem.loadLanguages();
        } catch (Exception e) {
            initErr("There was an error loading languages!");
        }

        initDebug("Starting loading ActionBot tasks...");
        try {
            ActionBotConfig.loadActionBotTasksData();
        } catch (Exception e) {
            initErr("There was an error loading ActionBot tasks!");
        }

        PluginSystem.getLoadedPlugins().forEach(Plugin::preInit);
    }

    public void onInitializePost() {
        BThackRender.init();
        BThack.initDebug("Starting client initialization...");
        Client.startup();
        if (Client.inited) {
            BThack.initDebug("Client initialized!");
        } else {
            BThack.initErr("There was an error during client initialization! Further work is impossible!");
            throw new RuntimeException();
        }

        BThackWidgets.init();
        BThackScreens.init();

        BThack.initDebug("Starting loading the config...");
        try {
            ConfigSystem.loadConfig();
            BThack.initDebug("Config successfully uploaded!");
        } catch (Exception e) {
            BThack.initErr("There was an error when loading the config. Further work may occur with failures.");
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }

        ShutdownSystem.init();
        ShutdownSystem.addShutdownHook(() -> {
            ModuleList.timer.setEnabled(false);
            ConfigSystem.saveConfig();
            BThack.instance.saveVersionInfo();
            BThack.log("Config Saved!");
        });

        PluginSystem.getLoadedPlugins().forEach(Plugin::postInit);


        BThack.initLog("BThack is fully initialized and ready for further work. Enjoy your game!");
    }

    private void checkForOutdate() {
        try {
            String text = new BufferedReader(new InputStreamReader(new URI("https://raw.githubusercontent.com/Ferra13671/BThack/" + MC_VERSION + "/currentVersion.txt").toURL().openStream())).readLine();
            if (!text.equals(RELEASE_VERSION)) {
                VERSION_INFO.setOutdated(true);
                VERSION_INFO.setNewVersion(text);
            }
        } catch (Exception ignored) {
            error("Failed getting information on the current release.");
        }
    }

    private void loadVersionInfo() {
        try {
            ConfigUtils.loadFromJson("VersionInfo", "", jsonObject -> {
                if (!JsonUtils.equalsNull(jsonObject, "lastCheckVersion", "needShowAgainOneRelease")) {
                    if (!jsonObject.get("lastCheckVersion").getAsString().equals(VERSION_INFO.getNewVersion())) {
                        VERSION_INFO.setNeedShowAgainOneRelease(true);
                    } else {
                        VERSION_INFO.setNewVersion(jsonObject.get("lastCheckVersion").getAsString());
                        VERSION_INFO.setNeedShowAgainOneRelease(jsonObject.get("needShowAgainOneRelease").getAsBoolean());
                    }
                }
                if (!JsonUtils._null(jsonObject, "needShowAgainAllReleases"))
                    VERSION_INFO.setNeedShowAgainAllReleases(jsonObject.get("needShowAgainAllReleases").getAsBoolean());
                if (!JsonUtils._null(jsonObject, "firstLaunched"))
                    VERSION_INFO.setFirstLaunched(jsonObject.get("firstLaunched").getAsBoolean());
                if (!JsonUtils._null(jsonObject, "sendDebug"))
                    VERSION_INFO.setSendDebug(jsonObject.get("sendDebug").getAsBoolean());
            }, () -> {});
        } catch (IOException ignored) {}
    }

    public void saveVersionInfo() {
        try {
            ConfigUtils.saveInJson("VersionInfo", "", jsonObject -> {
                jsonObject.add("lastCheckVersion", new JsonPrimitive(VERSION_INFO.getNewVersion()));
                jsonObject.add("needShowAgainOneRelease", new JsonPrimitive(VERSION_INFO.isNeedShowAgainOneRelease()));
                jsonObject.add("needShowAgainAllReleases", new JsonPrimitive(VERSION_INFO.isNeedShowAgainAllReleases()));
                jsonObject.add("firstLaunched", new JsonPrimitive(VERSION_INFO.isFirstLaunched()));
                jsonObject.add("sendDebug", new JsonPrimitive(VERSION_INFO.isSendDebug()));
            });
        } catch (IOException ignored) {}
    }

    private void logBThackLogo() {
        log("/````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````\\");
        log("|   /##############       /###################   /####                                                              /####                |");
        log("|   /##############       /###################   /####                                                              /####                |");
        log("|   /####//////////####   /////////####///////   /####                      /##########           /##########       /####       /####    |");
        log("|   /####         /####           /####          /####   /#######           /##########           /##########       /####       /####    |");
        log("|   /##############////           /####          /###################       ///////////####   /####//////////####   /####   /####////    |");
        log("|   /##############               /####          /########///////####                 /####   /####         /####   /####   /####        |");
        log("|   /####//////////####           /####          /####////      /####       /##############   /####         /////   /########////        |");
        log("|   /####         /####           /####          /####          /####       /##############   /####                 /########            |");
        log("|   /####         /####           /####          /####          /####   /####//////////####   /####         /####   /####////####        |");
        log("|   /####         /####           /####          /####          /####   /####          ####   /####         /####   /####   /####        |");
        log("|   /##############////           /####          /####          /####   /////##############   /////##########////   /####   /////####    |");
        log("|   /##############               /####          /####          /####       /##############       /##########       /####       /####    |");
        log("|   ///////////////               /////          /////          /////       ///////////////       ///////////       /////       /////    |");
        log("\\,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,/");
    }

    @SuppressWarnings("StringConcatenationInLoop")
    public static void initLog(CharSequence message) {
        String line = " ";
        for (int i = 2; i < message.length(); i++) {
            line = line + "-";
        }

        log(line);
        log(message.toString());
        log(line);
    }

    @SuppressWarnings("StringConcatenationInLoop")
    public static void initDebug(CharSequence message) {
        String line = " ";
        for (int i = 2; i < message.length(); i++) {
            line = line + "-";
        }

        debug(line);
        debug(message.toString());
        debug(line);
    }

    @SuppressWarnings("StringConcatenationInLoop")
    public static void initErr(CharSequence message) {
        String messageText = "ERROR: " + message;
        String line = " ";
        for (int i = 2; i < messageText.length(); i++) {
            line = line + "-";
        }

        error(line);
        error(message.toString());
        error(line);
    }
}
