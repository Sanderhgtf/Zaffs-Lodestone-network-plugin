package me.sander.test123;

import org.bukkit.Location;

public class LodestoneCoordinate {

    private Location location;
    private String worldName;
    private String netherStarName;
    private int tier;
    private int experienceRequired;
    private int experienceProgression;

    public LodestoneCoordinate(Location location, String worldName, String netherStarName, int tier, int experienceRequired, int experienceProgression) {
        this.location = location;
        this.worldName = worldName;
        this.netherStarName = netherStarName;
        this.tier = tier;
        this.experienceRequired = experienceRequired;
        this.experienceProgression = experienceProgression;
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

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public int getExperienceRequired() {
        return experienceRequired;
    }

    public void setExperienceRequired(int experienceRequired) {
        this.experienceRequired = experienceRequired;
    }

    public int getExperienceProgression() {
        return experienceProgression;
    }

    public void setExperienceProgression(int experienceProgression) {
        this.experienceProgression = experienceProgression;
    }
}
