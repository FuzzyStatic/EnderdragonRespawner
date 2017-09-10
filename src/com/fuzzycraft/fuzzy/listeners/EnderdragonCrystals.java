package com.fuzzycraft.fuzzy.listeners;

import java.util.ArrayList;
import java.util.List;

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

/**
 * @author Allen Flickinger (allen.flickinger@gmail.com)
 */

public class EnderdragonCrystals implements Listener {

    public JavaPlugin plugin;
    private World world;
    private List<Location> list = new ArrayList<Location>();

    public EnderdragonCrystals(JavaPlugin plugin, World world) {
        this.plugin = plugin;
        this.world = world;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof EnderCrystal && entity.getWorld().equals(this.world)) {
            list.add(entity.getLocation());
        }
    }

    public void respawn() {
        if (!this.list.isEmpty()) {
            for (Location loc : this.list) {
                Block block = loc.getBlock();
                block.setType(Material.AIR);
                this.world.spawnEntity(loc, EntityType.ENDER_CRYSTAL);
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
