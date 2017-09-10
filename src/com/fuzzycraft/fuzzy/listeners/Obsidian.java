package com.fuzzycraft.fuzzy.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.fuzzycraft.fuzzy.EnderdragonRespawner;

/**
 * @author Allen Flickinger (allen.flickinger@gmail.com)
 */

public class Obsidian implements Listener {

    public EnderdragonRespawner plugin;
    private World world;
    private List<Location> list = new ArrayList<Location>();

    public Obsidian(EnderdragonRespawner plugin, World world) {
        this.plugin = plugin;
        this.world = world;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (block.getType() == Material.OBSIDIAN && block.getWorld().equals(this.world)) {
            list.add(block.getLocation());
        }
    }

    public void respawn() {
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

    public List<Location> getLocations() {
        return this.list;
    }

    public World world() {
        return this.world;
    }
}