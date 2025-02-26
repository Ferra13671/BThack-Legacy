package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils;
import com.ferra13671.BThack.api.Events.Render.RenderHudPreEvent;
import com.ferra13671.BThack.api.Module.HudComponent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.managers.Waypoint.Waypoint;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Waypoints extends Module {

    public final NumberSetting scale = new NumberSetting("Scale", this, 1, 1, 2, false);
    public final BooleanSetting convertNether = new BooleanSetting("Convert Nether", this, true);
    public final BooleanSetting convertOverworld = new BooleanSetting("Convert Overworld", this, true);

    public Waypoints() {
        super("Waypoints",
                "lang.module.Waypoints",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        initSettings(
                scale,
                convertNether,
                convertOverworld
        );
    }

    @EventSubscriber
    public void onHudRender(RenderHudPreEvent e) {
        if (nullCheck()) return;

        String currentServer = mc.isInSingleplayer() ? "SinglePlayer" : mc.getNetworkHandler().getServerInfo().address;

        BThackRender.guiGraphics.getMatrices().push();
        if (mc.world.getRegistryKey() == World.OVERWORLD || (convertOverworld.getValue() && mc.world.getRegistryKey() == World.NETHER)) {
            Managers.WAYPOINT_MANAGER.getOverworldWaypoints().forEach(waypoint -> {
                if (!waypoint.isVisible()) return;
                if (!waypoint.getServer().equals(currentServer)) return;
                Vec3d position = new Vec3d(waypoint.getPosition().getX(), waypoint.getPosition().getY(), waypoint.getPosition().getZ());

                if (convertOverworld.getValue() && mc.world.getRegistryKey() == World.NETHER) {
                    position.x /= 8;
                    position.z /= 8;
                }

                drawWaypoint(position, waypoint);
            });
        }
        if (mc.world.getRegistryKey() == World.END) {
            Managers.WAYPOINT_MANAGER.getEndWaypoints().forEach(waypoint -> {
                if (!waypoint.isVisible()) return;
                if (!waypoint.getServer().equals(currentServer)) return;
                drawWaypoint(waypoint.getPosition(), waypoint);
            });
        }
        if (mc.world.getRegistryKey() == World.NETHER || (convertNether.getValue() && mc.world.getRegistryKey() == World.OVERWORLD)) {
            Managers.WAYPOINT_MANAGER.getNetherWaypoints().forEach(waypoint -> {
                if (!waypoint.isVisible()) return;
                if (!waypoint.getServer().equals(currentServer)) return;
                Vec3d position = new Vec3d(waypoint.getPosition().getX(), waypoint.getPosition().getY(), waypoint.getPosition().getZ());
                if (convertNether.getValue() && mc.world.getRegistryKey() == World.OVERWORLD) {
                    position.x *= 8;
                    position.z *= 8;
                }

                drawWaypoint(position, waypoint);
            });
        }
        BThackRender.guiGraphics.getMatrices().pop();
    }

    private void drawWaypoint(Vec3d position, Waypoint waypoint) {
        BThackRender.guiGraphics.getMatrices().translate(0, 0, 1);
        float[] pos = BThackRenderUtils.worldPosToScreenXY(position, false);
        if (pos == null) return;

        BThackRender.drawRect(pos[0] - 6, pos[1] - 6, pos[0] + 6, pos[1] + 6, waypoint.getColor());
        BThackRender.drawCenteredString(Character.toString(waypoint.getName().charAt(0)), pos[0], pos[1] - (FontUtils.getTextHeight(Character.toString(waypoint.getName().charAt(0)), FontRenderManager.DrawMode.NORMAL_BOLD) / 2), -1, FontRenderManager.DrawMode.NORMAL_BOLD);
        pos[1] = pos[1] + 6 + (FontUtils.getTextHeight(waypoint.getName()) / 2);
        BThackRender.drawCenteredString(waypoint.getName(), pos[0], pos[1], -1);
        String text = "(" + HudComponent.decimal.format(MathUtils.getDistance(mc.player.getPos(), position)) + "m.)";
        BThackRender.drawCenteredString(text, pos[0], pos[1] + 6 + (FontUtils.getTextHeight(text, FontRenderManager.DrawMode.SMALL) / 2), -1, FontRenderManager.DrawMode.SMALL);
    }
}
