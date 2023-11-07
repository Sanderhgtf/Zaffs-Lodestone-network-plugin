package me.sander.test123;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
//
public class PluginReloader {
    public static void reloadPlugin(Plugin plugin) {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.disablePlugin(plugin);
        pluginManager.enablePlugin(plugin);
        Bukkit.getConsoleSender().sendMessage("Plugin manually reloaded");
    }
}
