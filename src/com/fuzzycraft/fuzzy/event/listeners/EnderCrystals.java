/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-18 10:10:39
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-29 12:33:59
 */

package com.fuzzycraft.fuzzy.event.listeners;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.fuzzycraft.fuzzy.event.Management;
import com.fuzzycraft.fuzzy.event.files.ConfigTree;
import com.fuzzycraft.fuzzy.event.files.Name;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EnderCrystals implements Listener {
  public JavaPlugin plugin;
  private List<Location> list = new ArrayList<Location>();

  public EnderCrystals(JavaPlugin plugin) { this.plugin = plugin; }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onEntityDamage(EntityDamageEvent event)
      throws FileNotFoundException {
    final Entity entity = event.getEntity();

    if (entity instanceof EnderCrystal &&
        Management.isEventActive(entity.getWorld())) {
      list.add(entity.getLocation());

      ConfigTree ct = new ConfigTree(this.plugin);
      String filename = ct.getWorldsDirectory().toString() + File.separator +
                        entity.getWorld().getName() + File.separator +
                        Name.DIR_DATA + File.separator + Name.DAT_CRYSTAL;

      PrintWriter pw = new PrintWriter(new FileOutputStream(filename));
      for (Location location : list)
        pw.println(location);
      pw.close();
    }
  }

  public static void respawn(JavaPlugin plugin, World w) throws IOException {
    ConfigTree ct = new ConfigTree(plugin);
    String filename = ct.getWorldsDirectory().toString() + File.separator +
                      w.getName() + File.separator + Name.DIR_DATA +
                      File.separator + Name.DAT_CRYSTAL;
    BufferedReader in = new BufferedReader(new FileReader(filename));
    plugin.getLogger().log(Level.INFO, "Crystal at: " + in.readLine());

    /* if (!this.list.isEmpty()) {
       for (Location loc : this.list) {
         Block block = loc.getBlock();
         block.setType(Material.AIR);
         w.spawnEntity(loc, EntityType.ENDER_CRYSTAL);
       }
     }*/

    in.close();
  }

  public List<Location> getLocations() { return this.list; }
}
