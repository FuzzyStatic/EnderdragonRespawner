package com.fuzzycraft.fuzzy;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzzycraft.fuzzy.configurations.ConfigWorldParameters;
import com.fuzzycraft.fuzzy.constants.Defaults;
import com.fuzzycraft.fuzzy.constants.Paths;
import com.fuzzycraft.fuzzy.listeners.EnderdragonCrystals;
import com.fuzzycraft.fuzzy.listeners.EnderdragonPreventPortal;
import com.fuzzycraft.fuzzy.listeners.EnderdragonSpawnTimer;
import com.fuzzycraft.fuzzy.listeners.Obsidian;
import com.fuzzycraft.fuzzy.utilities.DirectoryStructure;
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
	
	private EnderdragonRespawner plugin = this;
	
	private ConfigWorldParameters defaultConfig;
	private DirectoryStructure ds;
	private String defaultDirectory = Defaults.DIR_WORLDS;
	private String defaultFilename = Defaults.WORLD + ".yml";
	
	private EnderdragonCrystals enderCrystals;
	private Obsidian obsidian;
	
	public void onEnable() {
		// Create defaults
		this.ds = new DirectoryStructure(this, this.defaultDirectory);
		this.ds.createDirectory();
		this.defaultConfig = new ConfigWorldParameters(this, this.defaultDirectory, new File(this.defaultFilename));
		this.defaultConfig.setDefaults();
		
		new BukkitRunnable() {
        	
			public void run() {
				for (File file : ds.getDirectory().listFiles()) {
					ConfigWorldParameters cwp = new ConfigWorldParameters(plugin, defaultDirectory, file);
					
					if (cwp.getLocation().getWorld() != null) {
						SerializableLocation sc = new SerializableLocation(new YamlLocation(getConfig(), (Paths.LOCATION)).getLocationMap());
					}
				}
			}
			
		}.runTaskLater(this, 1);
				
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
	
	public void onDisable() {
	
	}
	
	public void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		
		if (getConfig().getBoolean(Paths.RESPAWN_CRYSTALS)) {
			this.enderCrystals = new EnderdragonCrystals(this, ec.getWorld());
			pm.registerEvents(this.enderCrystals, this);
		}
		
		if (getConfig().getBoolean(Paths.RESPAWN_OBSIDIAN)) {
			this.obsidian = new Obsidian(this, ec.getWorld());
			pm.registerEvents(this.obsidian, this);
		}
		
		pm.registerEvents(new EnderdragonSpawnTimer(this, this.enderCrystals, this.obsidian, getConfig().getInt(Paths.TIME)), this);
		pm.registerEvents(new EnderdragonPreventPortal(this, ec.getWorld(), getConfig().getBoolean(Paths.CREATE_PORTAL), getConfig().getBoolean(Paths.CREATE_EGG)), this);
		
	}
}
