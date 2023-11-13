package me.sander.test123;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
///
public final class Test123 extends JavaPlugin {

    @Override
    public void onEnable() {
        // Ensure the data folder exists, creating it and the file if necessary
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        File dataFile = new File(getDataFolder(), "lodestone_data.json");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // Create a list of LodestoneCoordinate data and add your data as needed
        List<LodestoneCoordinate> lodestoneCoordinates = new ArrayList<>();
        // Add your LodestoneCoordinate objects to the list as needed

        // Create an instance of the BeaconClickListener and pass the LodestoneCoordinate data
        BeaconClickListener beaconClickListener = new BeaconClickListener(lodestoneCoordinates);

        // Create an instance of the CustomInventoryGui and pass the LodestoneCoordinate data
        CustomInventoryGui customGui = new CustomInventoryGui(lodestoneCoordinates);

        // Create an instance of the StoneRightClickListener and register it
        StoneRightClickListener stoneRightClickListener = new StoneRightClickListener(this);
        getServer().getPluginManager().registerEvents(stoneRightClickListener, this);

        // Create instances of your other classes
        LodestoneRemover lodestoneRemover = new LodestoneRemover(this);

        // Register your listeners
        getServer().getPluginManager().registerEvents(beaconClickListener, this);
        getServer().getPluginManager().registerEvents(lodestoneRemover, this);
        getServer().getPluginManager().registerEvents(customGui, this);

        // Register the RedstoneParticleScheduler
        RedstoneParticleScheduler particleScheduler = new RedstoneParticleScheduler(lodestoneCoordinates);
    }




    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
