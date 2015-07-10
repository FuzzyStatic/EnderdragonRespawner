package com.fuzzycraft.fuzzy;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.fuzzycraft.fuzzy.constants.Defaults;
import com.fuzzycraft.fuzzy.constants.Paths;
import com.fuzzycraft.fuzzy.listeners.EnderdragonCrystals;
import com.fuzzycraft.fuzzy.listeners.EnderdragonPreventPortal;
import com.fuzzycraft.fuzzy.listeners.EnderdragonSpawnTimer;
import com.fuzzycraft.fuzzy.listeners.Obsidian;
import com.fuzzycraft.fuzzy.utilities.SerializableLocation;
import com.fuzzycraft.fuzzy.utilities.YamlLocation;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class EnderdragonRespawner extends JavaPlugin {
	
	public static EnderdragonSpawner es;
	public static EnderdragonChecker ec;
	
	private EnderdragonCrystals enderCrystals;
	private Obsidian obsidian;
	private World world;
	private Location location;
	
	public void onEnable() {
		this.world = getServer().getWorld(Defaults.WORLD);
		this.location = new Location(this.world, Defaults.X, Defaults.Y, Defaults.Z);
		 
		configDefaults();
		
		SerializableLocation sc = new SerializableLocation(this.location);
		
		// Get location from configuration if exists.
		if (new YamlLocation(getConfig(), (Paths.LOCATION)).getLocationMap().get("world") != null) {
			sc = new SerializableLocation(new YamlLocation(getConfig(), (Paths.LOCATION)).getLocationMap());
		}
		
		// Check to see if world exists.
		if (sc.getWorld() != null) {
			es = new EnderdragonSpawner(this, sc.getWorld(), sc.getLocation(), getConfig().getInt(Paths.AMOUNT), getConfig().getString(Paths.MSG));
			ec = new EnderdragonChecker(sc.getWorld());
			registerListeners();
			
			// Checks for existence of Enderdragon(s) in specified world on load. If Enderdragon(s) do not exist, spawn dragon.		
			if (!ec.exists()) {
				es.spawnEnderdragon();
			}
		} else {
			Bukkit.getLogger().warning("Configured world does not exist. Please modify the config.yml.");
		}
	}
	
	public void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		
		if (getConfig().getBoolean(Paths.RESPAWN_CRYSTALS)) {
			this.enderCrystals = new EnderdragonCrystals(this, ec.getWorld());
		}
		
		if (getConfig().getBoolean(Paths.RESPAWN_OBSIDIAN)) {
			this.obsidian = new Obsidian(this, ec.getWorld());
		}
		
		pm.registerEvents(this.enderCrystals, this);
		pm.registerEvents(this.obsidian, this);
		pm.registerEvents(new EnderdragonSpawnTimer(this, this.enderCrystals, this.obsidian, getConfig().getInt(Paths.TIME)), this);
		pm.registerEvents(new EnderdragonPreventPortal(this, ec.getWorld(), getConfig().getBoolean(Paths.CREATE_PORTAL), getConfig().getBoolean(Paths.CREATE_EGG)), this);
		
	}
	
	public void configDefaults() {
		getDataFolder().mkdir();
		getConfig().addDefault(Paths.LOCATION, new SerializableLocation(this.location).serialize());
		getConfig().addDefault(Paths.AMOUNT, Defaults.AMOUNT);
		getConfig().addDefault(Paths.TIME, Defaults.TIME);
		getConfig().addDefault(Paths.MSG, Defaults.MSG);
		getConfig().addDefault(Paths.RESPAWN_CRYSTALS, Defaults.RESPAWN_CRYSTALS);
		getConfig().addDefault(Paths.RESPAWN_OBSIDIAN, Defaults.RESPAWN_OBSIDIAN);
		getConfig().addDefault(Paths.CREATE_PORTAL, Defaults.CREATE_PORTAL);
		getConfig().addDefault(Paths.CREATE_EGG, Defaults.CREATE_EGG);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
}
