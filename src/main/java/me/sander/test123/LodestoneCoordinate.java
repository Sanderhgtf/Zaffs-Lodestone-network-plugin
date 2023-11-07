package me.sander.test123;

import org.bukkit.Location;

public class LodestoneCoordinate {

    private Location location;
    private String worldName;
    private String netherStarName;

    public LodestoneCoordinate(Location location, String worldName, String netherStarName) {
        this.location = location;
        this.worldName = worldName;
        this.netherStarName = netherStarName;
    }

    public Location getLocation() {
        return location;
    }

    public String getWorldName() {
        return worldName;
    }

    public String getNetherStarName() {
        return netherStarName;
    }
}