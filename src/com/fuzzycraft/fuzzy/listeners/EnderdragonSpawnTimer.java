package com.fuzzycraft.fuzzy.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzzycraft.fuzzy.EnderdragonChecker;
import com.fuzzycraft.fuzzy.EnderdragonRespawner;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class EnderdragonSpawnTimer implements Listener {

	public EnderdragonRespawner plugin;
	private EnderdragonChecker edc;
	private World spawnWorld;
	private Location location;
	private int time;
	private String msg;
    
	/**
	 * Creates listener for EnderdragonSpawnTimer.
	 * @param plugin
	 * @param world
	 */
	public EnderdragonSpawnTimer(EnderdragonRespawner plugin, World checkWorld, World spawnWorld, Location location, int time, String msg) {
		this.plugin = plugin;
		edc = new EnderdragonChecker(checkWorld);
		this.spawnWorld = spawnWorld;
		this.location = location;
		this.time = time;
		this.msg = msg;
	}
		
	/**
	 * Checks for death of Enderdragon in specified world. If Enderdragon does not exist, start a respawn timer for specified time.
	 * @param event
	 */
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof EnderDragon && !edc.exists()) {
			// Create the task anonymously to spawn Enderdragon and schedule to run it once after specified time.
	        new BukkitRunnable() {
	        	
				@Override
	            public void run() {
					spawnWorld.spawn(location, EnderDragon.class);
	                plugin.getServer().broadcastMessage(ChatColor.DARK_RED + msg);
	            }
	 
	        }.runTaskLater(this.plugin, this.time);
	    }
	}
}
