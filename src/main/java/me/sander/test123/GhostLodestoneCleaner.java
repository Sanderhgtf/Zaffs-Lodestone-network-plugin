package me.sander.test123;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GhostLodestoneCleaner {
    private final String dataFilePath = "plugins/Test123/lodestone_data.json";

    public void cleanUp(List<LodestoneCoordinate> lodestoneCoordinates, World world) {
        List<Location> ghostLodestonesToRemove = new ArrayList<>();

        for (LodestoneCoordinate coordinate : lodestoneCoordinates) {
            Location location = coordinate.getLocation();

            Bukkit.getConsoleSender().sendMessage("Checking location: " + location);

            if (location.getBlock().getType() != Material.LODESTONE) {
                Bukkit.getConsoleSender().sendMessage("Ghost lodestone found at: " + location);
                ghostLodestonesToRemove.add(location);
            }
        }

        for (Location locationToRemove : ghostLodestonesToRemove) {
            removeGhostLodestone(locationToRemove, world, lodestoneCoordinates);
        }

        Bukkit.getConsoleSender().sendMessage("GhostLodestoneCleaner ran");
    }

    private void removeGhostLodestone(Location location, World world, List<LodestoneCoordinate> lodestoneCoordinates) {
        LodestoneCoordinate lodestoneToRemove = null;
        for (LodestoneCoordinate coordinate : lodestoneCoordinates) {
            if (coordinate.getLocation().equals(location) && coordinate.getWorldName().equals(world.getName())) {
                lodestoneToRemove = coordinate;
                break;
            }
        }

        if (lodestoneToRemove != null) {
            lodestoneCoordinates.remove(lodestoneToRemove);
            saveCoordinatesToJson(lodestoneCoordinates);
            Bukkit.getConsoleSender().sendMessage("Removed ghost lodestone at: " + location);
        }
    }

    private void saveCoordinatesToJson(List<LodestoneCoordinate> lodestoneCoordinates) {
        JSONArray jsonArray = new JSONArray();
        for (LodestoneCoordinate lodestone : lodestoneCoordinates) {
            JSONObject locationObject = new JSONObject();
            Location location = lodestone.getLocation();
            locationObject.put("X", location.getBlockX());
            locationObject.put("Y", location.getBlockY());
            locationObject.put("Z", location.getBlockZ());
            locationObject.put("World", lodestone.getWorldName());
            locationObject.put("NetherStarName", lodestone.getNetherStarName());
            locationObject.put("Tier", lodestone.getTier());  // Include tier in JSON
            locationObject.put("ExperienceRequired", lodestone.getExperienceRequired());  // Include experienceRequired in JSON
            locationObject.put("ExperienceProgression", lodestone.getExperienceProgression());  // Include experienceProgression in JSON
            jsonArray.add(locationObject);
        }

        try (FileWriter fileWriter = new FileWriter(dataFilePath)) {
            jsonArray.writeJSONString(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("Error saving coordinates to JSON: " + e.getMessage());
        }
    }
}
