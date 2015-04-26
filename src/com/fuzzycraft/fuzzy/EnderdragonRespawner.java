package com.fuzzycraft.fuzzy;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.fuzzycraft.fuzzy.listeners.EnderdragonPreventPortal;
import com.fuzzycraft.fuzzy.listeners.EnderdragonSpawnTimer;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class EnderdragonRespawner extends JavaPlugin {
	
	private EnderdragonSpawnTimer ect;
	private EnderdragonPreventPortal epp;
	
	public void onEnable() {
		World world = getServer().getWorld("world_the_end");
		Location location = new Location(world, 0, 20, 0);
				
		// Create listener instances
		ect = new EnderdragonSpawnTimer(this, world, world, location, Constants.TIME_RESPAWN, Constants.MSG);
		epp = new EnderdragonPreventPortal(this, world, Constants.CREATE_PORTAL, Constants.CREATE_EGG);

		// Register Listeners
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(ect, this);
		pm.registerEvents(epp, this);
		
		// Checks for existence of Enderdragon in specified world on load. If Enderdragon does not exist, spawn dragon.
		EnderdragonChecker edc = new EnderdragonChecker(world);
		
		if (!edc.exists()) {
			new EnderdragonSpawner(this, world, location, Constants.MSG).spawnEnderdragon();
		}
	}		
}
