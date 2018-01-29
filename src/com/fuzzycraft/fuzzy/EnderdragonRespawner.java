/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-20 21:02:33
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-29 17:17:39
 */

package com.fuzzycraft.fuzzy;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

import com.fuzzycraft.fuzzy.event.Management;
import com.fuzzycraft.fuzzy.event.commands.Cmd;
import com.fuzzycraft.fuzzy.event.commands.Start;
import com.fuzzycraft.fuzzy.event.commands.Stop;
import com.fuzzycraft.fuzzy.event.files.ConfigTree;
import com.fuzzycraft.fuzzy.event.listeners.EnderCrystals;
import com.fuzzycraft.fuzzy.event.listeners.EnderdragonPreventPortal;
import com.fuzzycraft.fuzzy.event.listeners.EnderdragonSpawnTimer;
import com.fuzzycraft.fuzzy.event.listeners.Obsidian;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class EnderdragonRespawner extends JavaPlugin {
  private EnderdragonRespawner plugin = this;
  private ConfigTree ct;

  public void onEnable() {
    // Check Directory Structure
    this.plugin.getLogger().log(Level.INFO, "Checking directory structure");

    ct = new ConfigTree(this);

    try {
      ct.createDirectoryStructure();
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.plugin.getLogger().log(
        Level.INFO,
        "Configurations will only work if they are in a specific world directory within " +
            ct.getWorldsDirectory().toString());

    ct.createAllWorldsDefaultConfiguration();

    // Set Commands
    getCommand(Cmd.START).setExecutor(new Start(this));
    getCommand(Cmd.STOP).setExecutor(new Stop(this));
    new Management();

    new BukkitRunnable() {
      public void run() {
        plugin.getLogger().log(Level.INFO,
                               "Registering listener events " +
                                   ct.getWorldsDirectory().toString());
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
