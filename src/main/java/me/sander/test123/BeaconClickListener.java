package me.sander.test123;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
// test message for git branch number 5
public class BeaconClickListener implements Listener {
    private final List<Location> beaconCoordinates = new ArrayList<>();
    private final String dataFilePath = "plugins/Test123/beacon_data.json";

    public BeaconClickListener() {
        loadCoordinatesFromJson();
    }

    @EventHandler
    public void onBeaconClick(PlayerInteractEvent event) {
        // Check if the player left-clicked a beacon
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BEACON) {
            // Get the beacon's location
            Block beacon = event.getClickedBlock();
            Location location = beacon.getLocation();

            // Add the beacon's location to the ArrayList
            beaconCoordinates.add(location);

            // Save the updated coordinates to the JSON file
            saveCoordinatesToJson();

            // Print the beacon's coordinates with world name to the console
            int x = location.getBlockX();
            int y = location.getBlockY();
            int z = location.getBlockZ();
            String worldName = location.getWorld().getName();
            Bukkit.getLogger().info("Beacon Coordinates in " + worldName + ": X=" + x + ", Y=" + y + ", Z=" + z);
        }
        // Check if the player left-clicked a stone block
        else if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.STONE) {
            // Print the entire ArrayList to the console
            Bukkit.getLogger().info("Beacon Coordinates:");
            for (Location location : beaconCoordinates) {
                int x = location.getBlockX();
                int y = location.getBlockY();
                int z = location.getBlockZ();
                Bukkit.getLogger().info("X=" + x + ", Y=" + y + ", Z=" + z);
            }
        }
    }

    private void saveCoordinatesToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Location location : beaconCoordinates) {
            JSONObject locationObject = new JSONObject();
            locationObject.put("X", location.getBlockX());
            locationObject.put("Y", location.getBlockY());
            locationObject.put("Z", location.getBlockZ());
            locationObject.put("World", location.getWorld().getName()); // Add the world name
            jsonArray.add(locationObject);
        }

        try (FileWriter fileWriter = new FileWriter(dataFilePath)) {
            jsonArray.writeJSONString(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCoordinatesFromJson() {
        JSONParser jsonParser = new JSONParser();
        try (FileReader fileReader = new FileReader(dataFilePath)) {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(fileReader);
            for (Object obj : jsonArray) {
                if (obj instanceof JSONObject) {
                    JSONObject locationObject = (JSONObject) obj;
                    int x = ((Long) locationObject.get("X")).intValue();
                    int y = ((Long) locationObject.get("Y")).intValue();
                    int z = ((Long) locationObject.get("Z")).intValue();
                    String worldName = (String) locationObject.get("World");
                    beaconCoordinates.add(new Location(Bukkit.getWorld(worldName), x, y, z));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
