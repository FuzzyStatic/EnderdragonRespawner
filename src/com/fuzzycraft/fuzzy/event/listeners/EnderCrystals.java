/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-18 10:10:39
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-02-03 20:54:55
 */

package com.fuzzycraft.fuzzy.event.listeners;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.fuzzycraft.fuzzy.event.Management;
import com.fuzzycraft.fuzzy.event.files.ConfigTree;
import com.fuzzycraft.fuzzy.utilities.SerializableLocation;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EnderCrystals implements Listener {
  public JavaPlugin plugin;

  public EnderCrystals(JavaPlugin plugin) { this.plugin = plugin; }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onEntityDamage(EntityDamageEvent event)
      throws FileNotFoundException {
    final Entity entity = event.getEntity();
    final World w = entity.getWorld();

    if (entity instanceof EnderCrystal && Management.isActive(w)) {
      add(plugin, w, entity.getLocation());
    }
  }

  public static void add(JavaPlugin plugin, World w, Location l) {
    ConfigTree.addLocationToFile(ConfigTree.createCrystalDataFile(plugin, w),
                                 l);
  }

  public static void respawn(JavaPlugin plugin, World w) {
    String filename = ConfigTree.createCrystalDataFile(plugin, w);

    try {
      BufferedReader br = new BufferedReader(new FileReader(filename));
      String line;

      while ((line = br.readLine()) != null) {
        Location l = SerializableLocation.deserializeString(plugin, line);
        Block b = l.getBlock();
        b.setType(Material.AIR);
        w.spawnEntity(l, EntityType.ENDER_CRYSTAL);
      }

      new File(filename).delete();
      br.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
