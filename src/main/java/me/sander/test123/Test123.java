package me.sander.test123;

import org.bukkit.plugin.java.JavaPlugin;

public final class Test123 extends JavaPlugin {

    @Override
    public void onEnable() {
        // Register your listeners
        getServer().getPluginManager().registerEvents(new BeaconClickListener(), this);
        getServer().getPluginManager().registerEvents(new LodestoneRemover(), this);

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
