package com.fuzzycraft.fuzzy;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.fuzzycraft.fuzzy.constants.Defaults;
import com.fuzzycraft.fuzzy.constants.Paths;
import com.fuzzycraft.fuzzy.listeners.EnderdragonPreventPortal;
import com.fuzzycraft.fuzzy.listeners.EnderdragonSpawnTimer;
import com.fuzzycraft.fuzzy.utilities.SerializableLocation;
import com.fuzzycraft.fuzzy.utilities.YamlLocation;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class EnderdragonRespawner extends JavaPlugin {
	
	private EnderdragonSpawner es;
	private EnderdragonChecker ec;
	private World world;
	private Location location;
	
	public void onEnable() {
		world = getServer().getWorld(Defaults.WORLD);
		location = new Location(world, Defaults.X, Defaults.Y, Defaults.Z);
		 
		configDefaults();
		
		// Get location from configuration.
		SerializableLocation sc = new SerializableLocation(new YamlLocation(getConfig(), (Paths.LOCATION)).getLocationMap());
		
		// Create our object instances.
		es = new EnderdragonSpawner(this, world, sc.getLocation(), getConfig().getString(Paths.MSG));
		ec = new EnderdragonChecker(world);
		
		registerListeners();
		
		// Checks for existence of Enderdragon in specified world on load. If Enderdragon does not exist, spawn dragon.		
		if (!ec.exists()) {
			es.spawnEnderdragon();
		}
	}
	
	public void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new EnderdragonSpawnTimer(this, es, ec, getConfig().getInt(Paths.TIME)), this);
		pm.registerEvents(new EnderdragonPreventPortal(this, ec.world(), getConfig().getBoolean(Paths.CREATE_PORTAL), getConfig().getBoolean(Paths.CREATE_EGG)), this);
	}
	
	public void configDefaults() {
		getDataFolder().mkdir();
		getConfig().addDefault(Paths.LOCATION, new SerializableLocation(location).serialize());
		getConfig().addDefault(Paths.TIME, Defaults.TIME);
		getConfig().addDefault(Paths.MSG, Defaults.MSG);
		getConfig().addDefault(Paths.CREATE_PORTAL, Defaults.CREATE_PORTAL);
		getConfig().addDefault(Paths.CREATE_EGG, Defaults.CREATE_EGG);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
}
