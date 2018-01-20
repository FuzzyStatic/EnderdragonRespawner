package com.fuzzycraft.fuzzy;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzzycraft.fuzzy.utilities.ConfigParameters;
import com.fuzzycraft.fuzzy.event.Enderdragon;
import com.fuzzycraft.fuzzy.event.commands.Cmd;
import com.fuzzycraft.fuzzy.event.commands.Start;
import com.fuzzycraft.fuzzy.event.commands.Stop;
import com.fuzzycraft.fuzzy.event.listeners.EnderdragonCrystals;
import com.fuzzycraft.fuzzy.event.listeners.Obsidian;
import com.fuzzycraft.fuzzy.event.listeners.EnderdragonPreventPortal;
import com.fuzzycraft.fuzzy.event.listeners.EnderdragonSpawnTimer;
import com.fuzzycraft.fuzzy.utilities.*;

/**
 * @author Allen Flickinger (allen.flickinger@gmail.com)
 */

public class EnderdragonRespawner extends JavaPlugin {
  private EnderdragonRespawner plugin = this;

  public void onEnable() {
    // Check Directory Structure
    this.plugin.getLogger().log(Level.INFO, "Checking Directory Structure");
    DirectoryStructure ds = new DirectoryStructure(this);
    try {
      ds.createDirectoryStructure();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Create Default Configurations
    ds.createWorldDefaultConfigurations();

    getCommand(Cmd.START).setExecutor(new Start(this));
    getCommand(Cmd.STOP).setExecutor(new Stop(this));

    new BukkitRunnable() {
      public void run() {
        for (File worldsFile :
             DirectoryStructure.getWorldsDirectory().listFiles()) {
          if (worldsFile.isDirectory()) {
            World world = plugin.getServer().getWorld(worldsFile.getName());

            if (world != null) {
              for (File file : worldsFile.listFiles()) {
                if (file.getName().equals(DirectoryStructure.FILE_CONFIG) &&
                    file.isFile()) {
                  ConfigParameters cp =
                      new ConfigParameters(plugin, world, file.toString());

                  if (cp.getActive()) {
                    EnderdragonCrystals ec = null;
                    Obsidian o = null;

                    plugin.getLogger().log(
                        Level.INFO, "Activating Configuration For World: " +
                                        worldsFile.getName());
                    PluginManager pm = getServer().getPluginManager();
                    Enderdragon es = new Enderdragon(plugin);

                    if (cp.getRespawnCrystals()) {
                      plugin.getLogger().log(
                          Level.INFO,
                          "Activating Enderdragon Crystal Respawn For World: " +
                              worldsFile.getName());
                      ec = new EnderdragonCrystals(plugin, cp.getWorld());
                      pm.registerEvents(ec, plugin);
                    }

                    if (cp.getRespawnObsidian()) {
                      plugin.getLogger().log(
                          Level.INFO,
                          "Activating Obsidian Respawn For World: " +
                              worldsFile.getName());
                      o = new Obsidian(plugin, cp.getWorld());
                      pm.registerEvents(o, plugin);
                    }

                    plugin.getLogger().log(Level.INFO,
                                           "Activating Listeners For World: " +
                                               worldsFile.getName());
                    pm.registerEvents(
                        new EnderdragonSpawnTimer(plugin, es, ec, o), plugin);
                    pm.registerEvents(new EnderdragonPreventPortal(plugin, cp),
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
