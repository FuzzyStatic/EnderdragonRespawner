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
	private EnderdragonChecker edc;
	private EnderdragonCrystals ecl;

	/**
	 * Constructs listener for EnderdragonSpawnTimer.
	 * @param plugin
	 * @param es
	 * @param ec
	 * @param respawnTime
	 */
	public EnderdragonSpawnTimer(EnderdragonRespawner plugin, EnderdragonSpawner es, EnderdragonChecker ec, EnderdragonCrystals ecl, int respawnTime) {
		this.plugin = plugin;
		this.es = es;
		this.edc = ec;
		this.ecl = ecl;
		this.respawnTime = respawnTime;
	}
		
	/**
	 * Checks for death of Enderdragon in specified world. If Enderdragon does not exist, start a respawn timer for specified time.
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityDeath(EntityDeathEvent event) {
		if(!(event.getEntity() instanceof EnderDragon) || this.edc.exists()) {
			return;
		}
		
		//Testing
		System.out.println(this.ecl.getCrystalLocations());
		System.out.println(this.ecl.world());
		
		// Create the task anonymously to spawn Enderdragon and schedule to run it once after specified time.
		new BukkitRunnable() {
	        	
			@Override
			public void run() {
				es.spawnEnderdragon();
			}
			
		}.runTaskLater(this.plugin, this.respawnTime);
	}
}
