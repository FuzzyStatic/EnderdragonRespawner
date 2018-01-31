/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-20 21:02:33
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-30 22:33:35
 */

package com.fuzzycraft.fuzzy;

import java.io.IOException;
import java.util.logging.Level;

import com.fuzzycraft.fuzzy.event.Management;
import com.fuzzycraft.fuzzy.event.command.Arg;
import com.fuzzycraft.fuzzy.event.files.ConfigTree;
import com.fuzzycraft.fuzzy.event.listeners.EnderCrystals;
import com.fuzzycraft.fuzzy.event.listeners.EnderdragonPreventPortal;
import com.fuzzycraft.fuzzy.event.listeners.EnderdragonSpawnTimer;
import com.fuzzycraft.fuzzy.event.listeners.Obsidian;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class EnderdragonRespawner extends JavaPlugin {
  private EnderdragonRespawner plugin = this;
  private ConfigTree ct;

  public void onEnable() {
    // Check directory structure
    this.plugin.getLogger().log(Level.INFO, "Checking directory structure");

    ct = new ConfigTree(this);

    try {
      ct.createDirectoryStructure();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Create default configurations
    this.plugin.getLogger().log(
        Level.INFO,
        "Configurations only work if located in specific world directory within " +
            ct.getWorldsDirectory().toString());
    ct.createAllWorldsDefaultConfiguration();

    // Initiate events
    this.plugin.getLogger().log(Level.INFO, "Initializing events");
    new Management(this.plugin);

    // Set commands
    getCommand(Arg.BASE).setExecutor(
        new com.fuzzycraft.fuzzy.event.command.Management(this));

    new BukkitRunnable() {
      public void run() {
        plugin.getLogger().log(Level.INFO, "Registering listener events");
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new EnderCrystals(plugin), plugin);
        pm.registerEvents(new Obsidian(plugin), plugin);
        pm.registerEvents(new EnderdragonSpawnTimer(plugin), plugin);
        pm.registerEvents(new EnderdragonPreventPortal(plugin), plugin);
      }
    }
        .runTaskLater(this, 1);
  }

  public void onDisable() {}
}
