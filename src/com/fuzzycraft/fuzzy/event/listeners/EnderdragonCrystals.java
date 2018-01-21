/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-18 10:10:39
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-20 22:41:45
 */

package com.fuzzycraft.fuzzy.event.listeners;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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
import com.fuzzycraft.fuzzy.event.files.ConfigTree;
import com.fuzzycraft.fuzzy.event.files.Name;

public class EnderdragonCrystals implements Listener {

  public JavaPlugin plugin;
  private ConfigTree ct;
  private World world;
  private List<Location> list = new ArrayList<Location>();
  private String filename;

  public EnderdragonCrystals(JavaPlugin plugin, World world) {
    this.plugin = plugin;
    this.ct = new ConfigTree(this.plugin);
    this.world = world;
    this.filename = this.ct.getWorldsDirectory().toString() + File.separator +
                    this.world.getName() + File.separator + Name.DIR_DATA +
                    File.separator + Name.DAT_CRYSTAL;
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onEntityDamage(EntityDamageEvent event)
      throws FileNotFoundException {
    Entity entity = event.getEntity();

    if (entity instanceof EnderCrystal &&
        entity.getWorld().equals(this.world)) {
      list.add(entity.getLocation());

      PrintWriter pw = new PrintWriter(new FileOutputStream(filename));
      for (Location location : list)
        pw.println(location);
      pw.close();
    }
  }

  public void respawn() throws IOException {
    BufferedReader in = new BufferedReader(new FileReader(this.filename));
    plugin.getLogger().log(Level.INFO, "Crystal at: " + in.readLine());

    if (!this.list.isEmpty()) {
      for (Location loc : this.list) {
        Block block = loc.getBlock();
        block.setType(Material.AIR);
        this.world.spawnEntity(loc, EntityType.ENDER_CRYSTAL);
      }
    }

    in.close();
  }

  public List<Location> getLocations() { return this.list; }

  public World world() { return this.world; }
}
