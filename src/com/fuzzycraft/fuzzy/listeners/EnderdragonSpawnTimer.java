package com.fuzzycraft.fuzzy.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzzycraft.fuzzy.EnderdragonChecker;
import com.fuzzycraft.fuzzy.EnderdragonRespawner;
import com.fuzzycraft.fuzzy.EnderdragonSpawner;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class EnderdragonSpawnTimer implements Listener {

	public EnderdragonRespawner plugin;
	private World checkWorld, spawnWorld;
	private Location location;
	private int respawnTime;
	private String msg;
	private EnderdragonChecker edc;

	/**
	 * Constructs listener for EnderdragonSpawnTimer.
	 * @param plugin
	 * @param world
	 */
	public EnderdragonSpawnTimer(EnderdragonRespawner plugin, World checkWorld, World spawnWorld, Location location, int respawnTime, String msg) {
		this.plugin = plugin;
		this.checkWorld = checkWorld;
		this.spawnWorld = spawnWorld;
		this.location = location;
		this.respawnTime = respawnTime;
		this.msg = msg;
		this.edc = new EnderdragonChecker(this.checkWorld);
	}
		
	/**
	 * Checks for death of Enderdragon in specified world. If Enderdragon does not exist, start a respawn timer for specified time.
	 * @param event
	 */
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if(!(event.getEntity() instanceof EnderDragon) && this.edc.exists()) {
			return;
		}
		
		// Create the task anonymously to spawn Enderdragon and schedule to run it once after specified time.
		new BukkitRunnable() {
	        	
			@Override
			public void run() {
				new EnderdragonSpawner(plugin, spawnWorld, location, msg).spawnEnderdragon();
			}
			
		}.runTaskLater(this.plugin, this.respawnTime);
	}
}
