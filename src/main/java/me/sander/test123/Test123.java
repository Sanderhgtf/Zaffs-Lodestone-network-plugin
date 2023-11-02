package me.sander.test123;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;

public final class Test123 extends JavaPlugin {

    @Override
    public void onEnable() {
        // Create a list of LodestoneCoordinate data and add your data as needed
        List<LodestoneCoordinate> lodestoneCoordinates = new ArrayList<>();
        // Add your LodestoneCoordinate objects to the list as needed

        // Create an instance of the BeaconClickListener and pass the LodestoneCoordinate data
        BeaconClickListener beaconClickListener = new BeaconClickListener(lodestoneCoordinates);

        // Create an instance of the CustomInventoryGui and pass the LodestoneCoordinate data
        CustomInventoryGui customGui = new CustomInventoryGui(lodestoneCoordinates);

        // Create instances of your other classes
        LodestoneRemover lodestoneRemover = new LodestoneRemover(this);

        // Register your listeners
        getServer().getPluginManager().registerEvents(beaconClickListener, this);
        getServer().getPluginManager().registerEvents(lodestoneRemover, this);
        getServer().getPluginManager().registerEvents(customGui, this);

        // Ensure the data folder exists, creating it if necessary
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
