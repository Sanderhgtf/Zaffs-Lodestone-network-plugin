package me.sander.test123;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
        loadNetherStarNames(0, null); // Initial load without a selected index
    }

    @EventHandler
    public void onRightClickLodestone(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null &&
                event.getClickedBlock().getType() == Material.LODESTONE) {
            // Get the location of the clicked lodestone
            Location lodestoneLocation = event.getClickedBlock().getLocation();

            // Check if a corresponding activated lodestone exists in the coordinates
            int selectedIndex = -1;

            for (int i = 0; i < lodestoneCoordinates.size(); i++) {
                LodestoneCoordinate coordinate = lodestoneCoordinates.get(i);
                if (coordinate.getLocation().getBlockX() == lodestoneLocation.getBlockX() &&
                        coordinate.getLocation().getBlockY() == lodestoneLocation.getBlockY() &&
                        coordinate.getLocation().getBlockZ() == lodestoneLocation.getBlockZ() &&
                        coordinate.getLocation().getWorld().getName().equals(lodestoneLocation.getWorld().getName())) {
                    selectedIndex = i;
                    break;
                }
            }

            if (selectedIndex != -1) {
                loadNetherStarNames(selectedIndex, lodestoneLocation);
                open(player);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == inventory) {
            event.setCancelled(true); // Prevent item movement

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() == Material.COMPASS) {
                Player player = (Player) event.getWhoClicked();
                int slot = event.getRawSlot();
                if (slot >= 0 && slot < inventory.getSize()) {
                    LodestoneCoordinate coordinate = lodestoneCoordinates.get(slot);
                    String message = String.format("%s clicked: X=%d, Y=%d, Z=%d in World: %s",
                            player.getName(),
                            coordinate.getLocation().getBlockX(),
                            coordinate.getLocation().getBlockY(),
                            coordinate.getLocation().getBlockZ(),
                            coordinate.getNetherStarName());
                    Bukkit.getLogger().info(message);

                    float originalPitch = player.getLocation().getPitch();
                    float originalYaw = player.getLocation().getYaw();

                    Location targetLocation = coordinate.getLocation().clone().add(0.5, 1, 0.5);
                    targetLocation.setPitch(originalPitch);
                    targetLocation.setYaw(originalYaw);
                    player.teleport(targetLocation);

                    sendActionBarMessage(player, ChatColor.WHITE + coordinate.getNetherStarName());

                    targetLocation.getWorld().playSound(targetLocation, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 0.5F, 0.2F);

                    player.spawnParticle(Particle.SMOKE_NORMAL, targetLocation, 100, 0.5, 1, 0.5, 0.1);
                    player.spawnParticle(Particle.ENCHANTMENT_TABLE, targetLocation, 100, 0.5, 1, 0.5, 0.1);

                    PotionEffect darknessEffect = new PotionEffect(PotionEffectType.BLINDNESS, 20, 1);
                    player.addPotionEffect(darknessEffect);
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

    public void loadNetherStarNames(int selectedIndex, Location playerInteractingLocation) {
        inventory.clear(); // Clear the existing items in the inventory
        for (int i = 0; i < lodestoneCoordinates.size(); i++) {
            LodestoneCoordinate lodestone = lodestoneCoordinates.get(i);
            String netherStarName = lodestone.getNetherStarName();
            ItemStack item;
            ItemMeta itemMeta;

            if (i == selectedIndex) {
                item = new ItemStack(Material.BARRIER);
                itemMeta = item.getItemMeta();
                itemMeta.setLore(Collections.singletonList(ChatColor.RED + "Your location"));
            } else {
                item = new ItemStack(Material.COMPASS);
                itemMeta = item.getItemMeta();
                itemMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Click to teleport"));
            }

            itemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.setDisplayName(ChatColor.YELLOW + netherStarName);

            item.setItemMeta(itemMeta);
            inventory.addItem(item);
        }
    }
}
