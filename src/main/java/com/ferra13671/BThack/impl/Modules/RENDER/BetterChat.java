package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.SocialManagers;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClientSettings;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Arrays;

public class BetterChat extends Module {

    public final BooleanSetting time = new BooleanSetting("Time", this, true);
    public final ModeSetting timeFormat = new ModeSetting("Time Format", this, Arrays.asList("24", "12"));
    public final ModeSetting separate1 = new ModeSetting("Separate1", this, Arrays.asList("[]", "<>", "{}", "()", "None"));
    public final ModeSetting separate2 = new ModeSetting("Separate2", this, Arrays.asList("Space", ":", ";", "-", "*", "None"));

    public final BooleanSetting friends = new BooleanSetting("Friends", this, true);

    public final BooleanSetting enemies = new BooleanSetting("Enemies", this, true);

    public final BooleanSetting yourself = new BooleanSetting("Yourself", this, true);
    public final BooleanSetting yourselfSound = new BooleanSetting("Yourself Sound", this, true);

    /**
     * @Original_Code <a href="https://github.com/Ezzenix/ChatAnimation">github.com/Ezzenix/ChatAnimation</a>
     * or
     * <a href="https://modrinth.com/mod/chatanimation">modrinth.com/mod/chatanimation</a>
     *
     * @see com.ferra13671.BThack.mixins.gui_and_hud.chat.MixinChatHud
     * @see com.ferra13671.BThack.mixins.gui_and_hud.chat.MixinChatScreen
     */
    public final BooleanSetting chatAnimation = new BooleanSetting("Chat Animation", this, true);
    public final NumberSetting fadeTime = new NumberSetting("Fade Time", this, 170, 100, 300, true, chatAnimation::getValue);

    public BetterChat() {
        super("BetterChat",
                "lang.module.BetterChat",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        initSettings(
                time,
                timeFormat,
                separate1,
                separate2,

                friends,

                enemies,

                yourself,
                yourselfSound,

                chatAnimation,
                fadeTime
        );
    }


    @EventSubscriber
    public void onPacketReceive(PacketEvent.Receive e) {
        if (nullCheck()) return;
        if (e.getPacket() instanceof GameMessageS2CPacket) {
            GameMessageS2CPacket packet = (GameMessageS2CPacket) e.getPacket();
            boolean checked = false;

            if (yourself.getValue()) {
                if (packet.content().getString().contains(mc.player.getDisplayName().getString()) && !packet.content().getString().contains("<" + mc.player.getDisplayName().getString() + ">")) {
                    String text = Formatting.YELLOW + packet.content().getString();
                    packet = new GameMessageS2CPacket(Text.of(text), packet.overlay());
                    if (yourselfSound.getValue()) {
                        mc.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                    }
                    checked = true;
                }
            }

            if (friends.getValue()) {
                for (String string : SocialManagers.FRIENDS.getPlayers()) {
                    if (packet.content().getString().contains(string)) {
                        String text = packet.content().getString().replace(string, ClientSettings.getFriendColor() + string + (checked ? Formatting.YELLOW : Formatting.RESET));
                        packet = new GameMessageS2CPacket(Text.of(text), packet.overlay());
                    }
                }
            }
            if (enemies.getValue()) {
                for (String string : SocialManagers.ENEMIES.getPlayers()) {
                    if (packet.content().getString().contains(string)) {
                        String text = packet.content().getString().replace(string, ClientSettings.getEnemyColor() + string + (checked ? Formatting.YELLOW : Formatting.RESET));
                        packet = new GameMessageS2CPacket(Text.of(text), packet.overlay());
                    }
                }
            }

            if (time.getValue()) {
                if (packet.content().getString().startsWith("<")) {
                    String timeText = Formatting.GRAY + getSeparate1(0) + Formatting.WHITE + Client.getRealTime(timeFormat.getValue()) + Formatting.GRAY + getSeparate1(1) + getSeparate2() + Formatting.RESET;
                    packet = new GameMessageS2CPacket(Text.of(timeText + packet.content().getString()), packet.overlay());
                }
            }

            e.setPacket(packet);
        }
    }

    private String getSeparate1(int at) {
        if (separate1.getValue().equals("None")) return "";
        else return Character.toString(separate1.getValue().charAt(at));
    }

    private String getSeparate2() {
        return switch (separate2.getValue()) {
            case "Space" -> " ";
            case "None" -> "";
            default -> separate2.getValue();
        };
    }

    public static final ArrayList<Long> messageTimestamps = new ArrayList<>();
    public static int chatDisplacementY = 0;


    public void calculateYOffset(int lineHeight, int scrolledLines) {
        // Calculate current required offset to achieve slide in from bottom effect
        try {
            float fadeOffsetYScale = 0.8f; // scale * lineHeight
            float maxDisplacement = (float)lineHeight * fadeOffsetYScale;
            long timestamp = messageTimestamps.get(0);
            long timeAlive = System.currentTimeMillis() - timestamp;

            if (timeAlive < fadeTime.getValue() && scrolledLines == 0) {
                chatDisplacementY = (int)(maxDisplacement - ((timeAlive / fadeTime.getValue()) * maxDisplacement));
            }
        } catch (Exception ignored) {}
    }
}
