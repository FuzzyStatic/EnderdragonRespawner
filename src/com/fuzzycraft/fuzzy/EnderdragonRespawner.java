/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-20 21:02:33
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-30 22:33:35
 */

package com.fuzzycraft.fuzzy;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.logging.Level;

import com.fuzzycraft.fuzzy.event.Management;
import com.fuzzycraft.fuzzy.event.Structure;
import com.fuzzycraft.fuzzy.event.command.Arg;
import com.fuzzycraft.fuzzy.event.files.ConfigTree;
import com.fuzzycraft.fuzzy.event.listeners.EnderCrystals;
import com.fuzzycraft.fuzzy.event.listeners.EnderdragonPreventPortal;
import com.fuzzycraft.fuzzy.event.listeners.EnderdragonSpawnTimer;
import com.fuzzycraft.fuzzy.event.listeners.Obsidian;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class EnderdragonRespawner extends JavaPlugin {
  private EnderdragonRespawner plugin = this;
  private ConfigTree ct;
  private BukkitTask bt;

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

    // Register listeners
    this.plugin.getLogger().log(Level.INFO, "Registering listener events");
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(new EnderCrystals(plugin), plugin);
    pm.registerEvents(new Obsidian(plugin), plugin);
    pm.registerEvents(new EnderdragonSpawnTimer(plugin), plugin);
    pm.registerEvents(new EnderdragonPreventPortal(plugin), plugin);

    this.bt = new BukkitRunnable() {
      public void run() {
        // Start events
        for (Entry<World, Structure> e : Management.getEventMap().entrySet()) {
          World w = e.getKey();
          Structure s = e.getValue();

          if (s.getConfig().getActive()) {
            // Clean up event
            plugin.getLogger().log(
                Level.INFO, "Cleaning up previous event for " + w.getName());
            int removedRestart = Management.stop(plugin, w);
            switch (removedRestart) {
            case 0:
              plugin.getLogger().log(Level.INFO,
                                     "No Enderdragons found/removed");
              break;
            default:
              plugin.getLogger().log(
                  Level.INFO, removedRestart + " Enderdragons found/removed");
              break;
            }

            // Start event
            plugin.getLogger().log(Level.INFO, "Starting event for " +
                                                   w.getName() + " started");
            int added = Management.start(plugin, w);
            switch (added) {
            case 0:
              plugin.getLogger().log(Level.INFO, "No Enderdragons spawned");
              break;
            default:
              plugin.getLogger().log(Level.INFO,
                                     added + " Enderdragons spawned");
              break;
            }
            break;
          }
        }
      }
    }.runTaskLater(this, 1);
  }

  public void onDisable() {
    // Cancel start up task
    if (this.bt != null) {
      this.bt.cancel();
    }

    // Cancel event tasks
    for (Entry<World, Structure> e : Management.getEventMap().entrySet()) {
      World w = e.getKey();
      Structure s = e.getValue();

      if (s.cancelTasks()) {
        plugin.getLogger().log(Level.INFO,
                               "Cancelled tasks for " + w.getName());
      }
    }
  }
}
