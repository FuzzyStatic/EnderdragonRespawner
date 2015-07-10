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
import org.bukkit.event.block.BlockDamageEvent;

import com.fuzzycraft.fuzzy.EnderdragonRespawner;

public class Obsidian implements Listener {
	
	public EnderdragonRespawner plugin;
	private World world;
	private List<Location> list = new ArrayList<Location>();
	
	/**
	 * Insert which world to check for EnderCrystals.
	 * @param plugin
	 * @param world
	 */
	public Obsidian(EnderdragonRespawner plugin, World world) {
		this.plugin = plugin;
		this.world = world;
	}
	
	/**
	 * Checks for damage to Obsidian in specified world.
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockDamage(BlockDamageEvent event) {
		Block block = event.getBlock();
		
		if(block.getType() == Material.OBSIDIAN) {
			list.add(block.getLocation());
		}
	}
	
	/**
	 * Respawn destroyed obsidian.
	 */
	public void respawn() {
		if (this.list != null) {
			for (Location loc : this.list) {
				Block block = loc.getBlock();
				
				// Check if block is already obisidian.
				if(block.getType() == Material.OBSIDIAN) {
					return;
				}
				
	            block.setType(Material.OBSIDIAN);
			}
		}
	}
	
	/**
	 * Return crystal locations.
	 * @return
	 */
	public List<Location> getLocations() {
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