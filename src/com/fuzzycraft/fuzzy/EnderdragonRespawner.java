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
	
	private EnderdragonSpawner es;
	private EnderdragonChecker ec;
	private EnderdragonSpawnTimer est;
	private EnderdragonPreventPortal epp;
	private World world;
	private Location location;
	
	public void onEnable() {
		world = getServer().getWorld(Constants.WORLD);
		location = new Location(world, Constants.X, Constants.Y, Constants.Z);
				
		// Create our object instances
		es = new EnderdragonSpawner(this, world, location, Constants.MSG);
		ec = new EnderdragonChecker(world);
		
		// Create listener instances
		est = new EnderdragonSpawnTimer(this, es, ec, Constants.TIME_RESPAWN);
		epp = new EnderdragonPreventPortal(this, ec.world(), Constants.CREATE_PORTAL, Constants.CREATE_EGG);

		// Register listeners
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(est, this);
		pm.registerEvents(epp, this);
		
		// Checks for existence of Enderdragon in specified world on load. If Enderdragon does not exist, spawn dragon.		
		if (!ec.exists()) {
			es.spawnEnderdragon();
		}
	}		
}
