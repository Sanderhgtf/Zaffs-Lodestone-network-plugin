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

    ///

    private final String dataFilePath = "plugins/Test123/lodestone_data.json"; // Define dataFilePath here

    public void cleanUp(List<LodestoneCoordinate> lodestoneCoordinates, World world) {
        // Create a list to store ghost lodestones to remove
        List<Location> ghostLodestonesToRemove = new ArrayList<>();

        // Iterate through the coordinates
        for (LodestoneCoordinate coordinate : lodestoneCoordinates) {
            Location location = coordinate.getLocation();

            // Debugging output
            Bukkit.getConsoleSender().sendMessage("Checking location: " + location);

            // Check if a lodestone exists at the stored location in the world
            if (location.getBlock().getType() != Material.LODESTONE) {
                Bukkit.getConsoleSender().sendMessage("Ghost lodestone found at: " + location);
                ghostLodestonesToRemove.add(location);
            }
        }

        // Remove the ghost lodestones
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
                break; // No need to continue searching
            }
        }

        if (lodestoneToRemove != null) {
            lodestoneCoordinates.remove(lodestoneToRemove);

            // Save the updated coordinates to the JSON file
            saveCoordinatesToJson(lodestoneCoordinates);

            Bukkit.getConsoleSender().sendMessage("Removed ghost lodestone at: " + location);
        }
    }

    // Method to save the updated coordinates to the JSON file
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
            jsonArray.add(locationObject);
        }

        try (FileWriter fileWriter = new FileWriter(dataFilePath)) {
            jsonArray.writeJSONString(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
