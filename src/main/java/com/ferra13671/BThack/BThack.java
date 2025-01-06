package com.ferra13671.BThack;

import com.ferra13671.BTbot.api.Utils.Controller.ClientPlayerController;
import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigSystem;
import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigUtils;
import com.ferra13671.BThack.Core.FileSystem.FileSystem;
import com.ferra13671.BThack.Core.FileSystem.JsonUtils;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.api.Gui.HudMover.HudMoverScreen;
import com.ferra13671.BThack.api.Gui.MainMenu.BThackMainMenuScreen;
import com.ferra13671.BThack.api.Gui.ClickGui.ClickGuiScreen;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Plugin.Plugin;
import com.ferra13671.BThack.api.Plugin.PluginSystem;
import com.ferra13671.BThack.api.Social.SocialManager;
import com.ferra13671.BThack.api.Social.SocialManagers;
import com.ferra13671.BThack.api.SoundSystem.Sounds;
import com.ferra13671.BThack.api.SoundSystem.yaw.TinySound;
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
import java.net.URL;

public final class BThack implements ClientModInitializer, Mc {

    public final VersionInfo versionInfo = new VersionInfo();
    public final String MC_VERSION;
    public final String VERSION;
    private InitStage initStage = InitStage.NOT_INITED;
    public final ClientPlayerController playerController = new ClientPlayerController();

    public static BThack instance;

    public static final Logger logger = LoggerFactory.getLogger("BThack");
    public static final IEventBus EVENT_BUS = new UpdatedEventBus();
    public static final String APP_ID = "1221431287852826676";

    public ClickGuiScreen clickGui;
    public BThackMainMenuScreen mainMenu;
    public HudMoverScreen hudMoverScreen;

    public BThack() {
        ModMetadata mod = FabricLoader.getInstance().getModContainer("bthack").get().getMetadata();
        MC_VERSION = mod.getCustomValue("mcVersion").getAsString();
        VERSION = mod.getVersion().getFriendlyString();
    }

    public static void log(String message) {
        logger.info(message);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static boolean isBaritonePresent() {
        FabricLoader fl = FabricLoader.getInstance();
        return fl.getModContainer("baritone").isPresent() || fl.getModContainer("baritone-meteor").isPresent();
    }

    @Override
    public void onInitializeClient() {
        if (initStage == InitStage.CLIENT_INIT) throw new UnsupportedOperationException("You cannot call client initialization inside client initialization");
        if (initStage == InitStage.POST_INIT) throw new UnsupportedOperationException("You cannot call an already passed initialization stage");
        if (initStage == InitStage.FULL_INITED) throw new UnsupportedOperationException("You cannot call initialization after a full initialization has been performed");

        initStage = InitStage.CLIENT_INIT;

        logBThackLogo();

        initLog("BThack initialization has begun. Your nickname: " + mc.getSession().getUsername());

        PluginSystem.loadPlugins();

        try {
            initLog("Starting to create BThack directory...");
            FileSystem.start();
            FileSystem.createTutorialJsonTheme();
            initLog("BThack directory successfully created!");
        } catch (IOException e) {
            initErr("There was an error when creating the BThack directory.");
            throw new RuntimeException(e);
        }

        initLog("Starting initialization of the sound engine...");
        TinySound.init();
        Sounds.initSounds();
        if (TinySound.isInitialized()) {
            initLog("The sound engine has been successfully initialized!");
        } else {
            initErr("The sound engine is not initialized!");
        }

        initLog("Starting loading languages...");
        try {
            ConfigSystem.loadLanguages();
        } catch (Exception e) {
            initErr("There was an error loading languages!");
        }

        initLog("Starting loading ActionBot tasks...");
        try {
            ActionBotConfig.loadActionBotTasksData();
        } catch (Exception e) {
            initErr("There was an error loading ActionBot tasks!");
        }

        instance = this;

        initLog("Starting to upload social info...");
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

        checkForOutdate();
        loadVersionInfo();

        PluginSystem.getLoadedPlugins().forEach(Plugin::preInit);
    }

    public void onInitializePost() {
        if (initStage == InitStage.NOT_INITED) throw new UnsupportedOperationException("You cannot call post initialization if Client initialization has not been performed");
        if (initStage == InitStage.POST_INIT) throw new UnsupportedOperationException("You cannot call post initialization inside post initialization");
        if (initStage == InitStage.FULL_INITED) throw new UnsupportedOperationException("You cannot call initialization after a full initialization has been performed");

        initStage = InitStage.POST_INIT;

        BThack.initLog("Starting to upload color themes....");
        try {
            ConfigSystem.loadColourThemes();
            BThack.initLog("Color themes has loaded!");
        } catch (IOException e) {
            BThack.initErr("There was a error when loading color themes!");
            throw new RuntimeException(e);
        }

        BThack.initLog("Starting client initialization...");
        Client.startup();
        if (Client.inited) {
            BThack.initLog("Client initialized!");
        } else {
            BThack.initErr("There was an error during client initialization! Further work is impossible!");
            throw new RuntimeException();
        }

        BThack.instance.clickGui = new ClickGuiScreen();
        BThack.instance.mainMenu = new BThackMainMenuScreen();
        BThack.instance.hudMoverScreen = new HudMoverScreen();

        BThack.initLog("Starting loading the config...");
        try {
            ConfigSystem.loadConfig();
            BThack.initLog("Config successfully uploaded!");
        } catch (Exception e) {
            BThack.initErr("There was an error when loading the config. Further work may occur with failures.");
            e.printStackTrace();
        }

        /*

        It wasn't very good, so I changed the way it worked :/

        ThreadManager.startNewThread(thread -> {
            do {
                if (BThack.instance.clickGui != null) {
                    if (mc.currentScreen == BThack.instance.clickGui) {
                        BThack.instance.clickGui.clickGuiTick();
                    }
                }
                try {
                    thread.sleep(10);
                } catch (InterruptedException ignored) {}
            } while (true);
        });

         */

        BThackRender.init();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ModuleList.timer.setToggled(false);
            ConfigSystem.saveConfig();
            try {
                ConfigSystem.saveHudComponents();
            } catch (IOException ignored) {}
            BThack.instance.saveVersionInfo();
            BThack.log("Config Saved!");
        }));

        PluginSystem.getLoadedPlugins().forEach(Plugin::postInit);


        BThack.initLog("BThack is fully initialized and ready for further work. Enjoy your game!");
        initStage = InitStage.FULL_INITED;
    }

    private void checkForOutdate() {
        try {
            String text = new BufferedReader(new InputStreamReader(new URL("https://raw.githubusercontent.com/Ferra13671/BThack/" + MC_VERSION + "/currentVersion.txt").openStream())).readLine();
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
