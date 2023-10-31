package me.sander.test123;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import java.util.logging.Logger;

public class NetherStarClickListener implements Listener {
    private final Logger logger;

    public NetherStarClickListener(Logger logger) {
        this.logger = logger;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Check if the player right-clicked
        if (event.getAction().toString().contains("RIGHT")) {
            // Check if the player is holding a Nether Star
            ItemStack itemInHand = event.getItem();
            if (itemInHand != null && itemInHand.getType() == Material.NETHER_STAR) {
                // Remove the Nether Star from the player's hand
                itemInHand.setAmount(itemInHand.getAmount() - 1);

                // Print "Nether Star used" to the console
                logger.info("Nether Star used");
            } else {
                // Print "Hold a Nether Star to create a lodestone" to the console
                logger.info("Hold a Nether Star to create a lodestone");
            }
        }
    }
}
