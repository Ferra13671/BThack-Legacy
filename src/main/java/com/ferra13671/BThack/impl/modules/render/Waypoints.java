package com.ferra13671.BThack.impl.modules.render;

import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.core.render.BThackMatrix;
import com.ferra13671.BThack.core.render.BThackRender;
import com.ferra13671.BThack.core.render.font.FontRenderManager;
import com.ferra13671.BThack.core.render.font.FontUtils;
import com.ferra13671.BThack.core.render.utils.BThackRenderUtils;
import com.ferra13671.BThack.events.render.RenderHudPreEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.NumberSetting;
import com.ferra13671.BThack.managers.impl.waypoint.Waypoint;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.utils.MathUtils;
import com.ferra13671.BThack.api.utils.PlayerUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(name = "Waypoints", description = "lang.module.Waypoints", category = "RENDER")
public class Waypoints extends Module {

    public final NumberSetting scale = new NumberSetting("Scale", this, 1, 1, 2, false);
    public final BooleanSetting convertNether = new BooleanSetting("Convert Nether", this, true);
    public final BooleanSetting convertOverworld = new BooleanSetting("Convert Overworld", this, true);


    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onHudRender(RenderHudPreEvent e) {
        if (nullCheck()) return;

        String currentServer = mc.isInSingleplayer() ? "SinglePlayer" : mc.getNetworkHandler().getServerInfo().address;

        BThackMatrix.push();
        if (PlayerUtils.isInOverworld() || (convertOverworld.getValue() && PlayerUtils.isInNether())) {
            Managers.WAYPOINT_MANAGER.getOverworldWaypoints().forEach(waypoint -> {
                if (!waypoint.isVisible()) return;
                if (!waypoint.getServer().equals(currentServer)) return;
                Vec3d position = new Vec3d(waypoint.getPosition().getX(), waypoint.getPosition().getY(), waypoint.getPosition().getZ());

                if (convertOverworld.getValue() && PlayerUtils.isInNether()) {
                    position.x /= 8;
                    position.z /= 8;
                }

                drawWaypoint(position, waypoint);
            });
        }
        if (PlayerUtils.isInEnd()) {
            Managers.WAYPOINT_MANAGER.getEndWaypoints().forEach(waypoint -> {
                if (!waypoint.isVisible()) return;
                if (!waypoint.getServer().equals(currentServer)) return;
                drawWaypoint(waypoint.getPosition(), waypoint);
            });
        }
        if (PlayerUtils.isInNether() || (convertNether.getValue() && PlayerUtils.isInOverworld())) {
            Managers.WAYPOINT_MANAGER.getNetherWaypoints().forEach(waypoint -> {
                if (!waypoint.isVisible()) return;
                if (!waypoint.getServer().equals(currentServer)) return;
                Vec3d position = new Vec3d(waypoint.getPosition().getX(), waypoint.getPosition().getY(), waypoint.getPosition().getZ());
                if (convertNether.getValue() && PlayerUtils.isInOverworld()) {
                    position.x *= 8;
                    position.z *= 8;
                }

                drawWaypoint(position, waypoint);
            });
        }
        BThackMatrix.pop();
    }

    @SuppressWarnings("DataFlowIssue")
    private void drawWaypoint(Vec3d position, Waypoint waypoint) {
        BThackMatrix.translate(0, 0, 1);
        float[] pos = BThackRenderUtils.worldPosToScreenXY(position, false);
        if (pos == null) return;

        BThackRender.drawRect(pos[0] - 6, pos[1] - 6, pos[0] + 6, pos[1] + 6, waypoint.getColor());
        BThackRender.drawCenteredString(Character.toString(waypoint.getName().charAt(0)), pos[0], pos[1] - (FontUtils.getTextHeight(Character.toString(waypoint.getName().charAt(0)), FontRenderManager.DrawMode.NORMAL_BOLD) / 2), -1, FontRenderManager.DrawMode.NORMAL_BOLD);
        pos[1] = pos[1] + 6 + (FontUtils.getTextHeight(waypoint.getName()) / 2);
        BThackRender.drawCenteredString(waypoint.getName(), pos[0], pos[1], -1);
        String text = "(" + Constants.DECIMAL_FORMAT.format(MathUtils.getDistance(mc.player.getPos(), position)) + "m.)";
        BThackRender.drawCenteredString(text, pos[0], pos[1] + 6 + (FontUtils.getTextHeight(text, FontRenderManager.DrawMode.SMALL) / 2), -1, FontRenderManager.DrawMode.SMALL);
    }
}
