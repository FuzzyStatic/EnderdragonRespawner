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
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Obsidian implements Listener {
  public JavaPlugin plugin;

  public Obsidian(JavaPlugin plugin) { this.plugin = plugin; }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onBlockBreak(BlockBreakEvent event) throws FileNotFoundException {
    final Block block = event.getBlock();
    final World w = block.getWorld();

    if (block.getType() == Material.OBSIDIAN && Management.isActive(w)) {
      add(plugin, w, block.getLocation());
    }
  }

  public static void add(JavaPlugin plugin, World w, Location l) {
    ConfigTree.addLocationToFile(ConfigTree.createObsidianDataFile(plugin, w),
                                 l);
  }

  public static void respawn(JavaPlugin plugin, World w) {
    String filename = ConfigTree.createObsidianDataFile(plugin, w);

    try {
      BufferedReader br = new BufferedReader(new FileReader(filename));
      String line;

      while ((line = br.readLine()) != null) {
        Location l = SerializableLocation.deserializeString(plugin, line);
        Block b = l.getBlock();

        // Check if block is already obsidian.
        if (!(b.getType() == Material.OBSIDIAN)) {
          b.setType(Material.OBSIDIAN);
        }
      }

      new File(filename).delete();
      br.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}