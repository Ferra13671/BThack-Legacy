# 1.21.1-1.6.3 Changes
## Modules added:
- Auto2FA
- TreeCutter
## Modules modified:
- TotemPopNotifier
- ClientSettings
- Nametags
## HudComponents modified:
- Companion
## Added Commands:
- $auto2FA --- Adds/removes a 2fa key for a specific nickname.
## Other changes:
- The screen system was heavily modified.
- Fixed a problem with launching on phones.
- BThackUpdater has been added, which can be used to install updates automatically.
- The wallpaper system has been removed.

# 1.21.1-1.6.2 Changes
### Modules added:
- CustomCape
- TridentDupe
### Modules modified:
- SuperInstaMine
- MenuShader
### Modules fixed:
- PacketMine(Fixed kicks from 2b2t.org.ru due to "bad packets [100]")
### Modules removed:
- BThackCape
### Added Commands:
- $customCape --- Sets a custom cape by default or by using a file or link.
- $openFolder --- Opens the BThack config folder.
### Other changes:
- Added rendering of snow in winter in ClickGui and HudEditor.

# 1.21.1-1.6.1 Changes
### Modules fixed:
- HighwayBuilder
### Modules modified:
- Surround
- AttackTrace
- CustomBob
- Tracers
### Modules removed:
- SafeTrap
- Caipirinha
### HudComponents added:
- Companion
### HudComponents modified:
- Inventory
### Other changes:
- Fixed an issue with interacting with the color setting whether it is open or not.
- Reduced client size by using a new version of GLTextureUtils.

# 1.21.1-1.6 Changes
### Added Modules:
- PhaseESP
- CustomFont
- Waypoints
- Macros
### Modules modified:
- ChestESP
- ESP
- Nametags
### Modules fixed:
- NewChunks(Fixed bug with render distance setting)
### Added Commands:
- $customFont --- Allows you to select a custom font from a folder or set the default font.
- $waypoint --- Adds, deletes, modifies a waypoint or shows a list of waypoints.
- $macro --- Adds, deletes, modifies a macro or shows a list of macros.
### Other changes:
- Code refactoring has been performed.

# 1.21.1-1.5 Changes
### Added Modules:
- AutoFarm
- AutoShear
- ClientSettings
- ActiveSpawnerDetect
- AutoMend
- CustomFriction
- PacketCanceller
### Modules modified:
- Scaffold(Increased stability and operability of ExtraWidth)
- PacketMine(Added InstaRebreak setting)
### Modules removed:
- Language(Was implemented in ClientSettings)
### Added Commands:
- $module --- Performs a specific action on a module.
- $pluginList --- Shows the entire list of plugins.
- $cordsCopy --- Copies the player's coordinates.
### Other changes:
- Command system has been completely rewritten.
- Fixed visual bugs when rendering the background in the ActionBot config setting when it is opened through the main menu.
- The ClickGui renderer has been slightly modified.

# 1.21.1-1.4-beta Changes
### Added Modules:
- DeathCamera
- AntiHunger
- PingSpoof
- Reach
### Modules modified:
- ChestStealer
- ActionBot
- KillAura(Added more detailed filtering of mobs)
- NoSlow(Added Grim mode for UseItems)
- Sprint(Strafe mode added)
- FreeCam
- Speed(Added 3b3t mode)
### Modules fixed:
- NoSRotations
- ESP(Fixed a bug with detecting animals as hostile mobs)
### Modules renamed:
- GrimRocket -> ExtendedFirework
### Modules removed:
- InventoryManager
### Modules returned:
- Replanish
- ItemSaver
### Added Commands:
- $chestStealer [clear] / [[add/remove] [item_name]] — Adds/removes a block or clears the ChestStealer list.
### Other changes:
- Enabling/disabling and customizing hud components has now been moved to the HudEditor menu.
- The category saving system has been rewritten.
- Removed the effect when opening ClickGui.
- Added a lot of animations to ClickGui.

# 1.20.4-1.3 Changes
### Added Modules:
- EnchantColor
- NoElytraBreak
- NoPush
- PearlPhase
- InstaNuker
- AutoMount
- AutoFarmland
- PasswordHider
- FastFall
- VisualRange
- ExtraTab
- BetterChat
- NewChunks
- LiquidFiller
- Parkour
- MotionBlur
- MinecraftShaders
- FXAA
- MenuShader
- NoBreakDelay
- NoSoundLag
- TotemPopNotifier
- NoPacketKick
- AutoWalk
- ToggleSound
- AutoAuth
- ElytraStrafe
- AutoSoup
- MoreChatHistory
- TrashThrower
- MiddleClick
- NoGlitchBlocks
- FastDrop
- AutoMine(Only if you have a baritone)
- Sneak
- AutoClicker
### Modules modified:
- PacketMine(Added AutoCity, increased stability)
- ElytraFlight(Added modes: Pitch40, Boost, Timer, Auto Glide, 1.12.2 Control. Increased stability of Firework and Bounce modes)
- HighwayBuilder
- FastUse
- ChestESP
- AutoFirework
- Nametags
- FullBright(Potion mode added)
- ClickGui
### Modules changed:
- KillAura(Completely rewritten Aura mode)
### Modules fixed:
- TopperRadar(It now detects toppers normally)
- Tooltips(Now displays the name of renamed items)
- Velocity(Grim mode now works)
### Modules removed:
- GammaColor(It's been moved to FullBright)
### Added Commands:
- $prefix/p [new_prefix] — Changes the current prefix to another prefix.
- $reloadSounds/rls — Reloads the sound manager.
- $hclip [ [x] [z] ] / [length] — Allows you to teleport horizontally.
- $vclip [y] — Allows you to teleport vertically.
- $entitiesNearby — Writes how many entities are near you.
- $config [save / load] [config_name] — Saves/loads the config.
- $resetModule [module_name] — Resets all module settings to default values.
- $disableAll — Disables all modules (except those in the CLIENT category).
- $clientGamemode [ [0/1/2/3] / [survival/creative/spectator/adventure] ] — Sets the client-side gamemode.
- $autoMine [clear] / [[add/remove] [block_name]] — Adds/removes a block or clears the AutoMine list.
- $trashThrower [clear] / [[add/remove] [item_name]] — Adds/removes an item or clears the TrashThrower list.
- $autoAuth [[add] [player_name] [password]] / [[remove] [player_name]] — Adds/removes a password for a specific nickname for AutoAuth.
### fixed Commands:
- $rotate [yaw{-30000 : 30000}] [pitch{-90 : 90}] — You can now enter fractional numbers.
### Other changes:
- Fixed overlapping of categories on each other when rendering.
- Added hybrid loading of configs to make it easier to transfer them between players or store them.
- ClickGui can now be opened in the main menu.
- It is now possible to resize ClickGui.
- Fixed some problems with Build and Destroy threads operation.
- My newer and better performing event system is now being used.
- Added shaders to the main menu(60 shaders to choose from).
- Added a system to check and notify you of a new release.
- Plugins can now create their own categories.
- Fixed some bugs.

# 1.20.4-1.2 Changes
### Added Modules:
- PacketPlace
- ChatNotifications
- GrimRocket
- AutoOffhand
- AutoTotemFall
- NinjaBridge
- LagDetector
- Nametags
- LevitationControl
- AutoSign
- SuperInstaMine
- Breaker
- AttackTrace
- Scrapper
- IgniteAura
- WitherRoseAura
- TNTIgniter
- WebAura
- AutoArmor
- TopperRadar
- BThackMainMenu
- AutoElytra
- NoFog
- InventoryManager(Replenish, ItemSaver)
### Modules modified:
- FireBallAura
- AutoDisconnect
- Scaffold
- FastPlace
- AutoJump
- NoRender
- ElytraSwap
### Modules fixed:
- KillAura(Changed operating principle)
- ActionBot(Most of the code has been rewritten. Added: BreakTask, PlaceTask)
- FreeCam(The player is now displayed normally)
- CustomDayTime(Fixed blinking between new time and normal time)
- NoSRotations(Partially)
### Modules removed:
- Introvert(Was moved as an optional setting to AutoDisconnect)
### Added Commands:
- $breaker [clear] / [[add/remove] [block_name]] — adds/removes a block or clears the Breaker list.
### Other changes:
- Fixed incompatibility with Future client.

# 1.20.4-1.1 Changes
### Added Modules:
- Surround
- HighwayBuilder (BETA)
- PacketMine(With Double Mine(BETA))
- HoleFill
- AutoFish
### Modules modified:
- ElytraFlight(Bounce mod is much more stable to work)
### Modules fixed:
- ChestESP(Fixed Range setting)
- Search(Fixed tracers always working regardless of whether they are enabled or not)
### Other changes:
- Work has been done on HUD optimization.
- Work has been done on optimizing events.
- ActionBot now saves the task sequence you created.

# 1.20.4-1.0 Changes
#### The first release BThack for version 1.20.4!



# /-------------------- 1.12.2 versions --------------------/

# v1.5.1 Changes
### Added Modules:
- ChestPutter(Yeah, the name is shit.)
- AutoOffhand
- AutoConcrete
- AntiConcrete
- ElytraBoost
- AutoTotemFall
### Modules modified:
- ChestESP(Now you can choose what you want to highlight)
- PMSpammer(Now players from the list are taken randomly instead of one by one)
- ClickGui(Now you can customize the effect of the appearance of the modules)
### Added HudComponents
- TextRadar
### HudComponents modified:
- Armor(Corrected text layout)
### Added Commands:
- $chestputter <add/remove> <item_name> — Adds/Removes an item to ChestPutter's list
### Other changes:
- Checkbox and Visible settings have been changed.
- Fixed 1 bug with HUD.
- Reducing the font size from 17 to 16.
- Fixed a problem with the text of module descriptions popping out.
- Fixed the problem of launching BThack c phones.

# v1.5.0.2 Changes
- Schematica mod integrated
- ClickGui has been slightly modified

# v1.5 Changes
### Added Modules:
- ElytraReplace
- PacketPlace
- ThunderHack
- LiquidInteract
- BlockHighlight
- CameraDistance
- ShulkerPreview
- Trajectories
- DiscordRPC
### Modules modified:
- NewElytraFlight(You can now take off in a tunnel 3 blocks high without crouch manipulation)
- Tracers(Can highlight objects)
- BlockReach(Renamed Reach. Changed the method of operation)
### Fixed Modules:
- Lawnmower
### Added HudComponents:
- Biome
- ServerIp
- RealTime
- Dimension
- Durability
- CrystalCount
- EXPCount
- GappleCount
- TotemCount
- PlayerModel
### Other changes:
- New thread manager to reduce code size.
- Added some effects.
- Changed textures. — Thanks to Leha59 for help.
- The positions of the hud components are now saved.

# v1.4.4.1 Changes
### Added Modules:
- ModuleOnOffSound
- PMSpammer
- Chams
- HoleESP
- NewElytraFlight
### Modules modified:
- XCarry(Fixed problems with closing chests and ender chests)
### Fixed Modules:
- LagDetector
### Added Commands:
- $clans <add> <clanName> < red/orange/yellow/green/blue/violet >  —  Adds a clan with the specified color name.
- $clans <add> <clanName> <red(0-255)> <green(0-255)> <blue(0-255)>  —  Adds a clan with a custom color value.
- $clans <remove> <clanName>  —  Deletes the clan.
- $clanMember <add / remove> <clanName> <memberName>  —  Adds/Removes a clan member.
- $clanStatus <clanName> <Friendly / Neutral / Enemy>  —  Sets the clan to 1 of 3 statuses: Friendly; Neutral; Hostile.
### Deleted Commands:
- $allies <add> <nickname> <clan name> <Red> <Green> <Blue>
- $allies <add> <nickname> <clan name> <red/orange/yellow/green/blue/violet>
- $allies <remove> <nickname>
### Other changes:
- The allies system has been completely rewritten to a more advanced version of the clan system. You can now set clan statuses (Friendly/Neutral/Hostile), as well as select the target clan(s) to attack.
- Now you can customize the value of the slider in more detail (To do this, you need to point at the slider and click on the mouse wheel, then enter your value through the keyboard and press Enter).
- Now ArrayList can display their various information in addition to the names of included modules.
- Fixed a bug that when the clan manager is enabled in some modules, players not added to any of the clans are no longer detected.

# v1.4.3 Changes
### Added Modules:
- FireBallAura
- ItemRandomizer
- ChildModel
- AutoShield
- SafeTrap
- Africa
- HudEditor
- BThackMainMenu
### Other changes:
- Completely rewritten HUD renderer, because of which it became possible to freely change the location of HUD components.(To do this, you need to open HudEditor).
Fixes for some moments.

# v1.4.2 Changes
### Added Modules:
- ElytraFlight(+ Config 35 b/s)
- AutoBuilder
- Scaffold(Unstable)
- Surround
- Caipirinha
- Introvert(Automatically exits if a player is detected nearby (does not work on friends by default))
- ActionBot(A bot that performs a pre-built chain of tasks) — Thanks to .leha59_ for the idea
- BThackCape(You get a BThack cape)
- CustomSkin
### Modules modified:
- ElytraTakeOf(When turned on, the module will just change the elytres if you are flying them, and immediately turn off(comes in handy if you are stuck on air with ElytraFlight))
### Other changes:
- The OTHER category has been renamed to MISC, delete the ClickGui config file or your minecraft will not start.

# v1.4.1 Changes
### Added Modules:
- GameCrasher
- ViewModel
- Step
- CS_Crosshair
- ElytraSwap — Thanks to Leha59 for the idea
- ElytraTakeOf — Thanks to Leha59 for the idea
- CloudsColor
- FogColor
- WorldElements
- LagDetector
- PistonSoundDelay
- ItemSaver
- TopperRadar (Warns you if a topper has been detected in your line of sight)
- MultyFakePlayer (You can create an infinite number of fake players)
### Fixed Modules:
- ChestStealer
- AutoPearl
- AutoFirework
- CustomDayTime
- InventoryMove(Renamed GuiMove)
- PortalGod
- Sprint
- Fly
- AutoTool
- FullBright
- FastBow
- FastUse
### Modules modified:
- Jesus(Removed Matrix mode and added Solid mode)
- LargeNicknames(Completely rewritten(very small performance degradation possible))
- HUD(Added watermark display modes: logo or text)
### New commands:
- $friendlist — sends a list of friends to the chat room.                        
- $enemylist — sends a list of enemies to chat.                                    
- $allylist — sends a list of allies to chat.                                               
- $xraylist — sends to chat a list of blocks added to xray.                     
- $commandlist — sends a list of all commands to the chat room.

**Thanks to DoggLilY for the idea**
### Other changes:
- The work of the config system has been changed. Old configs will not work on this version. Delete the folder "Modules" from the BThack folder (Located in the folder of the build)
- Now in each module there is an option to turn off its visibility (will not be displayed in ArrayList)
- Completely rewritten the command system to a faster one.
- The system of module settings has been changed. Now some settings can be hidden depending on other settings.
- Added support for playing music in the main menu.
- Added its own button system.

# v1.3 Changes
#### The first public BThack release!