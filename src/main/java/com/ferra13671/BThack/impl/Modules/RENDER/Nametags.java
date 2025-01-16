package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Events.Render.RenderHudPreEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.SocialManagers;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClientSettings;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector4d;

import java.util.Arrays;

public class Nametags extends Module {

    public final BooleanSetting players = new BooleanSetting("Players", this, true);
    public final ModeSetting playerMode = new ModeSetting("PMode", this, Arrays.asList("Mini", "Normal", "Full"));

    public final BooleanSetting items = new BooleanSetting("Items", this, true);

    public Nametags() {
        super("Nametags",
                "lang.module.Nametags",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        initSettings(
                players,
                playerMode,
                items
        );
    }

    @EventSubscriber
    public void onRenderHud(RenderHudPreEvent e) {
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof PlayerEntity player && players.getValue() && player != mc.player) {
                renderPlayerNametag(player);
                continue;
            }
            if (entity instanceof ItemEntity itemEntity && items.getValue()) {
                renderItemNametag(itemEntity);
            }
        }
    }

    public void renderItemNametag(ItemEntity itemEntity) {
        float[] cords = getNametagPos(itemEntity, 0);
        if (cords == null) return;

        BThackRender.drawItem(BThackRender.guiGraphics, itemEntity.getStack(), (int) cords[0] - 8, (int) cords[1] - 18, null, true);
        BThackRender.drawCenteredString(itemEntity.getName().getString(), cords[0], cords[1], -1, 0.8f);
    }

    public void renderPlayerNametag(PlayerEntity player) {
        float[] cords = getNametagPos(player, player.getHeight() + 0.3f);
        if (cords == null) return;

        BThackRender.guiGraphics.getMatrices().push();
        BThackRender.guiGraphics.getMatrices().translate(1,1,600);

        switch (playerMode.getValue()) {
            case "Mini" -> renderMiniPlayerNametag(cords, player);
            case "Normal" -> renderNormalPlayerNametag(cords, player);
            case "Full" -> renderFullPlayerNametag(cords, player);
        }

        BThackRender.guiGraphics.getMatrices().pop();
    }

    public void renderMiniPlayerNametag(float[] cords, PlayerEntity player) {
        float leftX = cords[0] - 60;
        float upY = cords[1] - 16;
        float rightX = cords[0] + 60;
        float downY = cords[1];

        drawBase(leftX, upY, rightX, downY);
        float hp = player.getHealth();
        drawName((SocialManagers.FRIENDS.contains(player) ? ClientSettings.getFriendColor() : (SocialManagers.ENEMIES.contains(player) ? ClientSettings.getEnemyColor() : "")) + player.getDisplayName().getString() + " " + (hp > 15 ? Formatting.GREEN : (hp > 8 ? Formatting.YELLOW : Formatting.RED)) + hp, leftX, downY);
    }

    public void renderNormalPlayerNametag(float[] cords, PlayerEntity player) {
        float leftX = cords[0] - 60;
        float upY = cords[1] - 45;
        float rightX = cords[0] + 60;
        float downY = cords[1];

        drawBase(leftX, upY, rightX, downY);
        drawName((SocialManagers.FRIENDS.contains(player) ? ClientSettings.getFriendColor() : (SocialManagers.ENEMIES.contains(player) ? ClientSettings.getEnemyColor() : "")) + player.getDisplayName().getString(), leftX, downY);
        drawHP(leftX + 5, downY - 22, player);
        drawArmor(leftX + 5, upY + 4, player);
    }

    public void renderFullPlayerNametag(float[] cords, PlayerEntity player) {
        float leftX = cords[0] - 100;
        float upY = cords[1] - 50;
        float rightX = cords[0] + 100;
        float downY = cords[1];


        drawBase(leftX, upY, rightX, downY);
        drawName(player.getDisplayName().getString(), leftX, downY);

        float moveF = leftX + 5;
        moveF = drawArmor(moveF, upY + 5, player);

        moveF += 10;


        drawHandsInfo(moveF, upY + 4, player);
        drawSocialInfo(leftX + 5, upY + 23, player);
        drawHP(rightX - 100, upY + 23, player);
    }

    public void renderMobNametag(LivingEntity livingEntity) {

    }

    public float[] getNametagPos(Entity entity, float yPlus) {
        double x = entity.prevX + (entity.getX() - entity.prevX) * mc.getRenderTickCounter().getTickDelta(true);
        double y = entity.prevY + (entity.getY() - entity.prevY) * mc.getRenderTickCounter().getTickDelta(true);
        double z = entity.prevZ + (entity.getZ() - entity.prevZ) * mc.getRenderTickCounter().getTickDelta(true);
        Vec3d vector = new Vec3d(x, y + yPlus, z);

        Vector4d position;

        vector = BThackRenderUtils.worldCordsToScreenCords(vector);

        if (vector.z > 0 && vector.z < 1) {
            position = new Vector4d(vector.x, vector.y, vector.z, 0);
            position.x = Math.min(vector.x, position.x);
            position.y = Math.min(vector.y, position.y);
            position.z = Math.max(vector.x, position.z);
        } else {
            return null;
        }

        //                      X                  Y
        return new float[]{(float) position.x, (float) position.y};
    }

    public void drawBase(float leftX, float upY, float rightX, float downY) {
        BThackRender.drawRect(leftX, upY, rightX, downY, ColorUtils.fastRGBA(0,0,0,150));
        BThackRender.drawOutlineRect(leftX, upY, rightX, downY, 1.3f, ColorUtils.rainbow(100));
    }

    public void drawName(String name, float leftX, float downY) {
        BThackRender.drawString(name, leftX + 5, downY - mc.textRenderer.fontHeight - 3, -1, false, 1.1f);
    }

    public float drawArmor(float startX, float startY, LivingEntity entity) {
        for (ItemStack stack : entity.getArmorItems()) {
            if (stack != null) {
                BThackRender.drawItem(BThackRender.guiGraphics, stack, (int) startX, (int) startY, null, true);
            }
            startX += 20;
        }
        return startX;
    }

    public void drawHP(float startX, float startY, LivingEntity entity) {
        BThackRender.drawString("HP: " + (int) entity.getHealth(), startX, startY, -1);

        float length = (((entity.getMaxHealth() - entity.getHealth()) / entity.getMaxHealth()) * 80);

        //float extraX = ((entity.getHealth() / entity.getMaxHealth()) * 80);
        BThackRender.drawHorizontalGradientRect(startX + 15, startY, startX + 80, startY + 7, ColorUtils.RED, ColorUtils.GREEN);
        BThackRender.drawRect(95 + startX - length, startY, startX + 95, startY + 7, ColorUtils.BLACK);
    }

    public void drawSocialInfo(float startX, float startY, PlayerEntity player) {
        String socialText = (SocialManagers.FRIENDS.contains(player) ? ClientSettings.getFriendColor() + "Friend" : "") + Formatting.RESET + (SocialManagers.ENEMIES.contains(player) ? ClientSettings.getEnemyColor() + (SocialManagers.FRIENDS.contains(player) ? "   " : "") + "Enemy" : "");
        BThackRender.drawString(socialText, startX, startY, -1, false);
    }

    public void drawHandsInfo(float startX, float startY, PlayerEntity player) {
        BThackRender.drawString("L: ", startX, startY + 5, -1, false);
        startX += 12;
        if (player.getOffHandStack() != null) {
            BThackRender.drawItem(BThackRender.guiGraphics, player.getOffHandStack(), (int) startX, (int) startY, null, true);
        }

        startX += 30;

        BThackRender.drawString("R: ", startX, startY + 5, -1, false);
        startX += 12;
        if (player.getMainHandStack() != null) {
            BThackRender.drawItem(BThackRender.guiGraphics, player.getMainHandStack(), (int) startX, (int) startY, null, false);
        }
    }
}
