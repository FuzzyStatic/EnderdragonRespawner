package com.fuzzycraft.fuzzy.event.listeners;

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

import com.fuzzycraft.fuzzy.EnderdragonRespawner;
import com.fuzzycraft.fuzzy.event.DirectoryStructure;

/**
 * @author Allen Flickinger (allen.flickinger@gmail.com)
 */

public class Obsidian implements Listener {

  public EnderdragonRespawner plugin;
  private World world;
  private List<Location> list = new ArrayList<Location>();
  private String filename;

  public Obsidian(EnderdragonRespawner plugin, World world) {
    this.plugin = plugin;
    this.world = world;
    this.filename = DirectoryStructure.getWorldsDirectoryPath() +
                    File.separator + this.world.getName() + File.separator +
                    DirectoryStructure.DIR_DATA + File.separator +
                    DirectoryStructure.FILE_OBSIDIAN;
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