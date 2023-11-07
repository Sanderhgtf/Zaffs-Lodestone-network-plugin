package me.sander.test123;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.List;

public class CustomInventoryGui implements Listener {
    private final Inventory inventory;
    private final List<LodestoneCoordinate> lodestoneCoordinates;

    public CustomInventoryGui(List<LodestoneCoordinate> lodestoneCoordinates) {
        int numActiveLodestones = lodestoneCoordinates.size();
        int inventorySize = (int) (Math.ceil(numActiveLodestones / 9.0) * 9); // Round up to the nearest multiple of 9
        inventory = Bukkit.createInventory(null, Math.min(inventorySize, 54), "LODESTONE NETWORK");
        this.lodestoneCoordinates = lodestoneCoordinates;
    }

    @EventHandler
    public void onRightClickLodestone(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction().toString().contains("RIGHT") && event.getClickedBlock() != null &&
                event.getClickedBlock().getType() == Material.LODESTONE) {
            // Get the location of the clicked lodestone
            Location lodestoneLocation = event.getClickedBlock().getLocation();

            boolean matchingLodestone = false;

            for (int i = 0; i < lodestoneCoordinates.size(); i++) {
                LodestoneCoordinate coordinate = lodestoneCoordinates.get(i);

                if (coordinate.getLocation().getBlockX() == lodestoneLocation.getBlockX() &&
                        coordinate.getLocation().getBlockY() == lodestoneLocation.getBlockY() &&
                        coordinate.getLocation().getBlockZ() == lodestoneLocation.getBlockZ() &&
                        coordinate.getLocation().getWorld().getName().equals(lodestoneLocation.getWorld().getName())) {
                    matchingLodestone = true;
                    loadNetherStarNames(i, player.getLocation());
                    open(player);
                    event.setCancelled(true);
                    break;
                }
            }

            if (!matchingLodestone) {
                // Handle the case when no matching lodestone is found
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Handle inventory click events as before
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    private void sendActionBarMessage(Player player, String message) {
        player.sendTitle("", message, 0, 20 * 2, 10);
    }

    public void loadNetherStarNames(int selectedIndex, Location playerInteractingLocation) {
        for (int i = 0; i < lodestoneCoordinates.size(); i++) {
            String netherStarName = lodestoneCoordinates.get(i).getNetherStarName();
            ItemStack item;
            ItemMeta itemMeta;

            if (i == selectedIndex) {
                // Display the selected lodestone as a barrier and indicate the player's location
                item = new ItemStack(Material.BARRIER);
                itemMeta = item.getItemMeta();
                itemMeta.setLore(Collections.singletonList(ChatColor.RED + "Your location"));
            } else {
                // Display other lodestones as Compass items for teleportation
                item = new ItemStack(Material.COMPASS);
                itemMeta = item.getItemMeta();
                itemMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Click to teleport"));
            }

            // Apply the enchantment effect without any enchant name
            itemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);

            // Create a custom ItemFlag to hide the enchantment name
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            // Set the display name with custom chat color
            itemMeta.setDisplayName(ChatColor.YELLOW + netherStarName);

            item.setItemMeta(itemMeta);
            inventory.addItem(item);
        }
    }
}
