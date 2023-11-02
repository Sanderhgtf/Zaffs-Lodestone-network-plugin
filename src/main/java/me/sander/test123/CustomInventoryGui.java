package me.sander.test123;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CustomInventoryGui implements Listener {
    private final Inventory inventory;
    private final List<LodestoneCoordinate> lodestoneCoordinates;

    public CustomInventoryGui(List<LodestoneCoordinate> lodestoneCoordinates) {
        inventory = Bukkit.createInventory(null, 27, "Custom GUI");
        this.lodestoneCoordinates = lodestoneCoordinates;
        loadNetherStarNames();
    }

    @EventHandler
    public void onRightClickCalcite(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction().toString().contains("RIGHT") && event.getClickedBlock() != null &&
                event.getClickedBlock().getType() == Material.CALCITE) {
            open(player);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == inventory) {
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() == Material.CALCITE) {
                String displayName = clickedItem.getItemMeta().getDisplayName();
                Player player = (Player) event.getWhoClicked();

                for (LodestoneCoordinate coordinate : lodestoneCoordinates) {
                    if (coordinate.getNetherStarName().equals(displayName)) {
                        String message = String.format("%s clicked: X=%d, Y=%d, Z=%d in World: %s",
                                player.getName(),
                                coordinate.getLocation().getBlockX(),
                                coordinate.getLocation().getBlockY(),
                                coordinate.getLocation().getBlockZ(),
                                coordinate.getWorldName());
                        Bukkit.getLogger().info(message);

                        // Teleport the player one block above
                        Location targetLocation = coordinate.getLocation().clone().add(0.5, 1, 0.5);
                        player.teleport(targetLocation);

                        event.setCancelled(true); // Prevent item movement
                        break;
                    }
                }
            }
        }
    }





    public void open(Player player) {
        player.openInventory(inventory);
    }

    public void loadNetherStarNames() {
        // Fill the inventory with Calcite blocks named after NetherStarNames
        for (LodestoneCoordinate lodestone : lodestoneCoordinates) {
            String netherStarName = lodestone.getNetherStarName();
            ItemStack item = new ItemStack(Material.CALCITE);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(netherStarName);
            item.setItemMeta(itemMeta);
            inventory.addItem(item);
        }
    }
}
