package com.fuzzycraft.fuzzy;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.fuzzycraft.fuzzy.listeners.EnderdragonSpawnTimer;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class EnderdragonRespawner extends JavaPlugin {
	
	private EnderdragonSpawnTimer ect;
	
	public void onEnable() {
		World world = getServer().getWorld("world_the_end");
		Location location = new Location(world, 0, 20, 0);
		int time = 432000; // 6 hours of ticks: 20 ticks/seconds * 21600 seconds (6 hours)
		String msg = "The beasts awakens from his slumber...";
		ect = new EnderdragonSpawnTimer(this, world, world, location, time, msg);
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(ect, this);
		
		// Checks for existence of Enderdragon in specified world on load. If Enderdragon does not exist, spawn dragon.
		EnderdragonChecker edc = new EnderdragonChecker(world);
		
		if (!edc.exists()) {
			world.spawnEntity(location, EntityType.ENDER_DRAGON);
            getServer().broadcastMessage(ChatColor.DARK_RED + msg);
		}
	}		
}
