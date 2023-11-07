package me.sander.test123;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
//
public class RedstoneParticleScheduler {
    private final List<LodestoneCoordinate> lodestoneCoordinates;
    private final Map<Location, BukkitRunnable> animationTasks = new HashMap<>();
    private final int PARTICLE_VISIBILITY_DISTANCE = 25; // Blocks

    public RedstoneParticleScheduler(List<LodestoneCoordinate> lodestoneCoordinates) {
        this.lodestoneCoordinates = lodestoneCoordinates;
        startParticleTasks();
    }

    private void startParticleTasks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (LodestoneCoordinate lodestone : lodestoneCoordinates) {
                    Location location = lodestone.getLocation();
                    if (isLocationVisibleToPlayers(location)) {
                        startAnimation(location);
                    } else {
                        stopAnimation(location);
                    }
                }
            }
        }.runTaskTimer(Test123.getPlugin(Test123.class), 0, 20L); // 1 second
    }

    private boolean isLocationVisibleToPlayers(Location location) {
        int distanceSquared = PARTICLE_VISIBILITY_DISTANCE * PARTICLE_VISIBILITY_DISTANCE;
        for (Player player : location.getWorld().getPlayers()) {
            if (player.getLocation().distanceSquared(location) <= distanceSquared) {
                return true;
            }
        }
        return false;
    }

    private void startAnimation(Location location) {
        if (!animationTasks.containsKey(location)) {
            BukkitRunnable task = new BukkitRunnable() {
                double angle = 0;
                double verticalOffset = 0;
                int step = 0;
                int maxSteps = 20; // Number of steps for a complete animation cycle
                Location startingLocation = location.clone().add(0.5, 3, 0.5);
                double radius = 1.45; // Customize the turning radius
                double height = 2.75; // Customize the height
                double speed = 5.25; // Customize the rotation speed
                double verticalAmplitude = 0.4; // Customize the vertical amplitude (the height of the wave)

                @Override
                public void run() {
                    if (step >= maxSteps) {
                        step = 0;
                    }

                    double x = radius * Math.cos(Math.toRadians(angle));
                    double z = radius * Math.sin(Math.toRadians(angle));

                    // Calculate the vertical position in the wave
                    double y = verticalOffset + height;
                    Location currentLocation = startingLocation.clone().add(x, y, z);

                    // Create a DustOptions object with the color transitioning
                    int r = 153 + step * (239 - 153) / maxSteps;
                    int g = 0 + step * (35 - 0) / maxSteps;
                    int b = 0 + step * (2 - 0) / maxSteps;
                    Particle.DustOptions dustOptions = new Particle.DustOptions(
                            Color.fromRGB(r, g, b), 1
                    );

                    // Spawn the particle with the DustOptions
                    location.getWorld().spawnParticle(Particle.REDSTONE, currentLocation, 0, 0, 0, 0, 1, dustOptions);

                    angle += speed; // Adjust the rotation speed

                    // Update the vertical offset for the wave pattern
                    verticalOffset = verticalAmplitude * Math.sin(Math.toRadians(angle * 2)); // You can adjust the factor to control the wave frequency

                    step++;
                }
            };
            animationTasks.put(location, task);
            task.runTaskTimer(Test123.getPlugin(Test123.class), 0, 1); // 1 tick, change as needed
        }
    }

    private void stopAnimation(Location location) {
        BukkitRunnable task = animationTasks.remove(location);
        if (task != null) {
            task.cancel();
        }
    }
}
