package com.ferra13671.BThack.impl.commands;

import com.ferra13671.BThack.core.client.systems.config.SubConfigs;
import com.ferra13671.BThack.core.render.utils.ColorUtils;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.impl.command.AbstractCommand;
import com.ferra13671.BThack.managers.impl.command.Arguments;
import com.ferra13671.BThack.managers.impl.waypoint.Waypoint;
import com.ferra13671.BThack.impl.modules.client.ClientSettings;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class WaypointCommand extends AbstractCommand {
    public WaypointCommand() {
        super("lang.command.Waypoint.description", "waypoint");
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("list").then(arg("mode", Arguments.MODE("OnlyNames", "Full")).executes(context -> {
            String currentServer = mc.isInSingleplayer() ? "SinglePlayer" : mc.getNetworkHandler().getServerInfo().address;

            sendMessage(Formatting.AQUA + "|$#> " + ClientSettings.getFriendColor() + "Waypoints" + Formatting.AQUA + " <#&|");
            sendMessage(Formatting.GREEN + " " + currentServer);

            sendMessage(Formatting.DARK_PURPLE + "  Overworld:");
            sendWaypointList(Managers.WAYPOINT_MANAGER.getOverworldWaypoints(), context, currentServer);

            sendMessage(Formatting.DARK_PURPLE + "  Nether:");
            sendWaypointList(Managers.WAYPOINT_MANAGER.getNetherWaypoints(), context, currentServer);

            sendMessage(Formatting.DARK_PURPLE + "  End:");
            sendWaypointList(Managers.WAYPOINT_MANAGER.getEndWaypoints(), context, currentServer);

            return SUCCESFUL;
        })));
        builder.then(literal("add").then(
                arg("name", Arguments.STRING_ONE).then(
                arg("x", Arguments.DOUBLE).then(arg("y", Arguments.DOUBLE).then(arg("z", Arguments.DOUBLE)
                .then(arg("visible", Arguments.BOOLEAN).then(arg("dimension", Arguments.MODE("OVERWORLD", "END", "NETHER"))
                .then(arg("red", Arguments.INTEGER(0, 255))
                .then(arg("green", Arguments.INTEGER(0, 255))
                .then(arg("blue", Arguments.INTEGER(0, 255))
                .executes(context -> {
                    Managers.WAYPOINT_MANAGER.addWaypoint(
                            new Waypoint(
                                    context.getArgument("name", String.class),
                                    new Vec3d(
                                            context.getArgument("x", Double.class),
                                            context.getArgument("y", Double.class),
                                            context.getArgument("z", Double.class)
                                    ),
                                    context.getArgument("visible", Boolean.class),
                                    Waypoint.WaypointDimension.valueOf(context.getArgument("dimension", String.class)),
                                    ColorUtils.fastRGBA(
                                            context.getArgument("red", Integer.class),
                                            context.getArgument("green", Integer.class),
                                            context.getArgument("blue", Integer.class),
                                            255
                                    ),
                                    mc.isInSingleplayer() ? "SinglePlayer" : mc.getNetworkHandler().getServerInfo().address
                            )
                    );
                    SubConfigs.WAYPOINTS.save();

                    sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.Waypoint.added"), context.getArgument("name", String.class)));

                    return SUCCESFUL;
        })))))))))));
        builder.then(literal("remove").then(arg("waypoint", Arguments.WAYPOINT).executes(context -> {
            Managers.WAYPOINT_MANAGER.removeWaypoint(context.getArgument("waypoint", Waypoint.class));
            SubConfigs.WAYPOINTS.save();

            sendMessage(Formatting.AQUA +  String.format(LanguageSystem.translate("lang.command.Waypoint.removed"), Formatting.WHITE + context.getArgument("waypoint", Waypoint.class).getName() + Formatting.AQUA));

            return SUCCESFUL;
        })));
        builder.then(literal("get").then(arg("waypoint", Arguments.WAYPOINT)
                .then(literal("setName").then(arg("name", Arguments.STRING_ONE).executes(context -> {
                    Waypoint waypoint = context.getArgument("waypoint", Waypoint.class);
                    Managers.WAYPOINT_MANAGER.removeWaypoint(waypoint);
                    String oldName = waypoint.getName();
                    String newName = context.getArgument("name", String.class);
                    waypoint.setName(newName);
                    Managers.WAYPOINT_MANAGER.addWaypoint(waypoint);
                    SubConfigs.WAYPOINTS.save();

                    sendMessage(String.format(LanguageSystem.translate("lang.command.Waypoint.changedValue"), oldName, Formatting.AQUA, Formatting.WHITE + "Name: " + oldName + Formatting.AQUA, Formatting.WHITE + newName));

                    return SUCCESFUL;
                })))
                .then(literal("setPosition").then(arg("x", Arguments.DOUBLE).then(arg("y", Arguments.DOUBLE).then(arg("z", Arguments.DOUBLE).executes(context -> {
                    Waypoint waypoint = context.getArgument("waypoint", Waypoint.class);
                    Managers.WAYPOINT_MANAGER.removeWaypoint(waypoint);
                    Vec3d oldPos = waypoint.getPosition();
                    Vec3d newPos = new Vec3d(context.getArgument("x", Double.class), context.getArgument("y", Double.class), context.getArgument("z", Double.class));
                    waypoint.setPosition(newPos);
                    Managers.WAYPOINT_MANAGER.addWaypoint(waypoint);
                    SubConfigs.WAYPOINTS.save();

                    sendMessage(String.format(LanguageSystem.translate("lang.command.Waypoint.changedValue"), waypoint.getName(), Formatting.AQUA, Formatting.WHITE + "Position: " + oldPos.getX() + " " + oldPos.getY() + " " + oldPos.getZ() + Formatting.AQUA, Formatting.WHITE + "" + newPos.getX() + " " + newPos.getY() + " " + newPos.getZ()));

                    return SUCCESFUL;
                })))))
                .then(literal("setVisible").then(arg("visible", Arguments.BOOLEAN).executes(context -> {
                    Waypoint waypoint = context.getArgument("waypoint", Waypoint.class);
                    Managers.WAYPOINT_MANAGER.removeWaypoint(waypoint);
                    boolean oldVisible = waypoint.isVisible();
                    boolean newVisible = context.getArgument("visible", Boolean.class);
                    waypoint.setVisible(newVisible);
                    Managers.WAYPOINT_MANAGER.addWaypoint(waypoint);
                    SubConfigs.WAYPOINTS.save();

                    sendMessage(String.format(LanguageSystem.translate("lang.command.Waypoint.changedValue"), waypoint.getName(), Formatting.AQUA, Formatting.WHITE + "Visible: " + oldVisible + Formatting.AQUA, Formatting.WHITE + "" + newVisible));

                    return SUCCESFUL;
                })))
                .then(literal("setDimension").then(arg("dimension", Arguments.MODE("OVERWORLD", "END", "NETHER")).executes(context -> {
                    Waypoint waypoint = context.getArgument("waypoint", Waypoint.class);
                    Managers.WAYPOINT_MANAGER.removeWaypoint(waypoint);
                    Waypoint.WaypointDimension oldDimension = waypoint.getDimension();
                    Waypoint.WaypointDimension newDimension = Waypoint.WaypointDimension.valueOf(context.getArgument("dimension", String.class));
                    waypoint.setDimension(newDimension);
                    Managers.WAYPOINT_MANAGER.addWaypoint(waypoint);
                    SubConfigs.WAYPOINTS.save();

                    sendMessage(String.format(LanguageSystem.translate("lang.command.Waypoint.changedValue"), waypoint.getName(), Formatting.AQUA, Formatting.WHITE + "Dimension: " + oldDimension.name() + Formatting.AQUA, Formatting.WHITE + newDimension.name()));

                    return SUCCESFUL;
                })))
                .then(literal("setColor").then(arg("red", Arguments.INTEGER(0, 255)).then(arg("green", Arguments.INTEGER(0, 255)).then(arg("blue", Arguments.INTEGER(0, 255)).executes(context -> {
                    Waypoint waypoint = context.getArgument("waypoint", Waypoint.class);
                    Managers.WAYPOINT_MANAGER.removeWaypoint(waypoint);
                    float[] oldColor = ColorUtils.hashCodeToRGB(waypoint.getColor());
                    int[] newColor = new int[]{context.getArgument("red", Integer.class), context.getArgument("green", Integer.class), context.getArgument("blue", Integer.class)};
                    waypoint.setColor(ColorUtils.fastRGBA(newColor[0], newColor[1], newColor[2], 255));
                    Managers.WAYPOINT_MANAGER.addWaypoint(waypoint);
                    SubConfigs.WAYPOINTS.save();

                    sendMessage(String.format(LanguageSystem.translate("lang.command.Waypoint.changedValue"), waypoint.getName(), Formatting.AQUA, Formatting.WHITE + "Color: " + (int) (oldColor[0] * 255) + " " + (int) (oldColor[1] * 255) + " " + (int) (oldColor[2] * 255) + Formatting.AQUA, Formatting.WHITE + "" + newColor[0] + " " + newColor[1] + " " + newColor[2]));

                    return SUCCESFUL;
                })))))
        ));
    }

    private void sendWaypointList(List<Waypoint> waypoints, CommandContext<CommandSource> context, String currentServer) {
        int count = 1;
        for (Waypoint waypoint : waypoints) {
            if (!currentServer.equals(waypoint.getServer())) continue;

            String text = Formatting.GRAY + "   " + count + "." + Formatting.WHITE + "Name: " + Formatting.DARK_AQUA + waypoint.getName();
            if (context.getArgument("mode", String.class).equals("Full")) {
                float[] colors = ColorUtils.hashCodeToRGB(waypoint.getColor());
                text = text +
                        Formatting.WHITE + "  X: " + Formatting.DARK_AQUA + waypoint.getPosition().getX() +
                        Formatting.WHITE + "  Y: " + Formatting.DARK_AQUA + waypoint.getPosition().getY() +
                        Formatting.WHITE + "  Z: " + Formatting.DARK_AQUA + waypoint.getPosition().getZ() +
                        Formatting.WHITE + "  Visible: " + Formatting.DARK_AQUA + waypoint.isVisible() +
                        Formatting.WHITE + "  Red: " + Formatting.DARK_AQUA + (int) (colors[0] * 255) +
                        Formatting.WHITE + "  Green: " + Formatting.DARK_AQUA + (int) (colors[1] * 255) +
                        Formatting.WHITE + "  Blue: " + Formatting.DARK_AQUA + (int) (colors[2] * 255);
            }
            sendMessage(text);
            count++;
        }
    }
}
