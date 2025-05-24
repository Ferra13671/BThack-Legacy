package com.ferra13671.BThack.managers.managers.Waypoint;

import com.ferra13671.BThack.api.Utils.Initializable;

import java.util.ArrayList;
import java.util.List;

public class WaypointManager implements Initializable {
    private final List<Waypoint> overworldWaypoints = new ArrayList<>();
    private final List<Waypoint> endWaypoints = new ArrayList<>();
    private final List<Waypoint> netherWaypoints = new ArrayList<>();

    @Override
    public void init() {
    }

    public List<Waypoint> getWaypoints() {
        List<Waypoint> list = new ArrayList<>();
        list.addAll(overworldWaypoints);
        list.addAll(endWaypoints);
        list.addAll(netherWaypoints);
        return list;
    }

    public List<Waypoint> getOverworldWaypoints() {
        return new ArrayList<>(overworldWaypoints);
    }

    public List<Waypoint> getEndWaypoints() {
        return new ArrayList<>(endWaypoints);
    }

    public List<Waypoint> getNetherWaypoints() {
        return new ArrayList<>(netherWaypoints);
    }

    public void addWaypoint(Waypoint waypoint) {
        switch (waypoint.getDimension()) {
            case OVERWORLD -> overworldWaypoints.add(waypoint);
            case END -> endWaypoints.add(waypoint);
            case NETHER -> netherWaypoints.add(waypoint);
        }
    }

    public Waypoint getWaypoint(String name) {
        for (Waypoint waypoint : overworldWaypoints)
            if (waypoint.getName().equals(name)) return waypoint;
        for (Waypoint waypoint : endWaypoints)
            if (waypoint.getName().equals(name)) return waypoint;
        for (Waypoint waypoint : netherWaypoints)
            if (waypoint.getName().equals(name)) return waypoint;
        return null;
    }

    public void removeWaypoint(Waypoint waypoint) {
        overworldWaypoints.remove(waypoint);
        endWaypoints.remove(waypoint);
        netherWaypoints.remove(waypoint);
    }
}
