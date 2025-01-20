package com.ferra13671.BThack.api.Utils.DataList;

import com.ferra13671.BThack.Core.FileSystem.JsonUtils;
import com.ferra13671.BThack.api.Managers.managers.Command.Arguments;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.DataList.Commands.AbstractDataListCommand;
import com.ferra13671.BThack.api.Utils.DataList.Commands.EditDataListCommand;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.*;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.network.packet.s2c.common.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PacketList extends DataList<Class<? extends Packet>, String> { // Ignore this warning
    /**
     * I FUCK THIS!!!
     */
    public static final Map<String, Class<? extends Packet<?>>> PACKETS = new HashMap<>();
    public static final Map<String, Class<? extends Packet<?>>> CLIENT_PACKETS = new HashMap<>();
    public static final Map<String, Class<? extends Packet<?>>> SERVER_PACKETS = new HashMap<>();
    //After every change of mc version of BThack, you need to check and overwrite the packets list.
    static {
        //Client Packets
        //Common
        CLIENT_PACKETS.put("ClientOptionsC2SPacket", ClientOptionsC2SPacket.class);
        CLIENT_PACKETS.put("CommonPongC2SPacket", CommonPongC2SPacket.class);
        CLIENT_PACKETS.put("CookieResponseC2SPacket", CookieResponseC2SPacket.class);
        CLIENT_PACKETS.put("CustomPayloadC2SPacket", CustomPayloadC2SPacket.class);
        CLIENT_PACKETS.put("KeepAliveC2SPacket", KeepAliveC2SPacket.class);
        CLIENT_PACKETS.put("ResourcePackStatusC2SPacket", ResourcePackStatusC2SPacket.class);
        //Play
        CLIENT_PACKETS.put("AcknowledgeChunksC2SPacket", AcknowledgeChunksC2SPacket.class);
        CLIENT_PACKETS.put("AcknowledgeReconfigurationC2SPacket", AcknowledgeReconfigurationC2SPacket.class);
        CLIENT_PACKETS.put("AdvancementTabC2SPacket", AdvancementTabC2SPacket.class);
        CLIENT_PACKETS.put("BoatPaddleStateC2SPacket", BoatPaddleStateC2SPacket.class);
        CLIENT_PACKETS.put("BookUpdateC2SPacket", BookUpdateC2SPacket.class);
        CLIENT_PACKETS.put("ButtonClickC2SPacket", ButtonClickC2SPacket.class);
        CLIENT_PACKETS.put("ChatCommandSignedC2SPacket", ChatCommandSignedC2SPacket.class);
        CLIENT_PACKETS.put("ChatMessageC2SPacket", ChatMessageC2SPacket.class);
        CLIENT_PACKETS.put("ClickSlotC2SPacket", ClickSlotC2SPacket.class);
        CLIENT_PACKETS.put("ClientCommandC2SPacket", ClientCommandC2SPacket.class);
        CLIENT_PACKETS.put("ClientStatusC2SPacket", ClientStatusC2SPacket.class);
        CLIENT_PACKETS.put("CloseHandledScreenC2SPacket", CloseHandledScreenC2SPacket.class);
        CLIENT_PACKETS.put("CommandExecutionC2SPacket", CommandExecutionC2SPacket.class);
        CLIENT_PACKETS.put("CraftRequestC2SPacket", CraftRequestC2SPacket.class);
        CLIENT_PACKETS.put("CreativeInventoryActionC2SPacket", CreativeInventoryActionC2SPacket.class);
        CLIENT_PACKETS.put("DebugSampleSubscriptionC2SPacket", DebugSampleSubscriptionC2SPacket.class);
        CLIENT_PACKETS.put("HandSwingC2SPacket", HandSwingC2SPacket.class);
        CLIENT_PACKETS.put("JigsawGeneratingC2SPacket", JigsawGeneratingC2SPacket.class);
        CLIENT_PACKETS.put("MessageAcknowledgmentC2SPacket", MessageAcknowledgmentC2SPacket.class);
        CLIENT_PACKETS.put("PickFromInventoryC2SPacket", PickFromInventoryC2SPacket.class);
        CLIENT_PACKETS.put("PlayerActionC2SPacket", PlayerActionC2SPacket.class);
        CLIENT_PACKETS.put("PlayerInputC2SPacket", PlayerInputC2SPacket.class);
        CLIENT_PACKETS.put("PlayerInteractBlockC2SPacket", PlayerInteractBlockC2SPacket.class);
        CLIENT_PACKETS.put("PlayerInteractEntityC2SPacket", PlayerInteractEntityC2SPacket.class);
        CLIENT_PACKETS.put("PlayerInteractItemC2SPacket", PlayerInteractItemC2SPacket.class);
        CLIENT_PACKETS.put("PlayerMoveC2SPacket", PlayerMoveC2SPacket.class);
        CLIENT_PACKETS.put("PlayerSessionC2SPacket", PlayerSessionC2SPacket.class);
        CLIENT_PACKETS.put("QueryBlockNbtC2SPacket", QueryBlockNbtC2SPacket.class);
        CLIENT_PACKETS.put("QueryEntityNbtC2SPacket", QueryEntityNbtC2SPacket.class);
        CLIENT_PACKETS.put("RecipeBookDataC2SPacket", RecipeBookDataC2SPacket.class);
        CLIENT_PACKETS.put("RecipeCategoryOptionsC2SPacket", RecipeCategoryOptionsC2SPacket.class);
        CLIENT_PACKETS.put("RenameItemC2SPacket", RenameItemC2SPacket.class);
        CLIENT_PACKETS.put("RequestCommandCompletionsC2SPacket", RequestCommandCompletionsC2SPacket.class);
        CLIENT_PACKETS.put("SelectMerchantTradeC2SPacket", SelectMerchantTradeC2SPacket.class);
        CLIENT_PACKETS.put("SlotChangedStateC2SPacket", SlotChangedStateC2SPacket.class);
        CLIENT_PACKETS.put("SpectatorTeleportC2SPacket", SpectatorTeleportC2SPacket.class);
        CLIENT_PACKETS.put("TeleportConfirmC2SPacket", TeleportConfirmC2SPacket.class);
        CLIENT_PACKETS.put("UpdateBeaconC2SPacket", UpdateBeaconC2SPacket.class);
        CLIENT_PACKETS.put("UpdateCommandBlockC2SPacket", UpdateCommandBlockC2SPacket.class);
        CLIENT_PACKETS.put("UpdateCommandBlockMinecartC2SPacket", UpdateCommandBlockMinecartC2SPacket.class);
        CLIENT_PACKETS.put("UpdateDifficultyC2SPacket", UpdateDifficultyC2SPacket.class);
        CLIENT_PACKETS.put("UpdateDifficultyLockC2SPacket", UpdateDifficultyLockC2SPacket.class);
        CLIENT_PACKETS.put("UpdateJigsawC2SPacket", UpdateJigsawC2SPacket.class);
        CLIENT_PACKETS.put("UpdatePlayerAbilitiesC2SPacket", UpdatePlayerAbilitiesC2SPacket.class);
        CLIENT_PACKETS.put("UpdateSelectedSlotC2SPacket", UpdateSelectedSlotC2SPacket.class);
        CLIENT_PACKETS.put("UpdateSignC2SPacket", UpdateSignC2SPacket.class);
        CLIENT_PACKETS.put("UpdateStructureBlockC2SPacket", UpdateStructureBlockC2SPacket.class);
        CLIENT_PACKETS.put("VehicleMoveC2SPacket", VehicleMoveC2SPacket.class);


        //Server Packets
        //Common
        SERVER_PACKETS.put("CommonPingS2CPacket", CommonPingS2CPacket.class);
        SERVER_PACKETS.put("CookieRequestS2CPacket", CookieRequestS2CPacket.class);
        SERVER_PACKETS.put("CustomPayloadS2CPacket", CustomPayloadS2CPacket.class);
        SERVER_PACKETS.put("CustomReportDetailsS2CPacket", CustomReportDetailsS2CPacket.class);
        SERVER_PACKETS.put("DisconnectS2CPacket", DisconnectS2CPacket.class);
        SERVER_PACKETS.put("KeepAliveS2CPacket", KeepAliveS2CPacket.class);
        SERVER_PACKETS.put("ResourcePackRemoveS2CPacket", ResourcePackRemoveS2CPacket.class);
        SERVER_PACKETS.put("ResourcePackSendS2CPacket", ResourcePackSendS2CPacket.class);
        SERVER_PACKETS.put("ServerLinksS2CPacket", ServerLinksS2CPacket.class);
        SERVER_PACKETS.put("ServerTransferS2CPacket", ServerTransferS2CPacket.class);
        SERVER_PACKETS.put("StoreCookieS2CPacket", StoreCookieS2CPacket.class);
        SERVER_PACKETS.put("SynchronizeTagsS2CPacket", SynchronizeTagsS2CPacket.class);
        //Play
        SERVER_PACKETS.put("AdvancementUpdateS2CPacket", AdvancementUpdateS2CPacket.class);
        SERVER_PACKETS.put("BlockBreakingProgressS2CPacket", BlockBreakingProgressS2CPacket.class);
        SERVER_PACKETS.put("BlockEntityUpdateS2CPacket", BlockEntityUpdateS2CPacket.class);
        SERVER_PACKETS.put("BlockEventS2CPacket", BlockEventS2CPacket.class);
        SERVER_PACKETS.put("BlockUpdateS2CPacket", BlockUpdateS2CPacket.class);
        SERVER_PACKETS.put("BossBarS2CPacket", BossBarS2CPacket.class);
        SERVER_PACKETS.put("BundleDelimiterS2CPacket", BundleDelimiterS2CPacket.class);
        SERVER_PACKETS.put("BundleS2CPacket", BundleS2CPacket.class);
        SERVER_PACKETS.put("ChangeUnlockedRecipesS2CPacket", ChangeUnlockedRecipesS2CPacket.class);
        SERVER_PACKETS.put("ChatMessageS2CPacket", ChatMessageS2CPacket.class);
        SERVER_PACKETS.put("ChatSuggestionsS2CPacket", ChatSuggestionsS2CPacket.class);
        SERVER_PACKETS.put("ChunkBiomeDataS2CPacket", ChunkBiomeDataS2CPacket.class);
        SERVER_PACKETS.put("ChunkDataS2CPacket", ChunkDataS2CPacket.class);
        SERVER_PACKETS.put("ChunkDeltaUpdateS2CPacket", ChunkDeltaUpdateS2CPacket.class);
        SERVER_PACKETS.put("ChunkLoadDistanceS2CPacket", ChunkLoadDistanceS2CPacket.class);
        SERVER_PACKETS.put("ChunkRenderDistanceCenterS2CPacket", ChunkRenderDistanceCenterS2CPacket.class);
        SERVER_PACKETS.put("ChunkSentS2CPacket", ChunkSentS2CPacket.class);
        SERVER_PACKETS.put("ClearTitleS2CPacket", ClearTitleS2CPacket.class);
        SERVER_PACKETS.put("CloseScreenS2CPacket", CloseScreenS2CPacket.class);
        SERVER_PACKETS.put("CommandSuggestionsS2CPacket", CommandSuggestionsS2CPacket.class);
        SERVER_PACKETS.put("CommandTreeS2CPacket", CommandTreeS2CPacket.class);
        SERVER_PACKETS.put("CooldownUpdateS2CPacket", CooldownUpdateS2CPacket.class);
        SERVER_PACKETS.put("CraftFailedResponseS2CPacket", CraftFailedResponseS2CPacket.class);
        SERVER_PACKETS.put("DamageTiltS2CPacket", DamageTiltS2CPacket.class);
        SERVER_PACKETS.put("DeathMessageS2CPacket", DeathMessageS2CPacket.class);
        SERVER_PACKETS.put("DebugSampleS2CPacket", DebugSampleS2CPacket.class);
        SERVER_PACKETS.put("DifficultyS2CPacket", DifficultyS2CPacket.class);
        SERVER_PACKETS.put("EndCombatS2CPacket", EndCombatS2CPacket.class);
        SERVER_PACKETS.put("EnterCombatS2CPacket", EnterCombatS2CPacket.class);
        SERVER_PACKETS.put("EnterReconfigurationS2CPacket", EnterReconfigurationS2CPacket.class);
        SERVER_PACKETS.put("EntitiesDestroyS2CPacket", EntitiesDestroyS2CPacket.class);
        SERVER_PACKETS.put("EntityAnimationS2CPacket", EntityAnimationS2CPacket.class);
        SERVER_PACKETS.put("EntityAttachS2CPacket", EntityAttachS2CPacket.class);
        SERVER_PACKETS.put("EntityAttributesS2CPacket", EntityAttributesS2CPacket.class);
        SERVER_PACKETS.put("EntityDamageS2CPacket", EntityDamageS2CPacket.class);
        SERVER_PACKETS.put("EntityEquipmentUpdateS2CPacket", EntityEquipmentUpdateS2CPacket.class);
        SERVER_PACKETS.put("EntityPassengersSetS2CPacket", EntityPassengersSetS2CPacket.class);
        SERVER_PACKETS.put("EntityPositionS2CPacket", EntityPositionS2CPacket.class);
        SERVER_PACKETS.put("EntityS2CPacket", EntityS2CPacket.class);
        SERVER_PACKETS.put("EntitySetHeadYawS2CPacket", EntitySetHeadYawS2CPacket.class);
        SERVER_PACKETS.put("EntitySpawnS2CPacket", EntitySpawnS2CPacket.class);
        SERVER_PACKETS.put("EntityStatusEffectS2CPacket", EntityStatusEffectS2CPacket.class);
        SERVER_PACKETS.put("EntityStatusS2CPacket", EntityStatusS2CPacket.class);
        SERVER_PACKETS.put("EntityTrackerUpdateS2CPacket", EntityTrackerUpdateS2CPacket.class);
        SERVER_PACKETS.put("EntityVelocityUpdateS2CPacket", EntityVelocityUpdateS2CPacket.class);
        SERVER_PACKETS.put("ExperienceBarUpdateS2CPacket", ExperienceBarUpdateS2CPacket.class);
        SERVER_PACKETS.put("ExperienceOrbSpawnS2CPacket", ExperienceOrbSpawnS2CPacket.class);
        SERVER_PACKETS.put("ExplosionS2CPacket", ExplosionS2CPacket.class);
        SERVER_PACKETS.put("GameJoinS2CPacket", GameJoinS2CPacket.class);
        SERVER_PACKETS.put("GameMessageS2CPacket", GameMessageS2CPacket.class);
        SERVER_PACKETS.put("GameStateChangeS2CPacket", GameStateChangeS2CPacket.class);
        SERVER_PACKETS.put("HealthUpdateS2CPacket", HealthUpdateS2CPacket.class);
        SERVER_PACKETS.put("InventoryS2CPacket", InventoryS2CPacket.class);
        SERVER_PACKETS.put("ItemPickupAnimationS2CPacket", ItemPickupAnimationS2CPacket.class);
        SERVER_PACKETS.put("LightUpdateS2CPacket", LightUpdateS2CPacket.class);
        SERVER_PACKETS.put("LookAtS2CPacket", LookAtS2CPacket.class);
        SERVER_PACKETS.put("MapUpdateS2CPacket", MapUpdateS2CPacket.class);
        SERVER_PACKETS.put("NbtQueryResponseS2CPacket", NbtQueryResponseS2CPacket.class);
        SERVER_PACKETS.put("OpenHorseScreenS2CPacket", OpenHorseScreenS2CPacket.class);
        SERVER_PACKETS.put("OpenScreenS2CPacket", OpenScreenS2CPacket.class);
        SERVER_PACKETS.put("OpenWrittenBookS2CPacket", OpenWrittenBookS2CPacket.class);
        SERVER_PACKETS.put("OverlayMessageS2CPacket", OverlayMessageS2CPacket.class);
        SERVER_PACKETS.put("ParticleS2CPacket", ParticleS2CPacket.class);
        SERVER_PACKETS.put("PlayerAbilitiesS2CPacket", PlayerAbilitiesS2CPacket.class);
        SERVER_PACKETS.put("PlayerActionResponseS2CPacket", PlayerActionResponseS2CPacket.class);
        SERVER_PACKETS.put("PlayerListHeaderS2CPacket", PlayerListHeaderS2CPacket.class);
        SERVER_PACKETS.put("PlayerListS2CPacket", PlayerListS2CPacket.class);
        SERVER_PACKETS.put("PlayerPositionLookS2CPacket", PlayerPositionLookS2CPacket.class);
        SERVER_PACKETS.put("PlayerRemoveS2CPacket", PlayerRemoveS2CPacket.class);
        SERVER_PACKETS.put("PlayerRespawnS2CPacket", PlayerRespawnS2CPacket.class);
        SERVER_PACKETS.put("PlayerSpawnPositionS2CPacket", PlayerSpawnPositionS2CPacket.class);
        SERVER_PACKETS.put("PlaySoundFromEntityS2CPacket", PlaySoundFromEntityS2CPacket.class);
        SERVER_PACKETS.put("PlaySoundS2CPacket", PlaySoundS2CPacket.class);
        SERVER_PACKETS.put("ProfilelessChatMessageS2CPacket", ProfilelessChatMessageS2CPacket.class);
        SERVER_PACKETS.put("ProjectilePowerS2CPacket", ProjectilePowerS2CPacket.class);
        SERVER_PACKETS.put("RemoveEntityStatusEffectS2CPacket", RemoveEntityStatusEffectS2CPacket.class);
        SERVER_PACKETS.put("RemoveMessageS2CPacket", RemoveMessageS2CPacket.class);
        SERVER_PACKETS.put("ScoreboardDisplayS2CPacket", ScoreboardDisplayS2CPacket.class);
        SERVER_PACKETS.put("ScoreboardObjectiveUpdateS2CPacket", ScoreboardObjectiveUpdateS2CPacket.class);
        SERVER_PACKETS.put("ScoreboardScoreResetS2CPacket", ScoreboardScoreResetS2CPacket.class);
        SERVER_PACKETS.put("ScoreboardScoreUpdateS2CPacket", ScoreboardScoreUpdateS2CPacket.class);
        SERVER_PACKETS.put("ScreenHandlerPropertyUpdateS2CPacket", ScreenHandlerPropertyUpdateS2CPacket.class);
        SERVER_PACKETS.put("ScreenHandlerSlotUpdateS2CPacket", ScreenHandlerSlotUpdateS2CPacket.class);
        SERVER_PACKETS.put("SelectAdvancementTabS2CPacket", SelectAdvancementTabS2CPacket.class);
        SERVER_PACKETS.put("ServerMetadataS2CPacket", ServerMetadataS2CPacket.class);
        SERVER_PACKETS.put("SetCameraEntityS2CPacket", SetCameraEntityS2CPacket.class);
        SERVER_PACKETS.put("SetTradeOffersS2CPacket", SetTradeOffersS2CPacket.class);
        SERVER_PACKETS.put("SignEditorOpenS2CPacket", SignEditorOpenS2CPacket.class);
        SERVER_PACKETS.put("SimulationDistanceS2CPacket", SimulationDistanceS2CPacket.class);
        SERVER_PACKETS.put("StartChunkSendS2CPacket", StartChunkSendS2CPacket.class);
        SERVER_PACKETS.put("StatisticsS2CPacket", StatisticsS2CPacket.class);
        SERVER_PACKETS.put("StopSoundS2CPacket", StopSoundS2CPacket.class);
        SERVER_PACKETS.put("SubtitleS2CPacket", SubtitleS2CPacket.class);
        SERVER_PACKETS.put("SynchronizeRecipesS2CPacket", SynchronizeRecipesS2CPacket.class);
        SERVER_PACKETS.put("TeamS2CPacket", TeamS2CPacket.class);
        SERVER_PACKETS.put("TickStepS2CPacket", TickStepS2CPacket.class);
        SERVER_PACKETS.put("TitleFadeS2CPacket", TitleFadeS2CPacket.class);
        SERVER_PACKETS.put("TitleS2CPacket", TitleS2CPacket.class);
        SERVER_PACKETS.put("UnloadChunkS2CPacket", UnloadChunkS2CPacket.class);
        SERVER_PACKETS.put("UpdateSelectedSlotS2CPacket", UpdateSelectedSlotS2CPacket.class);
        SERVER_PACKETS.put("UpdateTickRateS2CPacket", UpdateTickRateS2CPacket.class);
        SERVER_PACKETS.put("VehicleMoveS2CPacket", VehicleMoveS2CPacket.class);
        SERVER_PACKETS.put("WorldBorderCenterChangedS2CPacket", WorldBorderCenterChangedS2CPacket.class);
        SERVER_PACKETS.put("WorldBorderInitializeS2CPacket", WorldBorderInitializeS2CPacket.class);
        SERVER_PACKETS.put("WorldBorderInterpolateSizeS2CPacket", WorldBorderInterpolateSizeS2CPacket.class);
        SERVER_PACKETS.put("WorldBorderSizeChangedS2CPacket", WorldBorderSizeChangedS2CPacket.class);
        SERVER_PACKETS.put("WorldBorderWarningBlocksChangedS2CPacket", WorldBorderWarningBlocksChangedS2CPacket.class);
        SERVER_PACKETS.put("WorldBorderWarningTimeChangedS2CPacket", WorldBorderWarningTimeChangedS2CPacket.class);
        SERVER_PACKETS.put("WorldEventS2CPacket", WorldEventS2CPacket.class);
        SERVER_PACKETS.put("WorldTimeUpdateS2CPacket", WorldTimeUpdateS2CPacket.class);

        PACKETS.putAll(CLIENT_PACKETS);
        PACKETS.putAll(SERVER_PACKETS);
    }

    public PacketList(String descName, String alias, String txtName) {
        super(txtName);
        initEditDataListCommand(new EditPacketListCommand("lang.command.PacketList.description", descName, alias, this));
        initAbstractDataListCommand(new AbstractDataListCommand("lang.command.PacketList.description", "lang.command.PacketList.message", descName, alias) {
            @Override
            public void sendAllList() {
                PacketList.this.sendAllList();
            }
        });
    }

    @Override
    protected void save(JsonObject jsonObject) {
        JsonArray jsonList = new JsonArray();
        valueNames.forEach(value -> jsonList.add(new JsonPrimitive(value)));
        jsonObject.add("values", jsonList);
    }

    @Override
    protected void load(JsonObject jsonObject) {
        if (!JsonUtils._null(jsonObject, "values")) {
            JsonArray jsonList = jsonObject.get("values").getAsJsonArray();
            jsonList.asList().forEach(jsonElement -> {
                String value = jsonElement.getAsString();
                if (PACKETS.containsKey(value)) {
                    values.add(PACKETS.get(value));
                    valueNames.add(value);
                }
            });
        }
    }

    @Override
    public void addToList(String packet) {
        if (!valueNames.contains(packet)) {
            values.add(PACKETS.get(packet));
            valueNames.add(packet);
            try {
                saveInFile();
            } catch (IOException ignored) {}
            ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.List.packetAdded"));
        } else {
            ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.List.packetAlreadyAdded"));
        }
    }

    @Override
    public void removeFromList(String packet) {
        if (valueNames.contains(packet)) {
            values.remove(PACKETS.get(packet));
            valueNames.remove(packet);
            try {
                saveInFile();
            } catch (IOException ignored) {}
            ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.List.packetRemoved"));
        } else {
            ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.List.packetAlreadyRemoved"));
        }
    }

    @Override
    public void clearList() {
        values.clear();
        valueNames.clear();
        try {
            saveInFile();
        } catch (IOException ignored) {}
        ChatUtils.sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.List.listCleared"), editDataListCommand.descName));
    }

    @Override
    public void sendAllList() {
        for (String packetName : valueNames) {
            ChatUtils.sendMessage(packetName);
        }
    }

    public static class EditPacketListCommand extends EditDataListCommand<Class<? extends Packet>, String> {

        public EditPacketListCommand(String descriptionKey, String descName, String alias, DataList<Class<? extends Packet>, String> dataList) {
            super(descriptionKey, descName, alias, dataList);
        }

        @Override
        public void compile(LiteralArgumentBuilder<CommandSource> builder) {
            builder.then(literal("add").then(literal("client->server").then(arg("packet", Arguments.CLIENT_PACKET).executes(context -> {
                dataList.addToList(context.getArgument("packet", String.class));
                dataList.postAction();

                return SUCCESFUL;
            }))).then(literal("server->client").then(arg("packet", Arguments.SERVER_PACKET).executes(context -> {
                dataList.addToList(context.getArgument("packet", String.class));
                dataList.postAction();

                return SUCCESFUL;
            }))));
            builder.then(literal("remove").then(arg("packet", Arguments.PACKET_LIST_PACKET((PacketList) dataList)).executes(context -> {
                dataList.removeFromList(context.getArgument("packet", String.class));
                dataList.postAction();

                return SUCCESFUL;
            })));
            builder.then(literal("clear").executes(context -> {
                dataList.clearList();
                dataList.postAction();

                return SUCCESFUL;
            }));
        }
    }
}
