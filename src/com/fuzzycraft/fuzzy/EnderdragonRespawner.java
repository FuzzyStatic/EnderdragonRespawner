package com.fuzzycraft.fuzzy;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.fuzzycraft.fuzzy.listeners.EnderdragonSpawnTimer;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class EnderdragonRespawner extends JavaPlugin {
		
	private final World world = getServer().getWorld("world_the_end");
	private final Location location = new Location(world, 0, 100, 0);
	private final int time = 43200; // 6 hours of ticks: 20 ticks/seconds * 21600 seconds (6 hours)
	private final String msg = "The beasts awakens from his slumber...";
	private final EnderdragonSpawnTimer ect = new EnderdragonSpawnTimer(this, world, world, location, time, msg);
	
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this.ect, this);
	}
}
