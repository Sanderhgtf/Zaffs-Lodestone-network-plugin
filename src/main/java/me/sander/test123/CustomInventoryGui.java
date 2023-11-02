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
        loadNetherStarNames();
    }

    @EventHandler
    public void onRightClickLodestone(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction().toString().contains("RIGHT") && event.getClickedBlock() != null &&
                event.getClickedBlock().getType() == Material.LODESTONE) {
            // Get the location of the clicked lodestone
            Location lodestoneLocation = event.getClickedBlock().getLocation();

            // Check if a corresponding activated lodestone exists in the coordinates
            if (lodestoneCoordinates.stream().anyMatch(coordinate ->
                    coordinate.getLocation().getBlockX() == lodestoneLocation.getBlockX() &&
                            coordinate.getLocation().getBlockY() == lodestoneLocation.getBlockY() &&
                            coordinate.getLocation().getBlockZ() == lodestoneLocation.getBlockZ() &&
                            coordinate.getLocation().getWorld().getName().equals(lodestoneLocation.getWorld().getName()))) {
                open(player);
                event.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == inventory) {
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() == Material.COMPASS) {
                String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
                Player player = (Player) event.getWhoClicked();

                for (LodestoneCoordinate coordinate : lodestoneCoordinates) {
                    if (ChatColor.stripColor(coordinate.getNetherStarName()).equals(displayName)) {
                        String message = String.format("%s clicked: X=%d, Y=%d, Z=%d in World: %s",
                                player.getName(),
                                coordinate.getLocation().getBlockX(),
                                coordinate.getLocation().getBlockY(),
                                coordinate.getLocation().getBlockZ(),
                                coordinate.getNetherStarName()); // Display the Nether Star name
                        Bukkit.getLogger().info(message);

                        // Get the player's original pitch and yaw
                        float originalPitch = player.getLocation().getPitch();
                        float originalYaw = player.getLocation().getYaw();

                        // Teleport the player one block above
                        Location targetLocation = coordinate.getLocation().clone().add(0.5, 1, 0.5);
                        targetLocation.setPitch(originalPitch);
                        targetLocation.setYaw(originalYaw);
                        player.teleport(targetLocation);

                        sendActionBarMessage(player, ChatColor.WHITE + coordinate.getNetherStarName()); // Display the Nether Star name

                        // Play the BLOCK_ENCHANTMENT_TABLE_USE sound at the target location
                        targetLocation.getWorld().playSound(targetLocation, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 0.5F, 0.2F);

                        // Create a dark smoke particle effect for the player being teleported
                        player.spawnParticle(Particle.SMOKE_NORMAL, targetLocation, 100, 0.5, 1, 0.5, 0.1);
                        player.spawnParticle(Particle.ENCHANTMENT_TABLE, targetLocation, 100, 0.5, 1, 0.5, 0.1);

                        // Give the player the darkness effect for 2 seconds
                        PotionEffect darknessEffect = new PotionEffect(PotionEffectType.BLINDNESS, 20, 1);
                        player.addPotionEffect(darknessEffect);

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

    private void sendActionBarMessage(Player player, String message) {
        player.sendTitle("", message, 0, 20 * 2, 10);
    }

    public void loadNetherStarNames() {
        // Fill the inventory with Compass items named after NetherStarNames without enchant name
        for (LodestoneCoordinate lodestone : lodestoneCoordinates) {
            String netherStarName = lodestone.getNetherStarName();
            ItemStack item = new ItemStack(Material.COMPASS);
            ItemMeta itemMeta = item.getItemMeta();

            // Apply the enchantment effect without any enchant name
            itemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);

            // Create a custom ItemFlag to hide the enchantment name
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            // Set the display name with custom chat color
            itemMeta.setDisplayName(ChatColor.YELLOW + netherStarName);

            // Add lore with custom chat color
            itemMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Click to teleport"));

            item.setItemMeta(itemMeta);
            inventory.addItem(item);
        }
    }
}