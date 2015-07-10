package com.fuzzycraft.fuzzy.listeners;

import org.bukkit.entity.EnderDragon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
	private int respawnTime;
	private EnderdragonSpawner es;
	private EnderdragonChecker ec;
	private EnderdragonCrystals enderCrystals;
	private Obsidian obsidian;

	/**
	 * Constructs listener for EnderdragonSpawnTimer.
	 * @param plugin
	 * @param es
	 * @param ec
	 * @param o 
	 * @param respawnTime
	 */
	public EnderdragonSpawnTimer(EnderdragonRespawner plugin, EnderdragonSpawner es, EnderdragonChecker ec, EnderdragonCrystals enderCrystals, Obsidian obsidian, int respawnTime) {
		this.plugin = plugin;
		this.es = es;
		this.ec = ec;
		this.enderCrystals = enderCrystals;
		this.obsidian = obsidian;
		this.respawnTime = respawnTime;
	}
		
	/**
	 * Checks for death of Enderdragon in specified world. If Enderdragon does not exist, start a respawn timer for specified time.
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityDeath(EntityDeathEvent event) {
		if(!(event.getEntity() instanceof EnderDragon) || this.ec.exists()) {
			return;
		}
		
		// Locations currently in memory. Respawn Ender Crystals and obsidian now just in case of server shutdown.
		this.enderCrystals.respawn();
		this.obsidian.respawn();
		
		// Create the task anonymously to spawn Enderdragon and schedule to run it once after specified time.
		new BukkitRunnable() {
	        	
			@Override
			public void run() {
				es.spawnEnderdragon();
			}
			
		}.runTaskLater(this.plugin, this.respawnTime * 20);
	}
}
