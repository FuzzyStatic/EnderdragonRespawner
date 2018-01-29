/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-20 21:02:33
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-28 21:38:30
 */

package com.fuzzycraft.fuzzy;

import com.fuzzycraft.fuzzy.event.Management;
import com.fuzzycraft.fuzzy.event.Structure;
import com.fuzzycraft.fuzzy.event.commands.Cmd;
import com.fuzzycraft.fuzzy.event.commands.Start;
import com.fuzzycraft.fuzzy.event.commands.Stop;
import com.fuzzycraft.fuzzy.event.files.ConfigTree;
import com.fuzzycraft.fuzzy.event.files.Name;
import com.fuzzycraft.fuzzy.event.listeners.EnderdragonCrystals;
import com.fuzzycraft.fuzzy.event.listeners.Obsidian;
import com.fuzzycraft.fuzzy.event.listeners.EnderdragonPreventPortal;
import com.fuzzycraft.fuzzy.event.listeners.EnderdragonSpawnTimer;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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
    ct.createWorldSampleConfig();
    this.plugin.getLogger().log(Level.INFO,
                                "View sample configuration at " +
                                    ct.getWorldsDirectory().toString());
    this.plugin.getLogger().log(
        Level.INFO,
        "Configurations will only work if they are in a specific world directory within " +
            ct.getWorldsDirectory().toString());

    // Set Commands
    getCommand(Cmd.START).setExecutor(new Start(this));
    getCommand(Cmd.STOP).setExecutor(new Stop(this));

    new BukkitRunnable() {
      public void run() {
        HashMap<World, File> hm = ct.getWorldDirectories();

        for (World w : hm.keySet()) {
          File file = hm.get(w);

          if (file.getName().equals(Name.YML_CONFIG) && file.isFile()) {
            Management m = new Management(plugin, w);
          }
        }

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new EnderdragonCrystals(plugin), plugin);
        pm.registerEvents(new Obsidian(plugin), plugin);
        pm.registerEvents(new EnderdragonSpawnTimer(plugin), plugin);
        pm.registerEvents(new EnderdragonPreventPortal(plugin), plugin);
      }
    }
        .runTaskLater(this, 1);
  }

  public void onDisable() {}
}
