package me.sander.test123;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BeaconClickListener implements Listener {
    private final List<LodestoneCoordinate> lodestoneCoordinates;
    private final String dataFilePath = "plugins/Test123/lodestone_data.json";

    public BeaconClickListener(List<LodestoneCoordinate> lodestoneCoordinates) {
        this.lodestoneCoordinates = lodestoneCoordinates;
        loadCoordinatesFromJson();
    }

    @EventHandler
    public void onBeaconClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction().toString().contains("RIGHT")) {
            ItemStack itemInHand = event.getItem();

            if (itemInHand != null && itemInHand.getType() == Material.NETHER_STAR) {
                Location clickedBlockLocation = event.getClickedBlock().getLocation();
                String worldName = clickedBlockLocation.getWorld().getName();
                String netherStarName = getNetherStarName(itemInHand);

                if (netherStarName == null || netherStarName.isEmpty()) {
                    if (!isCoordinatesStored(clickedBlockLocation, worldName)) {
                        player.sendMessage(ChatColor.YELLOW + "Rename the Nether Star to what you want this lodestone to be called.");
                    }
                } else if (!isCoordinatesStored(clickedBlockLocation, worldName)) {
                    if (clickedBlockLocation.getBlock().getType() == Material.LODESTONE) {
                            itemInHand.setAmount(itemInHand.getAmount() - 1);
                            lodestoneCoordinates.add(new LodestoneCoordinate(clickedBlockLocation, worldName, netherStarName));
                            saveCoordinatesToJson();
                            Bukkit.getLogger().info("Lodestone Coordinates in " + worldName + ": X=" + clickedBlockLocation.getBlockX() + ", Y=" + clickedBlockLocation.getBlockY() + ", Z=" + clickedBlockLocation.getBlockZ() + ", Nether Star Name: " + netherStarName);
                            sendActionBarMessage(player, ChatColor.GREEN + "Lodestone constructed");
                            playSoundNearbyPlayers(clickedBlockLocation);
                            clickedBlockLocation.getBlock().setMetadata("Test123Activated", new FixedMetadataValue(Test123.getPlugin(Test123.class), true));
                            PluginReloader.reloadPlugin(Test123.getPlugin(Test123.class));
                        } else {
                        player.sendMessage(ChatColor.GRAY + "I wonder what would happen if I used this on a lodestone..");
                    }
                    } else {
                    player.sendMessage(ChatColor.GOLD + "This is already a lodestone position");
                }
                }
            }
        }




    private String getNetherStarName(ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            return item.getItemMeta().getDisplayName();
        }
        return null;
    }

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
            locationObject.put("NetherStarName", lodestone.getNetherStarName());
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
                    String netherStarName = (String) locationObject.get("NetherStarName");
                    lodestoneCoordinates.add(new LodestoneCoordinate(new Location(Bukkit.getWorld(worldName), x, y, z), worldName, netherStarName));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private boolean isCoordinatesStored(Location location, String worldName) {
        for (LodestoneCoordinate lodestone : lodestoneCoordinates) {
            if (lodestone.getLocation().equals(location) && lodestone.getWorldName().equals(worldName)) {
                return true;
            }
        }
        return false;
    }

    private void playSoundNearbyPlayers(Location location) {
        for (Player nearbyPlayer : location.getWorld().getPlayers()) {
            if (nearbyPlayer.getLocation().distance(location) <= 15) {
                nearbyPlayer.playSound(location, Sound.ENTITY_VILLAGER_WORK_WEAPONSMITH, 1.2F, 0.35F);
            }
        }
    }
}
