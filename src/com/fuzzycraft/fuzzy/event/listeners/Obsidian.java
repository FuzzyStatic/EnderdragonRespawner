package com.fuzzycraft.fuzzy.event.listeners;

import com.fuzzycraft.fuzzy.event.Management;
import com.fuzzycraft.fuzzy.event.files.ConfigTree;
import com.fuzzycraft.fuzzy.event.files.Name;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
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
  private List<Location> list = new ArrayList<Location>();

  public Obsidian(JavaPlugin plugin) { this.plugin = plugin; }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onBlockBreak(BlockBreakEvent event) throws FileNotFoundException {
    final Block block = event.getBlock();

    if (block.getType() == Material.OBSIDIAN &&
        Management.isEventActive(block.getWorld())) {
      list.add(block.getLocation());

      ConfigTree ct = new ConfigTree(this.plugin);
      String filename = ct.getWorldsDirectory().toString() + File.separator +
                        block.getWorld().getName() + File.separator +
                        Name.DIR_DATA + File.separator + Name.DAT_OBSIDIAN;

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
    plugin.getLogger().log(Level.INFO, "Obsidian at: " + in.readLine());

    /*if (!this.list.isEmpty()) {
      for (Location loc : this.list) {
        Block block = loc.getBlock();

        // Check if block is already obsidian.
        if (block.getType() == Material.OBSIDIAN) {
          return;
        }

        block.setType(Material.OBSIDIAN);
      }
    }*/

    in.close();
  }

  public List<Location> getLocations() { return this.list; }
}