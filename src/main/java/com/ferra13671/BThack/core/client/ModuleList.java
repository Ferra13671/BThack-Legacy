package com.ferra13671.BThack.core.client;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.core.DeviceSystem;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.plugin.Plugin;
import com.ferra13671.BThack.api.plugin.PluginSystem;
import com.ferra13671.BThack.api.plugin.PluginUtils;
import com.ferra13671.BThack.impl.hud.*;
import com.ferra13671.BThack.impl.hud.onetext.*;
import com.ferra13671.BThack.impl.modules.client.*;
import com.ferra13671.BThack.impl.modules.combat.*;
import com.ferra13671.BThack.impl.modules.combat.KillAura;
import com.ferra13671.BThack.impl.modules.misc.*;
import com.ferra13671.BThack.impl.modules.misc.packetmine.PacketMine;
import com.ferra13671.BThack.impl.modules.movement.*;
import com.ferra13671.BThack.impl.modules.player.*;
import com.ferra13671.BThack.impl.modules.player.ActionBot.ActionBot;
import com.ferra13671.BThack.impl.modules.player.Spammer.Spammer;
import com.ferra13671.BThack.impl.modules.render.*;
import com.ferra13671.BThack.impl.modules.render.HoleESP;

import java.util.Arrays;

@SuppressWarnings("unused")
public final class ModuleList {
    //CLIENT
    public static BThackMainMenu bthackMainMenu = register(new BThackMainMenu());
    public static ChatNotifications chatNotifications = register(new ChatNotifications());
    public static ClickGui clickGui = register(new ClickGui());
    public static ClientSettings clientSettings = register(new ClientSettings());
    public static CustomCape customCape = register(new CustomCape());
    public static CustomFont customFont = (DeviceSystem.getLaunchDevice() == DeviceSystem.LaunchDevice.PC) ? register(new CustomFont()) : null;
    public static DiscordRPC discordRPC = (DeviceSystem.getLaunchDevice() == DeviceSystem.LaunchDevice.PC) ? register(new DiscordRPC()) : null;
    public static HUD HUD = register(new HUD());
    public static HudEditor hudEditor = register(new HudEditor());
    public static Macros macros = register(new Macros());
    public static MenuShader menuShader = register(new MenuShader());

    //COMBAT
    public static AimBot aimBot = register(new AimBot());
    public static AutoArmor autoArmor = register(new AutoArmor());
    public static AutoClicker autoClicker = register(new AutoClicker());
    public static AutoOffhand autoOffhand = register(new AutoOffhand());
    public static AutoShield autoShield = register(new AutoShield());
    public static AutoSoup autoSoup = register(new AutoSoup());
    public static AutoSword autoSword = register(new AutoSword());
    public static AutoTotemFall autoTotemFall = register(new AutoTotemFall());
    public static Criticals criticals = register(new Criticals());
    public static FastBow fastBow = register(new FastBow());
    public static FireBallAura fireBallAura = register(new FireBallAura());
    public static HitSound hitSound = register(new HitSound());
    public static HoleFill holeFill = register(new HoleFill());
    public static IgniteAura igniteAura = register(new IgniteAura());
    public static KillAura killAura = register(new KillAura());
    public static NoFriendDamage noFriendDamage = register(new NoFriendDamage());
    public static PearlPhase pearlPhase = register(new PearlPhase());
    public static Surround surround = register(new Surround());
    public static TNTIgniter tntIgniter = register(new TNTIgniter());
    public static TotemPopNotifier totemPopNotifier = register(new TotemPopNotifier());
    public static WebAura webAura = register(new WebAura());
    public static WitherRoseAura witherRoseAura = register(new WitherRoseAura());

    //MISC
    public static ActiveSpawnerDetect activeSpawnerDetect = register(new ActiveSpawnerDetect());
    public static AntiHunger antiHunger = register(new AntiHunger());
    public static Auto2FA auto2FA = register(new Auto2FA());
    public static AutoAnvilEnchant autoAnvilEnchant = register(new AutoAnvilEnchant());
    public static AutoAuth autoAuth = register(new AutoAuth());
    public static AutoFarm autoFarm = register(new AutoFarm());
    public static AutoFarmland autoFarmland = register(new AutoFarmland());
    public static AutoShear autoShear = register(new AutoShear());
    public static AutoSign autoSign = register(new AutoSign());
    public static Breaker breaker = register(new Breaker());
    public static CleanMemory cleanMemory = register(new CleanMemory());
    public static DeathCamera deathCamera = register(new DeathCamera());
    public static Fly fly = register(new Fly());
    public static HighwayBuilder highwayBuilder = register(new HighwayBuilder());
    public static InstaNuker instaNuker = register(new InstaNuker());
    public static ItemRandomizer itemRandomizer = register(new ItemRandomizer());
    public static Lawnmower lawnmower = register(new Lawnmower());
    public static LiquidFiller liquidFiller = register(new LiquidFiller());
    public static MiddleClick middleClick = register(new MiddleClick());
    public static MoreChatHistory moreChatHistory = register(new MoreChatHistory());
    public static NoBreakDelay noBreakDelay = register(new NoBreakDelay());
    public static NoPacketKick noPacketKick = register(new NoPacketKick());
    public static NoSoundLag noSoundLag = register(new NoSoundLag());
    public static OpenedGuiInfo openedGuiInfo = register(new OpenedGuiInfo());
    public static PacketCanceller packetCanceller = register(new PacketCanceller());
    public static PacketMine packetMine = register(new PacketMine());
    public static PistonSoundDelay pistonSoundDelay = register(new PistonSoundDelay());
    public static PortalGod portalGod = register(new PortalGod());
    public static Reach reach = register(new Reach());
    public static Scrapper scrapper = register(new Scrapper());
    public static SuperInstaMine superInstaMine = register(new SuperInstaMine());
    public static Timer timer = register(new Timer());
    public static TrashThrower trashThrower = register(new TrashThrower());
    public static TreeCutter treeCutter = register(new TreeCutter());
    public static TridentDupe tridentDupe = register(new TridentDupe());
    public static VisualRange visualRange = register(new VisualRange());

    //MOVEMENT
    public static AutoJump autoJump = register(new AutoJump());
    public static AutoWalk autoWalk = register(new AutoWalk());
    public static Blink blink = register(new Blink());
    public static CameraRotator cameraRotator = register(new CameraRotator());
    public static CustomFriction customFriction = register(new CustomFriction());
    public static ElytraFastClose elytraFastClose = register(new ElytraFastClose());
    public static ElytraFlight elytraFlight = register(new ElytraFlight());
    public static ElytraStrafe elytraStrafe = register(new ElytraStrafe());
    public static EntitySpeed entitySpeed = register(new EntitySpeed());
    public static ExtendedFirework extendedFirework = register(new ExtendedFirework());
    public static FastFall fastFall = register(new FastFall());
    public static Flip flip = register(new Flip());
    public static GuiMove guiMove = register(new GuiMove());
    public static Impulse impulse = register(new Impulse());
    public static KeepSprint keepSprint = register(new KeepSprint());
    public static LevitationControl levitationControl = register(new LevitationControl());
    public static LongJump longJump = register(new LongJump());
    public static NoFall noFall = register(new NoFall());
    public static NoJumpDelay noJumpDelay = register(new NoJumpDelay());
    public static NoPush noPush = register(new NoPush());
    public static NoRotate noRotate = register(new NoRotate());
    public static NoSlow noSlow = register(new NoSlow());
    public static NoSRotations noSRotations = register(new NoSRotations());
    public static Parkour parkour = register(new Parkour());
    public static SafeWalk safeWalk = register(new SafeWalk());
    public static Scaffold scaffold = register(new Scaffold());
    public static ShiftSpam shiftSpam = register(new ShiftSpam());
    public static Speed speed = register(new Speed());
    public static Sprint sprint = register(new Sprint());
    public static Strafe strafe = register(new Strafe());
    public static Velocity velocity = register(new Velocity());

    //PLAYER
    public static ActionBot actionBot = register(new ActionBot());
    public static AutoDisconnect autoDisconnect = register(new AutoDisconnect());
    public static AutoEat autoEat = register(new AutoEat());
    public static AutoElytra autoElytra = register(new AutoElytra());
    public static AutoFirework autoFirework = register(new AutoFirework());
    public static AutoFish autoFish = register(new AutoFish());
    public static AutoMend autoMend = register(new AutoMend());
    public static AutoMine autoMine = BThack.isBaritonePresent() ? register(new AutoMine()) : null;
    public static AutoMount autoMount = register(new AutoMount());
    public static AutoPearl autoPearl = register(new AutoPearl());
    public static AutoRespawn autoRespawn = register(new AutoRespawn());
    public static AutoTool autoTool = register(new AutoTool());
    public static BabyModel babyModel = register(new BabyModel());
    public static ChestStealer chestStealer = register(new ChestStealer());
    public static ElytraReplace elytraReplace = register(new ElytraReplace());
    public static ElytraSwap elytraSwap = register(new ElytraSwap());
    public static FakePlayer fakePlayer = register(new FakePlayer());
    public static FastDrop fastDrop = register(new FastDrop());
    public static FastPlace fastPlace = register(new FastPlace());
    public static FastUse fastUse = register(new FastUse());
    public static FreeCam freeCam = register(new FreeCam());
    public static ItemSaver itemSaver = register(new ItemSaver());
    public static LagDetector lagDetector = register(new LagDetector());
    public static MultiFakePlayer multiFakePlayer = register(new MultiFakePlayer());
    public static NoElytraBreak noElytraBreak = register(new NoElytraBreak());
    public static NoGlitchBlocks noGlitchBlocks = register(new NoGlitchBlocks());
    public static NoServerSlot noServerSlot = register(new NoServerSlot());
    public static PacketPlace packetPlace = register(new PacketPlace());
    public static PMSpammer pmSpammer = register(new PMSpammer());
    public static Replanish replanish = register(new Replanish());
    public static Sneak sneak = register(new Sneak());
    public static Spammer spammer = register(new Spammer());
    public static XCarry xCarry = register(new XCarry());

    //RENDER
    public static Ambience ambience = register(new Ambience());
    public static AntiHazard antiHazard = register(new AntiHazard());
    public static AttackTrace attackTrace = register(new AttackTrace());
    public static BetterChat betterChat = register(new BetterChat());
    public static BlockHighlight blockHighlight = register(new BlockHighlight());
    public static CameraClip cameraClip = register(new CameraClip());
    public static ChestESP chestESP = register(new ChestESP());
    public static CSCrosshair csCrosshair = register(new CSCrosshair());
    public static CustomBob customBob = register(new CustomBob());
    public static EnchantColor enchantColor = register(new EnchantColor());
    public static ESP ESP = register(new ESP());
    public static ExtraTab extraTab = register(new ExtraTab());
    public static FullBright fullBright = register(new FullBright());
    public static HandTweaks handTweaks = register(new HandTweaks());
    public static HoleESP holeESP = register(new HoleESP());
    public static LastOpenChest lastOpenChest = register(new LastOpenChest());
    public static ModifyCamera modifyCamera = register(new ModifyCamera());
    public static Nametags nametags = register(new Nametags());
    public static NewChunks newChunks = register(new NewChunks());
    public static NoFog noFog = register(new NoFog());
    public static NoOverlay noOverlay = register(new NoOverlay());
    public static NoRender noRender = register(new NoRender());
    public static NoSwing noSwing = register(new NoSwing());
    public static NoWeather noWeather = register(new NoWeather());
    public static PasswordHider passwordHider = register(new PasswordHider());
    public static PhaseESP phaseESP = register(new PhaseESP());
    public static Radar radar = register(new Radar());
    public static Search search = register(new Search());
    public static Shaders shaders = register(new Shaders());
    public static Tooltips tooltips = register(new Tooltips());
    public static Tracers tracers = register(new Tracers());
    public static Waypoints waypoints = register(new Waypoints());
    public static Xray xray = register(new Xray());
    public static Zoom zoom = register(new Zoom());

    static void initModules() {
        PluginSystem.getLoadedPlugins().forEach(Plugin::onInitModules);

        Client.modules.addAll(PluginUtils.getPluginsModules());

        initHudComponents();

        Client.modules.forEach(Module::initSettings);
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
                new KeyBindsComponent(),
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
    }

    private static <T extends Module> T register(T module) {
        Client.modules.add(module);
        return module;
    }
}
