package com.fuzzycraft.fuzzy.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.fuzzycraft.fuzzy.EnderdragonRespawner;

public class EnderdragonCrystals implements Listener {
	
	public EnderdragonRespawner plugin;
	private World world;
	private List<Location> list = new ArrayList<Location>();
	
	/**
	 * Insert which world to check for EnderCrystals.
	 * @param plugin
	 * @param world
	 */
	public EnderdragonCrystals(EnderdragonRespawner plugin, World world) {
		this.plugin = plugin;
		this.world = world;
	}
	
	/**
	 * Checks for damage to EnderCrystals in specified world.
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		
		if(entity instanceof EnderCrystal) {
			list.add(entity.getLocation());
		}
	}
	
	public List<Location> getCrystalLocations() {
		return this.list;
	}
	
	/**
	 * Return current world.
	 * @return
	 */
	public World world() {
		return this.world;
	}
}
