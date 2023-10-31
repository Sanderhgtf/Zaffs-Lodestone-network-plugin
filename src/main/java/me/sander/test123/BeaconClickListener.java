package me.sander.test123;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BeaconClickListener implements Listener {
    private final List<LodestoneCoordinate> lodestoneCoordinates = new ArrayList<>();
    private final String dataFilePath = "plugins/Test123/lodestone_data.json";

    public BeaconClickListener() {
        loadCoordinatesFromJson();
    }

    @EventHandler
    public void onBeaconClick(PlayerInteractEvent event) {
        Player player = event.getPlayer(); // Get the player

        // Check if the player right-clicked a block while holding a Nether Star
        if (event.getAction().toString().contains("RIGHT")) {
            ItemStack itemInHand = event.getItem();

            if (itemInHand != null && itemInHand.getType() == Material.NETHER_STAR) {
                Block clickedBlock = event.getClickedBlock();

                // Check if the clicked block is a lodestone
                if (clickedBlock != null && clickedBlock.getType() == Material.LODESTONE) {
                    Location location = clickedBlock.getLocation();
                    String worldName = location.getWorld().getName();

                    // Check if the coordinates + world are already stored
                    if (!isCoordinatesStored(location, worldName)) {
                        // Remove a Nether Star from the player's hand
                        itemInHand.setAmount(itemInHand.getAmount() - 1);

                        // Store the lodestone's location
                        lodestoneCoordinates.add(new LodestoneCoordinate(location, worldName));

                        // Save the updated coordinates to the JSON file
                        saveCoordinatesToJson();

                        // Print the lodestone's coordinates with the world name to the console
                        int x = location.getBlockX();
                        int y = location.getBlockY();
                        int z = location.getBlockZ();
                        Bukkit.getLogger().info("Lodestone Coordinates in " + worldName + ": X=" + x + ", Y=" + y + ", Z=" + z);

                        // Send an action bar message to the player
                        sendActionBarMessage(player, ChatColor.GREEN + "Lodestone constructed");

                        // Play a sound to all players within a 15-block radius
                        Location soundLocation = location; // Use the location where the lodestone was constructed

                        for (Player nearbyPlayer : location.getWorld().getPlayers()) {
                            if (nearbyPlayer.getLocation().distance(soundLocation) <= 15) {
                                // Adjust the sound parameters as needed (you can find available sounds in Sound category)
                                nearbyPlayer.playSound(soundLocation, Sound.ENTITY_VILLAGER_WORK_WEAPONSMITH, 1.2F, 0.35F);
                            }
                        }

                        PluginReloader.reloadPlugin(Test123.getPlugin(Test123.class));
                        // PLACE THE METHOD HERE WHICH IS RESPONSIBLE FOR RELOADING THE PLUGIN
                    } else {
                        // Print "This is already a lodestone position" to the console
                        Bukkit.getLogger().info("This is already a lodestone position");
                    }
                }
            }
        }
    }


    // Send an action bar message to the player
    private void sendActionBarMessage(Player player, String message) {
        player.sendTitle("", message, 0, 20 * 2, 10);
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
                    lodestoneCoordinates.add(new LodestoneCoordinate(new Location(Bukkit.getWorld(worldName), x, y, z), worldName));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private boolean isCoordinatesStored(Location location, String worldName) {
        // Check if the coordinates + world are already stored
        for (LodestoneCoordinate lodestone : lodestoneCoordinates) {
            if (lodestone.getLocation().equals(location) && lodestone.getWorldName().equals(worldName)) {
                return true;
            }
        }
        return false;
    }
}
