/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-20 21:02:33
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-02-10 14:43:05
 */

package com.fuzzycraft.fuzzy;

import java.io.IOException;
import java.time.Instant;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.fuzzycraft.fuzzy.event.Management;
import com.fuzzycraft.fuzzy.event.Structure;
import com.fuzzycraft.fuzzy.event.command.Arg;
import com.fuzzycraft.fuzzy.event.files.Config;
import com.fuzzycraft.fuzzy.event.files.ConfigTree;
import com.fuzzycraft.fuzzy.event.files.Parameter;
import com.fuzzycraft.fuzzy.event.listeners.EnderCrystals;
import com.fuzzycraft.fuzzy.event.listeners.PreventPortalLegacy;
import com.fuzzycraft.fuzzy.event.listeners.Timer;
import com.fuzzycraft.fuzzy.event.listeners.Obsidian;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class EnderdragonRespawner extends JavaPlugin {
  private EnderdragonRespawner plugin = this;
  private ConfigTree ct;
  private BukkitTask bt;

  private boolean legacy = false;
  public static final String[] legacyVer = new String[] {
      "1.0", "1.1", "1.2", "1.3", "1.4", "1.5", "1.6", "1.7", "1.8",
  };

  public void onEnable() {
    // Check directory structure
    this.plugin.getLogger().log(Level.INFO, "Checking directory structure");

    for (String s : legacyVer) {
      if (Bukkit.getVersion().contains(s)) {
        legacy = true;
        break;
      }
    }

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

    registerListeners();

    this.bt = new BukkitRunnable() {
      public void run() {
        // Start events
        for (Entry<World, Structure> e : Management.getEventMap().entrySet()) {
          World w = e.getKey();
          Structure s = e.getValue();
          Config c = s.getConfig();
          long nextStartTime = c.getNextStartTime();

          if (c.getActive() && (nextStartTime != Parameter.BEGINNING_OF_TIME &&
                                nextStartTime != -1)) {
            /* Looks like the event was already completed and is awaiting
             * it's next start */
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

            long now = Instant.now().toEpochMilli();

            if (nextStartTime < now) {
              /* Looks like the server was offline when the next event
               * should have started. Let's start event now */
              plugin.getLogger().log(Level.INFO,
                                     "Starting event for " + w.getName());
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
            } else {
              /* Looks like the event still has time until it starts. Let's
               * set the timer */
              plugin.getLogger().log(Level.INFO,
                                     "Setting event timer for " + w.getName());
              int time =
                  (int)TimeUnit.MILLISECONDS.toSeconds((nextStartTime - now));
              Timer.nextEvent(plugin, w, time);
            }
          } else {
            switch ((int)nextStartTime) {
            case 0:
              plugin.getLogger().log(Level.INFO,
                                     "It seems event for " + w.getName() +
                                         " was never activated, ignoring");
              break;
            case -1:
              plugin.getLogger().log(Level.INFO, "Event for " + w.getName() +
                                                     " is already running");
              break;
            }
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

  private void registerListeners() {
    // Register listeners
    this.plugin.getLogger().log(Level.INFO, "Registering listener events");
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(new EnderCrystals(plugin), plugin);
    pm.registerEvents(new Obsidian(plugin), plugin);
    pm.registerEvents(new Timer(plugin), plugin);

    if (legacy) {
      pm.registerEvents(new PreventPortalLegacy(plugin),
                        plugin); // Legacy implementation
    }
  }
}
