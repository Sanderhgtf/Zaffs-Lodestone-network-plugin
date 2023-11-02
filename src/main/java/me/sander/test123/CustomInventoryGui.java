package me.sander.test123;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
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
import org.bukkit.Particle;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.List;

public class CustomInventoryGui implements Listener {
    private final Inventory inventory;
    private final List<LodestoneCoordinate> lodestoneCoordinates;

    public CustomInventoryGui(List<LodestoneCoordinate> lodestoneCoordinates) {
        inventory = Bukkit.createInventory(null, 27, "LODESTONE NETWORK");
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
                                coordinate.getWorldName());
                        Bukkit.getLogger().info(message);

                        // Teleport the player one block above
                        Location targetLocation = coordinate.getLocation().clone().add(0.5, 1, 0.5);
                        player.teleport(targetLocation);

                        // Create a dark smoke particle effect
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            onlinePlayer.spawnParticle(Particle.SMOKE_NORMAL, targetLocation, 100, 0.5, 1, 0.5, 0.1);
                        }

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
