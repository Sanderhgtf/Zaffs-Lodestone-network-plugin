package me.sander.test123;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;

public class CustomInventoryGui implements Listener {
    private final Inventory inventory;
    private final List<LodestoneCoordinate> lodestoneCoordinates; // Change the variable name

    public CustomInventoryGui(List<LodestoneCoordinate> lodestoneCoordinates) { // Change the parameter name
        inventory = Bukkit.createInventory(null, 27, "Custom GUI");
        this.lodestoneCoordinates = lodestoneCoordinates; // Change the variable name
        loadNetherStarNames();
    }

    @EventHandler
    public void onRightClickCalcite(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction().toString().contains("RIGHT") && event.getClickedBlock() != null &&
                event.getClickedBlock().getType() == Material.CALCITE) {
            open(player);
            event.setCancelled(true); // Prevent right-clicking the calcite block
        }
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public void loadNetherStarNames() {
        // Fill the inventory with Calcite blocks named after NetherStarNames
        for (LodestoneCoordinate lodestone : lodestoneCoordinates) { // Change the variable name
            String netherStarName = lodestone.getNetherStarName(); // Assuming you have a getNetherStarName() method
            ItemStack item = new ItemStack(Material.CALCITE);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(netherStarName);
            item.setItemMeta(itemMeta);
            inventory.addItem(item);
        }
    }
}
