package me.sander.test123;

import org.bukkit.Location;

public class BeaconCoordinate {
    private Location location;
    private String worldName;

    public BeaconCoordinate(Location location, String worldName) {
        this.location = location;
        this.worldName = worldName;
    }

    public Location getLocation() {
        return location;
    }

    public String getWorldName() {
        return worldName;
    }
}
