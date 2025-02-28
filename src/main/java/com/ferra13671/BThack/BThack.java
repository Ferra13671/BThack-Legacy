package com.ferra13671.BThack;

import com.ferra13671.BTbot.api.Utils.Controller.ClientPlayerController;
import com.ferra13671.BThack.Core.BThackUpdater.BThackUpdater;
import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigSystem;
import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigUtils;
import com.ferra13671.BThack.Core.FileSystem.FileSystem;
import com.ferra13671.BThack.Core.FileSystem.JsonUtils;
import com.ferra13671.BThack.Core.DeviceSystem;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.ShutdownSystem;
import com.ferra13671.BThack.api.GuiSystem.BThackWidgets;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Plugin.Plugin;
import com.ferra13671.BThack.api.Plugin.PluginSystem;
import com.ferra13671.BThack.api.Social.SocialManager;
import com.ferra13671.BThack.api.Social.SocialManagers;
import com.ferra13671.BThack.api.SoundSystem.Sounds;
import com.ferra13671.BThack.api.SoundSystem.yaw.TinySound;
import com.ferra13671.BThack.api.GuiSystem.BThackScreens;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.MegaEvents.Base.IEventBus;
import com.ferra13671.MegaEvents.Base.UpdatedEventBus;
import com.google.gson.JsonPrimitive;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;

public final class BThack implements ClientModInitializer, Mc {

    public final VersionInfo versionInfo = new VersionInfo();
    public final String MC_VERSION;
    public final String VERSION;
    private InitStage initStage = InitStage.NOT_INITED;
    private final boolean withBaritone;
    public final ClientPlayerController playerController = new ClientPlayerController();

    public static BThack instance;

    public static final Logger logger = LoggerFactory.getLogger("BThack");
    public static final IEventBus EVENT_BUS = new UpdatedEventBus();
    public static final String APP_ID = "1221431287852826676";

    public BThack() {
        ModMetadata mod = FabricLoader.getInstance().getModContainer("bthack").get().getMetadata();
        MC_VERSION = mod.getCustomValue("mcVersion").getAsString();
        withBaritone = !mod.getVersion().getFriendlyString().equals("-NoBaritone");
        VERSION = mod.getVersion().getFriendlyString();
    }

    public static void log(String message) {
        logger.info(message);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static void debug(String message) {
        if (instance.versionInfo.isSendDebug())
            logger.info("[DEBUG] {}", message);
    }

    public static boolean isBaritonePresent() {
        FabricLoader fl = FabricLoader.getInstance();
        return fl.getModContainer("baritone").isPresent() || fl.getModContainer("baritone-meteor").isPresent();
    }

    public static boolean isFuturePresent() {
        return FabricLoader.getInstance().getModContainer("future").isPresent();
    }

    public boolean withBaritone() {
        return withBaritone;
    }

    @Override
    public void onInitializeClient() {
        if (initStage == InitStage.CLIENT_INIT) throw new UnsupportedOperationException("You cannot call client initialization inside client initialization");
        if (initStage == InitStage.POST_INIT) throw new UnsupportedOperationException("You cannot call an already passed initialization stage");
        if (initStage == InitStage.FULL_INITED) throw new UnsupportedOperationException("You cannot call initialization after a full initialization has been performed");

        initStage = InitStage.CLIENT_INIT;

        instance = this;

        checkForOutdate();
        loadVersionInfo();

        logBThackLogo();

        initLog("BThack initialization has begun. Your nickname: " + mc.getSession().getUsername());
        DeviceSystem.check();

        PluginSystem.loadPlugins();

        try {
            initDebug("Starting to create BThack directory...");
            FileSystem.start();
            FileSystem.createTutorialJsonTheme();
            initDebug("BThack directory successfully created!");
        } catch (IOException e) {
            initErr("There was an error when creating the BThack directory.");
            throw new RuntimeException(e);
        }

        initDebug("Starting initialization of the sound engine...");
        TinySound.init();
        Sounds.initSounds();
        if (TinySound.isInitialized()) {
            initDebug("The sound engine has been successfully initialized!");
        } else {
            initErr("The sound engine is not initialized!");
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

        initDebug("Starting to upload social info...");
        try {
            for (Field field : SocialManagers.class.getFields()) {
                if ( Modifier.isStatic(field.getModifiers())
                        && Modifier.isPublic(field.getModifiers())
                        && Modifier.isFinal(field.getModifiers())
                        && field.getType().equals(SocialManager.class)
                ) {
                    SocialManager socialManager = (SocialManager) field.get(null);
                    socialManager.load();
                }
            }
        } catch (Exception e) {
            initErr("There was an error loading social info!");
            e.printStackTrace(); //Okay
        }

        PluginSystem.getLoadedPlugins().forEach(Plugin::preInit);
    }

    public void onInitializePost() {
        if (initStage == InitStage.NOT_INITED) throw new UnsupportedOperationException("You cannot call post initialization if Client initialization has not been performed");
        if (initStage == InitStage.POST_INIT) throw new UnsupportedOperationException("You cannot call post initialization inside post initialization");
        if (initStage == InitStage.FULL_INITED) throw new UnsupportedOperationException("You cannot call initialization after a full initialization has been performed");

        initStage = InitStage.POST_INIT;

        BThack.initDebug("Starting to upload color themes....");
        try {
            ConfigSystem.loadColourThemes();
            BThack.initDebug("Color themes has loaded!");
        } catch (IOException e) {
            BThack.initErr("There was a error when loading color themes!");
            throw new RuntimeException(e);
        }

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
            e.printStackTrace();
        }

        BThackRender.init();

        ShutdownSystem.init();
        ShutdownSystem.addShutdownHook(() -> {
            ModuleList.timer.setToggled(false);
            ConfigSystem.saveConfig();
            try {
                ConfigSystem.saveHudComponents();
            } catch (IOException e) {
                BThack.error(e.getMessage());
            }
            BThack.instance.saveVersionInfo();
            BThack.log("Config Saved!");
        });

        PluginSystem.getLoadedPlugins().forEach(Plugin::postInit);


        BThack.initLog("BThack is fully initialized and ready for further work. Enjoy your game!");
        initStage = InitStage.FULL_INITED;
    }

    private void checkForOutdate() {
        try {
            String text = new BufferedReader(new InputStreamReader(new URI("https://raw.githubusercontent.com/Ferra13671/BThack/" + MC_VERSION + "/currentVersion.txt").toURL().openStream())).readLine();
            if (!text.equals(VERSION)) {
                versionInfo.setOutdated(true);
                versionInfo.setNewVersion(text);
            }
        } catch (Exception ignored) {
            error("Failed getting information on the current release.");
        }
    }

    private void loadVersionInfo() {
        try {
            ConfigUtils.loadFromJson("VersionInfo", "", jsonObject -> {
                if (!JsonUtils.equalsNull(jsonObject, "lastCheckVersion", "needShowAgainOneRelease")) {
                    if (!jsonObject.get("lastCheckVersion").getAsString().equals(versionInfo.getNewVersion())) {
                        versionInfo.setNeedShowAgainOneRelease(true);
                    } else {
                        versionInfo.setNewVersion(jsonObject.get("lastCheckVersion").getAsString());
                        versionInfo.setNeedShowAgainOneRelease(jsonObject.get("needShowAgainOneRelease").getAsBoolean());
                    }
                }
                if (!JsonUtils._null(jsonObject, "needShowAgainAllReleases"))
                    versionInfo.setNeedShowAgainAllReleases(jsonObject.get("needShowAgainAllReleases").getAsBoolean());
                if (!JsonUtils._null(jsonObject, "firstLaunched"))
                    versionInfo.setFirstLaunched(jsonObject.get("firstLaunched").getAsBoolean());
                if (!JsonUtils._null(jsonObject, "sendDebug"))
                    versionInfo.setSendDebug(jsonObject.get("sendDebug").getAsBoolean());
            }, () -> {});
        } catch (IOException ignored) {}
    }

    public void saveVersionInfo() {
        try {
            ConfigUtils.saveInJson("VersionInfo", "", jsonObject -> {
                jsonObject.add("lastCheckVersion", new JsonPrimitive(versionInfo.getNewVersion()));
                jsonObject.add("needShowAgainOneRelease", new JsonPrimitive(versionInfo.isNeedShowAgainOneRelease()));
                jsonObject.add("needShowAgainAllReleases", new JsonPrimitive(versionInfo.isNeedShowAgainAllReleases()));
                jsonObject.add("firstLaunched", new JsonPrimitive(versionInfo.isFirstLaunched()));
                jsonObject.add("sendDebug", new JsonPrimitive(versionInfo.isSendDebug()));
            });
        } catch (IOException ignored) {}
    }

    private void logBThackLogo() {
        log(" ---------------------------------------------------------------------------------------------------------------------------------------- ");
        log("|    ##############        ###################    ####                                                               ####                |");
        log("|    ##############        ###################    ####                                                               ####                |");
        log("|    ####          ####            ####           ####                       ##########            ##########        ####        ####    |");
        log("|    ####          ####            ####           ####    #######            ##########            ##########        ####        ####    |");
        log("|    ##############                ####           ###################                  ####    ####          ####    ####    ####        |");
        log("|    ##############                ####           ########       ####                  ####    ####          ####    ####    ####        |");
        log("|    ####          ####            ####           ####           ####        ##############    ####                  ########            |");
        log("|    ####          ####            ####           ####           ####        ##############    ####                  ########            |");
        log("|    ####          ####            ####           ####           ####    ####          ####    ####          ####    ####    ####        |");
        log("|    ####          ####            ####           ####           ####    ####          ####    ####          ####    ####    ####        |");
        log("|    ##############                ####           ####           ####        ##############        ##########        ####        ####    |");
        log("|    ##############                ####           ####           ####        ##############        ##########        ####        ####    |");
        log(" ---------------------------------------------------------------------------------------------------------------------------------------- ");
    }

    public static void initLog(CharSequence message) {
        String line = " ";
        for (int i = 2; i < message.length(); i++) {
            line = line + "-";
        }

        log(line);
        log(message.toString());
        log(line);
    }

    public static void initDebug(CharSequence message) {
        String line = " ";
        for (int i = 2; i < message.length(); i++) {
            line = line + "-";
        }

        debug(line);
        debug(message.toString());
        debug(line);
    }

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

    private enum InitStage {
        NOT_INITED,
        CLIENT_INIT,
        POST_INIT,
        FULL_INITED
    }
}
