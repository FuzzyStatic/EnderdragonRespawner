package com.fuzzycraft.fuzzy.event.listeners;

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
  private ConfigTree ct;
  private World world;
  private List<Location> list = new ArrayList<Location>();
  private String filename;

  public Obsidian(JavaPlugin plugin, World world) {
    this.plugin = plugin;
    this.ct = new ConfigTree(this.plugin);
    this.world = world;
    this.filename = this.ct.getWorldsDirectory().toString() + File.separator +
                    this.world.getName() + File.separator + Name.DIR_DATA +
                    File.separator + Name.DAT_OBSIDIAN;
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onBlockBreak(BlockBreakEvent event) throws FileNotFoundException {
    Block block = event.getBlock();

    if (block.getType() == Material.OBSIDIAN &&
        block.getWorld().equals(this.world)) {
      list.add(block.getLocation());

      PrintWriter pw = new PrintWriter(new FileOutputStream(filename));
      for (Location location : list)
        pw.println(location);
      pw.close();
    }
  }

  public void respawn() throws IOException {
    BufferedReader in = new BufferedReader(new FileReader(this.filename));
    plugin.getLogger().log(Level.INFO, "Obsidian at: " + in.readLine());

    if (!this.list.isEmpty()) {
      for (Location loc : this.list) {
        Block block = loc.getBlock();

        // Check if block is already obsidian.
        if (block.getType() == Material.OBSIDIAN) {
          return;
        }

        block.setType(Material.OBSIDIAN);
      }
    }
  }

  public List<Location> getLocations() { return this.list; }

  public World world() { return this.world; }
}