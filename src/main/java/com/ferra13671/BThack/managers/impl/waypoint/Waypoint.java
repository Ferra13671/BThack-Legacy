package com.ferra13671.BThack.managers.impl.waypoint;

import com.ferra13671.BThack.core.render.utils.ColorUtils;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class Waypoint {
    public static final Supplier<String> DEFAULT_NAME = () -> "Waypoint-" + (System.currentTimeMillis() / 10000);
    public static final double[] DEFAULT_POSITION = new double[]{0, 0, 0};
    public static final boolean DEFAULT_VISIBLE = true;
    public static final WaypointDimension DEFAULT_DIMENSION = WaypointDimension.OVERWORLD;
    public static final int DEFAULT_COLOR = ColorUtils.fastRGBA(143, 0, 255, 255);

    private String name = DEFAULT_NAME.get();
    private Vec3d position = new Vec3d(DEFAULT_POSITION[0], DEFAULT_POSITION[1], DEFAULT_POSITION[2]);
    private boolean visible = DEFAULT_VISIBLE;
    private WaypointDimension dimension = DEFAULT_DIMENSION;
    private int color = DEFAULT_COLOR;
    private String server;

    public Waypoint(String name, Vec3d position, boolean visible, WaypointDimension dimension, int color, String server) {
        this.name = name;
        this.position = position;
        this.visible = visible;
        this.color = color;
        this.dimension = dimension;
        this.server = server;
    }

    public Waypoint() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vec3d getPosition() {
        return position;
    }

    public void setPosition(Vec3d position) {
        this.position = position;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public WaypointDimension getDimension() {
        return dimension;
    }

    public String getServer() {
        return server;
    }

    public void setDimension(WaypointDimension dimension) {
        this.dimension = dimension;
    }

    public enum WaypointDimension {
        OVERWORLD(World.OVERWORLD),
        END(World.END),
        NETHER(World.NETHER);

        private final RegistryKey<World> world;

        WaypointDimension(RegistryKey<World> world) {
            this.world = world;
        }

        public RegistryKey<World> getWorld() {
            return world;
        }
    }
}
