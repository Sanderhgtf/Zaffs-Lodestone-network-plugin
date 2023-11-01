package me.sander.test123;

import org.bukkit.Location;
import org.bukkit.Bukkit;


import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LodestoneRemover implements Listener {

    ///

    private final String dataFilePath = "plugins/Test123/lodestone_data.json";
    private List<LodestoneCoordinate> lodestoneCoordinates = new ArrayList<>();
    private GhostLodestoneCleaner ghostLodestoneCleaner;

    public LodestoneRemover(Test123 test123) {
        loadCoordinatesFromJson();
        ghostLodestoneCleaner = new GhostLodestoneCleaner();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.LODESTONE) {
            Player player = event.getPlayer();
            Location location = event.getBlock().getLocation();
            String worldName = location.getWorld().getName();

            if (removeCoordinatesFromJson(location, worldName)) {
                Bukkit.getLogger().info("This lodestone had stored data which is now removed");

                // Send an action bar message to the player
                sendActionBarMessage(player, ChatColor.GOLD + "Lodestone disassembled..");

                // Apply Blindness to players within a 15-block radius
                int radius = 15; // Define the radius

                for (Player nearbyPlayer : location.getWorld().getPlayers()) {
                    if (nearbyPlayer.getLocation().distance(location) <= radius) {
                        int durationSeconds = 4; // Duration in seconds
                        int durationTicks = durationSeconds * 20; // Convert seconds to ticks

                        // Apply the Blindness effect to nearby players
                        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, durationTicks, 5));
                        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, durationTicks, 5));
                        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, durationTicks, 2));
                    }
                }

                // Call the cleanUp method from GhostLodestoneCleaner
                ghostLodestoneCleaner.cleanUp(lodestoneCoordinates, location.getWorld());

                PluginReloader.reloadPlugin(Test123.getPlugin(Test123.class));
                // PLACE THE METHOD HERE WHICH IS RESPONSIBLE FOR RELOADING THE PLUGIN
            } else {
                Bukkit.getLogger().info("This lodestone does not have stored data.");
            }
        }
    }

    // Send an action bar message to the player
    private void sendActionBarMessage(Player player, String message) {
        player.sendTitle("", message, 0, 20 * 2, 10);
    }

    private boolean removeCoordinatesFromJson(Location location, String worldName) {
        for (LodestoneCoordinate lodestone : lodestoneCoordinates) {
            if (lodestone.getLocation().equals(location) && lodestone.getWorldName().equals(worldName)) {
                lodestoneCoordinates.remove(lodestone);
                saveCoordinatesToJson();
                return true;
            }
        }
        return false;
    }

    private void saveCoordinatesToJson() {
        JSONArray jsonArray = new JSONArray();
        for (LodestoneCoordinate lodestone : lodestoneCoordinates) {
            JSONObject locationObject = new JSONObject();
            Location location = lodestone.getLocation();
            locationObject.put("X", location.getBlockX());
            locationObject.put("Y", location.getBlockY());
            locationObject.put("Z", location.getBlockZ());
            locationObject.put("World", lodestone.getWorldName());
            jsonArray.add(locationObject);
        }

        try (FileWriter fileWriter = new FileWriter(dataFilePath)) {
            jsonArray.writeJSONString(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Error saving coordinates to JSON: " + e.getMessage());
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
                    String netherStarName = (String) locationObject.get("NetherStarName");
                    lodestoneCoordinates.add(new LodestoneCoordinate(new Location(Bukkit.getWorld(worldName), x, y, z), worldName, netherStarName));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Error loading coordinates from JSON: " + e.getMessage());
        }
    }
}
