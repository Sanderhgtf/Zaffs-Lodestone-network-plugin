package me.sander.test123;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class CustomInventoryGui implements Listener {
    private final Inventory inventory;

    public CustomInventoryGui() {
        inventory = Bukkit.createInventory(null, 27, "Custom GUI");

        // Fill the first slot with a Calcite block
        inventory.setItem(0, new ItemStack(Material.CALCITE));

        // Load data from the JSON file and determine the number of elements
        int numElements = loadElementsFromJson();

        // Fill the rest of the inventory with singular Calcite blocks
        for (int i = 1; i < numElements && i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemStack(Material.CALCITE));
        }
    }

    @EventHandler
    public void onRightClickCalcite(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction().toString().contains("RIGHT") && event.getClickedBlock() != null &&
                event.getClickedBlock().getType() == Material.CALCITE) {
            open(player);
            event.setCancelled(true); // Prevent right-clicking the Calcite block
        }
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    private int loadElementsFromJson() {
        int numElements = 0;
        JSONParser jsonParser = new JSONParser();

        try (FileReader fileReader = new FileReader("plugins/Test123/lodestone_data.json")) {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(fileReader);
            numElements = jsonArray.size();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return numElements;
    }
}
