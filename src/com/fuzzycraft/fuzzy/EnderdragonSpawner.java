package com.fuzzycraft.fuzzy;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class EnderdragonSpawner {

	public EnderdragonRespawner plugin;
	private World world;
	private Location location;
	private int amount;
	private String msg;
	
	/**
	 * Insert which world and location to spawn Enderdragon and which message to produce.
	 * @param plugin
	 * @param world
	 * @param location
	 * @param msg
	 */
	public EnderdragonSpawner(EnderdragonRespawner plugin, World world, Location location, int amount, String msg) {
		this.plugin = plugin;
		this.world = world;
		this.location = location;
		this.amount = amount;
		this.msg = msg;
	}
	
	/**
	 * Spawn Enderdragon and broadcast message.
	 */
	public void spawnEnderdragon() {
		for (int i=0; i<this.amount; i++) {
			this.world.spawnEntity(this.location, EntityType.ENDER_DRAGON);
		}
		
		this.plugin.getServer().broadcastMessage(ChatColor.DARK_RED + this.msg);
	}
	
	/**
	 * Return current world.
	 * @return
	 */
	public World world() {
		return this.world;
	}
	
	/**
	 * Return current location.
	 * @return
	 */
	public Location location() {
		return this.location;
	}
	
	/**
	 * Return current amount.
	 * @return
	 */
	public int amount() {
		return this.amount;
	}
	
	/**
	 * Return current message.
	 * @return
	 */
	public String msg() {
		return this.msg;
	}
}
