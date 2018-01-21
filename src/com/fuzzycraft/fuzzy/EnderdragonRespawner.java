/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-20 21:02:33
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-20 22:39:00
 */

package com.fuzzycraft.fuzzy;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzzycraft.fuzzy.event.Enderdragon;
import com.fuzzycraft.fuzzy.event.commands.Cmd;
import com.fuzzycraft.fuzzy.event.commands.Start;
import com.fuzzycraft.fuzzy.event.commands.Stop;
import com.fuzzycraft.fuzzy.event.files.Config;
import com.fuzzycraft.fuzzy.event.files.ConfigTree;
import com.fuzzycraft.fuzzy.event.files.Name;
import com.fuzzycraft.fuzzy.event.listeners.EnderdragonCrystals;
import com.fuzzycraft.fuzzy.event.listeners.Obsidian;
import com.fuzzycraft.fuzzy.event.listeners.EnderdragonPreventPortal;
import com.fuzzycraft.fuzzy.event.listeners.EnderdragonSpawnTimer;

public class EnderdragonRespawner extends JavaPlugin {
  private EnderdragonRespawner plugin = this;
  private ConfigTree ct;

  public void onEnable() {
    // Check Directory Structure
    this.plugin.getLogger().log(Level.INFO, "Checking Directory Structure");
    ct = new ConfigTree(this);
    try {
      ct.createDirectoryStructure();
    } catch (IOException e) {
      e.printStackTrace();
    }

    getCommand(Cmd.START).setExecutor(new Start(this));
    getCommand(Cmd.STOP).setExecutor(new Stop(this));

    new BukkitRunnable() {
      public void run() {
        for (File worldsFile : ct.getWorldsDirectory().listFiles()) {
          if (worldsFile.isDirectory()) {
            World world = plugin.getServer().getWorld(worldsFile.getName());

            if (world != null) {
              for (File file : worldsFile.listFiles()) {
                if (file.getName().equals(Name.YML_CONFIG) && file.isFile()) {
                  Config c = new Config(plugin, world);

                  if (c.getActive()) {
                    EnderdragonCrystals ec = null;
                    Obsidian o = null;

                    plugin.getLogger().log(
                        Level.INFO, "Activating Configuration For World: " +
                                        worldsFile.getName());
                    PluginManager pm = getServer().getPluginManager();
                    Enderdragon es = new Enderdragon(plugin);

                    if (c.getRespawnCrystals()) {
                      plugin.getLogger().log(
                          Level.INFO,
                          "Activating Enderdragon Crystal Respawn For World: " +
                              worldsFile.getName());
                      ec = new EnderdragonCrystals(plugin, c.getWorld());
                      pm.registerEvents(ec, plugin);
                    }

                    if (c.getRespawnObsidian()) {
                      plugin.getLogger().log(
                          Level.INFO,
                          "Activating Obsidian Respawn For World: " +
                              worldsFile.getName());
                      o = new Obsidian(plugin, c.getWorld());
                      pm.registerEvents(o, plugin);
                    }

                    plugin.getLogger().log(Level.INFO,
                                           "Activating Listeners For World: " +
                                               worldsFile.getName());
                    pm.registerEvents(
                        new EnderdragonSpawnTimer(plugin, es, ec, o), plugin);
                    pm.registerEvents(new EnderdragonPreventPortal(plugin, c),
                                      plugin);
                  }
                }
              }
            } else {
              plugin.getLogger().log(Level.WARNING, "World " +
                                                        worldsFile.getName() +
                                                        " Does Not Exist");
            }
          }
        }
      }

    }
        .runTaskLater(this, 1);
  }

  public void onDisable() {}
}
