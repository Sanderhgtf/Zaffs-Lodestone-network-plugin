package me.sander.test123;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class LodestoneRemover implements Listener {
    private final String dataFilePath = "plugins/Test123/lodestone_data.json";

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.LODESTONE) {
            Location location = event.getBlock().getLocation();
            String worldName = location.getWorld().getName();

            if (hasStoredData(location, worldName)) {
                Bukkit.getLogger().info("This lodestone has stored data");
            } else {
                Bukkit.getLogger().info("This lodestone does not have stored data");
            }
        }
    }

    private boolean hasStoredData(Location location, String worldName) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader fileReader = new FileReader(dataFilePath)) {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(fileReader);
            for (Object obj : jsonArray) {
                if (obj instanceof JSONObject) {
                    JSONObject locationObject = (JSONObject) obj;
                    int x = ((Long) locationObject.get("X")).intValue();
                    int y = ((Long) locationObject.get("Y")).intValue();
                    int z = ((Long) locationObject.get("Z")).intValue();
                    String storedWorldName = (String) locationObject.get("World");

                    if (x == location.getBlockX() && y == location.getBlockY() && z == location.getBlockZ() && storedWorldName.equals(worldName)) {
                        return true;
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
