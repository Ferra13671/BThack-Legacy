package com.ferra13671.BThack.Core.Client;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.DeviceSystem;
import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigSystem;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Plugin.Plugin;
import com.ferra13671.BThack.api.Plugin.PluginSystem;
import com.ferra13671.BThack.api.Plugin.PluginUtils;
import com.ferra13671.BThack.impl.HudComponents.*;
import com.ferra13671.BThack.impl.HudComponents.OneTextComponents.*;
import com.ferra13671.BThack.impl.Modules.CLIENT.*;
import com.ferra13671.BThack.impl.Modules.COMBAT.*;
import com.ferra13671.BThack.impl.Modules.COMBAT.KillAura;
import com.ferra13671.BThack.impl.Modules.MISC.*;
import com.ferra13671.BThack.impl.Modules.MISC.PacketMine.PacketMine;
import com.ferra13671.BThack.impl.Modules.MOVEMENT.*;
import com.ferra13671.BThack.impl.Modules.PLAYER.*;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.ActionBot;
import com.ferra13671.BThack.impl.Modules.PLAYER.Spammer.Spammer;
import com.ferra13671.BThack.impl.Modules.RENDER.*;
import com.ferra13671.BThack.impl.Modules.RENDER.HoleESP;
import com.ferra13671.BThack.impl.Modules.WORLD.*;

import java.io.IOException;
import java.util.Arrays;

public final class ModuleList {
    //CLIENT
    public static BThackMainMenu bthackMainMenu;
    public static ChatNotifications chatNotifications;
    public static ClickGui clickGui;
    public static ClientSettings clientSettings;
    public static CustomCape customCape;
    public static CustomFont customFont;
    public static DiscordRPC discordRPC;
    public static FPSReducer fpsReducer;
    public static HUD HUD;
    public static HudEditor hudEditor;
    public static Macros macros;
    public static MemoryCleaner memoryCleaner;
    public static MenuShader menuShader;

    //COMBAT
    public static AimBot aimBot;
    public static AutoArmor autoArmor;
    public static AutoClicker autoClicker;
    public static AutoOffhand autoOffhand;
    public static AutoShield autoShield;
    public static AutoSoup autoSoup;
    public static AutoSword autoSword;
    public static AutoTotemFall autoTotemFall;
    public static Criticals criticals;
    public static FastBow fastBow;
    public static FireBallAura fireBallAura;
    public static HitSound hitSound;
    public static HoleFill holeFill;
    public static IgniteAura igniteAura;
    public static KillAura killAura;
    //public static LavaAura lavaAura;
    public static NoFriendDamage noFriendDamage;
    public static PearlPhase pearlPhase;
    public static Surround surround;
    public static TNTIgniter tntIgniter;
    public static TotemPopNotifier totemPopNotifier;
    public static WebAura webAura;
    public static WitherRoseAura witherRoseAura;

    //MISC
    public static ActiveSpawnerDetect activeSpawnerDetect;
    public static AntiHunger antiHunger;
    public static Auto2FA auto2FA;
    public static AutoAuth autoAuth;
    public static Breaker breaker;
    public static CleanMemory cleanMemory;
    public static DeathCamera deathCamera;
    public static HighwayBuilder highwayBuilder;
    public static InstaNuker instaNuker;
    public static ItemRandomizer itemRandomizer;
    public static MiddleClick middleClick;
    public static MoreChatHistory moreChatHistory;
    public static NoBreakDelay noBreakDelay;
    public static NoPacketKick noPacketKick;
    public static NoSoundLag noSoundLag;
    public static OpenedGuiInfo openedGuiInfo;
    public static PacketCanceller packetCanceller;
    public static PacketMine packetMine;
    public static PistonSoundDelay pistonSoundDelay;
    public static PortalGod portalGod;
    public static Scrapper scrapper;
    public static SuperInstaMine superInstaMine;
    public static Timer timer;
    public static TopperRadar topperRadar;
    public static TrashThrower trashThrower;
    public static TreeCutter treeCutter;
    public static TridentDupe tridentDupe;
    public static VisualRange visualRange;

    //MOVEMENT
    public static AntiAFK antiAFK;
    public static AutoJump autoJump;
    public static AutoWalk autoWalk;
    public static Blink blink;
    public static CameraRotator cameraRotator;
    public static CustomFriction customFriction;
    public static ElytraFastClose elytraFastClose;
    public static ElytraFlight elytraFlight;
    public static ElytraStrafe elytraStrafe;
    public static EntitySpeed entitySpeed;
    public static ExtendedFirework extendedFirework;
    public static FastFall fastFall;
    public static Flip flip;
    public static GuiMove guiMove;
    public static Impulse impulse;
    public static KeepSprint keepSprint;
    public static LevitationControl levitationControl;
    public static LongJump longJump;
    public static NinjaBridge ninjaBridge;
    public static NoFall noFall;
    public static NoJumpDelay noJumpDelay;
    public static NoPush noPush;
    public static NoRotate noRotate;
    public static NoSlow noSlow;
    public static NoSRotations noSRotations;
    public static Parkour parkour;
    public static SafeWalk safeWalk;
    public static Scaffold scaffold;
    public static ShiftSpam shiftSpam;
    public static Speed speed;
    public static Sprint sprint;
    public static Strafe strafe;
    public static Velocity velocity;

    //PLAYER
    public static ActionBot actionBot;
    public static AutoDisconnect autoDisconnect;
    public static AutoEat autoEat;
    public static AutoElytra autoElytra;
    public static AutoFirework autoFirework;
    public static AutoFish autoFish;
    public static AutoMend autoMend;
    public static AutoMine autoMine;
    public static AutoMount autoMount;
    public static AutoPearl autoPearl;
    public static AutoRespawn autoRespawn;
    public static AutoTool autoTool;
    public static BabyModel babyModel;
    public static ChestStealer chestStealer;
    public static ElytraReplace elytraReplace;
    public static ElytraSwap elytraSwap;
    public static FakePlayer fakePlayer;
    public static FastDrop fastDrop;
    public static FastPlace fastPlace;
    public static FastUse fastUse;
    public static FreeCam freeCam;
    public static ItemSaver itemSaver;
    public static LagDetector lagDetector;
    public static MultiFakePlayer multiFakePlayer;
    public static NoElytraBreak noElytraBreak;
    public static NoGlitchBlocks noGlitchBlocks;
    public static NoServerSlot noServerSlot;
    public static PacketPlace packetPlace;
    public static PMSpammer pmSpammer;
    public static Replanish replanish;
    public static Sneak sneak;
    public static Spammer spammer;
    public static XCarry xCarry;

    //RENDER
    public static AntiHazard antiHazard;
    public static AttackTrace attackTrace;
    public static BetterChat betterChat;
    public static BlockHighlight blockHighlight;
    public static CameraClip cameraClip;
    public static ChestESP chestESP;
    public static CS_Crosshair csCrosshair;
    public static CustomBob customBob;
    public static EnchantColor enchantColor;
    public static ESP ESP;
    public static ExtraTab extraTab;
    public static FullBright fullBright;
    public static FXAA FXAA;
    public static HandTweaks handTweaks;
    public static HoleESP holeESP;
    public static LastOpenChest lastOpenChest;
    public static MinecraftShaders minecraftShaders;
    public static ModifyCamera modifyCamera;
    public static MotionBlur motionBlur;
    public static Nametags nametags;
    public static NewChunks newChunks;
    public static NoFog noFog;
    public static NoOverlay noOverlay;
    public static NoRender noRender;
    public static NoSwing noSwing;
    public static PasswordHider passwordHider;
    public static PhaseESP phaseESP;
    public static Radar radar;
    public static Search search;
    public static Shaders shaders;
    public static Tooltips tooltips;
    public static Tracers tracers;
    public static Waypoints waypoints;
    public static Xray xray;

    //WORLD
    public static AutoFarm autoFarm;
    public static AutoFarmland autoFarmland;
    public static AutoShear autoShear;
    public static AutoSign autoSign;
    public static CloudsColor cloudsColor;
    public static CustomDayTime customDayTime;
    public static Fly fly;
    public static FogColor fogColor;
    public static Lawnmower lawnmower;
    public static LiquidFiller liquidFiller;
    public static NoWeather noWeather;
    public static Reach reach;
    public static SkyColor skyColor;
    public static WorldElements worldElements;


    static void initModules() {
        PluginSystem.getLoadedPlugins().forEach(Plugin::onInitModules);


        //CLIENT
        bthackMainMenu = register(new BThackMainMenu());
        chatNotifications = register(new ChatNotifications());
        clickGui = register(new ClickGui());
        clientSettings = register(new ClientSettings());
        customCape = register(new CustomCape());
        if(DeviceSystem.getLaunchDevice() == DeviceSystem.LaunchDevice.PC) customFont = register(new CustomFont());
        discordRPC = register(new DiscordRPC());
        fpsReducer = register(new FPSReducer());
        HUD = register(new HUD());
        hudEditor = register(new HudEditor());
        macros = register(new Macros());
        memoryCleaner = register(new MemoryCleaner());
        menuShader = register(new MenuShader());

        //COMBAT
        aimBot = register(new AimBot());
        autoArmor = register(new AutoArmor());
        autoClicker = register(new AutoClicker());
        autoOffhand = register(new AutoOffhand());
        autoShield = register(new AutoShield());
        autoSoup = register(new AutoSoup());
        autoSword = register(new AutoSword());
        autoTotemFall = register(new AutoTotemFall());
        criticals = register(new Criticals());
        fastBow = register(new FastBow());
        fireBallAura = register(new FireBallAura());
        hitSound = register(new HitSound());
        holeFill = register(new HoleFill());
        igniteAura = register(new IgniteAura());
        killAura = register(new KillAura());
        //lavaAura = register(new LavaAura());
        noFriendDamage = register(new NoFriendDamage());
        pearlPhase = register(new PearlPhase());
        surround = register(new Surround());
        tntIgniter = register(new TNTIgniter());
        totemPopNotifier = register(new TotemPopNotifier());
        webAura = register(new WebAura());
        witherRoseAura = register(new WitherRoseAura());

        //MISC
        activeSpawnerDetect = register(new ActiveSpawnerDetect());
        antiHunger = register(new AntiHunger());
        auto2FA = register(new Auto2FA());
        autoAuth = register(new AutoAuth());
        breaker = register(new Breaker());
        cleanMemory = register(new CleanMemory());
        deathCamera = register(new DeathCamera());
        highwayBuilder = register(new HighwayBuilder());
        instaNuker = register(new InstaNuker());
        itemRandomizer = register(new ItemRandomizer());
        middleClick = register(new MiddleClick());
        moreChatHistory = register(new MoreChatHistory());
        noBreakDelay = register(new NoBreakDelay());
        noPacketKick = register(new NoPacketKick());
        noSoundLag = register(new NoSoundLag());
        openedGuiInfo = register(new OpenedGuiInfo());
        packetCanceller = register(new PacketCanceller());
        packetMine = register(new PacketMine());
        pistonSoundDelay = register(new PistonSoundDelay());
        portalGod = register(new PortalGod());
        scrapper = register(new Scrapper());
        superInstaMine = register(new SuperInstaMine());
        timer = register(new Timer());
        topperRadar = register(new TopperRadar());
        trashThrower = register(new TrashThrower());
        treeCutter = register(new TreeCutter());
        tridentDupe = register(new TridentDupe());
        visualRange = register(new VisualRange());

        //MOVEMENT
        antiAFK = register(new AntiAFK());
        autoJump = register(new AutoJump());
        autoWalk = register(new AutoWalk());
        blink = register(new Blink());
        cameraRotator = register(new CameraRotator());
        customFriction = register(new CustomFriction());
        elytraFastClose = register(new ElytraFastClose());
        elytraFlight = register(new ElytraFlight());
        elytraStrafe = register(new ElytraStrafe());
        entitySpeed = register(new EntitySpeed());
        extendedFirework = register(new ExtendedFirework());
        fastFall = register(new FastFall());
        flip = register(new Flip());
        guiMove = register(new GuiMove());
        impulse = register(new Impulse());
        keepSprint = register(new KeepSprint());
        levitationControl = register(new LevitationControl());
        longJump = register(new LongJump());
        ninjaBridge = register(new NinjaBridge());
        noFall = register(new NoFall());
        noJumpDelay = register(new NoJumpDelay());
        noPush = register(new NoPush());
        noRotate = register(new NoRotate());
        noSlow = register(new NoSlow());
        noSRotations = register(new NoSRotations());
        parkour = register(new Parkour());
        safeWalk = register(new SafeWalk());
        scaffold = register(new Scaffold());
        shiftSpam = register(new ShiftSpam());
        speed = register(new Speed());
        sprint = register(new Sprint());
        strafe = register(new Strafe());
        velocity = register(new Velocity());

        //PLAYER
        actionBot = register(new ActionBot());
        autoDisconnect = register(new AutoDisconnect());
        autoEat = register(new AutoEat());
        autoElytra = register(new AutoElytra());
        autoFirework = register(new AutoFirework());
        autoFish = register(new AutoFish());
        autoMend = register(new AutoMend());
        if (BThack.isBaritonePresent()) autoMine = register(new AutoMine());
        autoMount = register(new AutoMount());
        autoPearl = register(new AutoPearl());
        autoRespawn = register(new AutoRespawn());
        autoTool = register(new AutoTool());
        babyModel = register(new BabyModel());
        chestStealer = register(new ChestStealer());
        elytraReplace = register(new ElytraReplace());
        elytraSwap = register(new ElytraSwap());
        fakePlayer = register(new FakePlayer());
        fastDrop = register(new FastDrop());
        fastPlace = register(new FastPlace());
        fastUse = register(new FastUse());
        freeCam = register(new FreeCam());
        itemSaver = register(new ItemSaver());
        lagDetector = register(new LagDetector());
        multiFakePlayer = register(new MultiFakePlayer());
        noElytraBreak = register(new NoElytraBreak());
        noGlitchBlocks = register(new NoGlitchBlocks());
        noServerSlot = register(new NoServerSlot());
        packetPlace = register(new PacketPlace());
        pmSpammer = register(new PMSpammer());
        replanish = register(new Replanish());
        sneak = register(new Sneak());
        spammer = register(new Spammer());
        xCarry = register(new XCarry());

        //RENDER
        antiHazard = register(new AntiHazard());
        attackTrace = register(new AttackTrace());
        betterChat = register(new BetterChat());
        blockHighlight = register(new BlockHighlight());
        cameraClip = register(new CameraClip());
        chestESP = register(new ChestESP());
        csCrosshair = register(new CS_Crosshair());
        customBob = register(new CustomBob());
        enchantColor = register(new EnchantColor());
        ESP = register(new ESP());
        extraTab = register(new ExtraTab());
        fullBright = register(new FullBright());
        FXAA = register(new FXAA());
        handTweaks = register(new HandTweaks());
        holeESP = register(new HoleESP());
        lastOpenChest = register(new LastOpenChest());
        minecraftShaders = register(new MinecraftShaders());
        modifyCamera = register(new ModifyCamera());
        motionBlur = register(new MotionBlur());
        nametags = register(new Nametags());
        newChunks = register(new NewChunks());
        noFog = register(new NoFog());
        noOverlay = register(new NoOverlay());
        noRender = register(new NoRender());
        noSwing = register(new NoSwing());
        passwordHider = register(new PasswordHider());
        phaseESP = register(new PhaseESP());
        radar = register(new Radar());
        search = register(new Search());
        shaders = register(new Shaders());
        tooltips = register(new Tooltips());
        tracers = register(new Tracers());
        waypoints = register(new Waypoints());
        xray = register(new Xray());

        //WORLD
        autoFarm = register(new AutoFarm());
        autoFarmland = register(new AutoFarmland());
        autoShear = register(new AutoShear());
        autoSign = register(new AutoSign());
        cloudsColor = register(new CloudsColor());
        customDayTime = register(new CustomDayTime());
        fly = register(new Fly());
        fogColor = register(new FogColor());
        lawnmower = register(new Lawnmower());
        liquidFiller = register(new LiquidFiller());
        noWeather = register(new NoWeather());
        reach = register(new Reach());
        skyColor = register(new SkyColor());
        worldElements = register(new WorldElements());



        Client.modules.addAll(PluginUtils.getPluginsModules());

        initHudComponents();
    }

    private static void initHudComponents() {
        PluginSystem.getLoadedPlugins().forEach(Plugin::onInitHudComponents);

        Client.hudComponents.addAll(Arrays.asList(
                new WatermarkComponent(),
                new CompanionComponent(),
                new FPSComponent(),
                new CoordinatesComponent(),
                new RotationComponent(),
                new DirectionComponent(),
                new ServerIPComponent(),
                new SpeedComponent(),
                new PingComponent(),
                new TPSComponent(),
                new PlayerCountComponent(),
                new InventoryComponent(),
                new ArmorComponent(),
                new RealTimeComponent(),
                new MinecraftTimeComponent(),


                new DimensionComponent(),
                new DurabilityComponent(),
                new CrystalCountComponent(),
                new EXPCountComponent(),
                new GappleCountComponent(),
                new TotemCountComponent(),

                new TextRadarComponent(),



                new ArrayListComponent()
        ));


        Client.hudComponents.addAll(PluginUtils.getPluginsHudComponents());
        Client.modules.addAll(Client.hudComponents);

        try {
            ConfigSystem.loadHudComponents();
        } catch (IOException ignored) {}
    }

    private static <T extends Module> T register(T module) {
        Client.modules.add(module);
        return module;
    }
}
