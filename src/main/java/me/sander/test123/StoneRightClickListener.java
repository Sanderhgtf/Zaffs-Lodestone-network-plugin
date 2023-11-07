package me.sander.test123;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class StoneRightClickListener implements Listener {

    ////

    private final JavaPlugin plugin;

    public StoneRightClickListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRightClickStone(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.STONE) {
            int radius = 10;
            int totalCandleCount = 0;
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        Material blockType = event.getClickedBlock().getRelative(x, y, z).getType();
                        if (isCandle(blockType)) {
                            totalCandleCount++;
                        }
                    }
                }
            }
            plugin.getLogger().info("Candles of all colors in a 10-block radius: " + totalCandleCount);
        }
    }

    private boolean isCandle(Material material) {
        // Check if the material is a candle of any color
        return material == Material.WHITE_CANDLE ||
                material == Material.ORANGE_CANDLE ||
                material == Material.CANDLE ||
                material == Material.MAGENTA_CANDLE ||
                material == Material.LIGHT_BLUE_CANDLE ||
                material == Material.YELLOW_CANDLE ||
                material == Material.LIME_CANDLE ||
                material == Material.PINK_CANDLE ||
                material == Material.GRAY_CANDLE ||
                material == Material.LIGHT_GRAY_CANDLE ||
                material == Material.CYAN_CANDLE ||
                material == Material.PURPLE_CANDLE ||
                material == Material.BLUE_CANDLE ||
                material == Material.BROWN_CANDLE ||
                material == Material.GREEN_CANDLE ||
                material == Material.RED_CANDLE ||
                material == Material.BEACON ||
                material == Material.DIAMOND_BLOCK ||
                material == Material.NETHERITE_BLOCK ||
                material == Material.DRAGON_EGG ||
                material == Material.BLACK_CANDLE;
    }
}
