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
						EnderdragonSpawner es = new EnderdragonSpawner(plugin, sc.getWorld(), sc.getLocation(), getConfig().getInt(Paths.AMOUNT), getConfig().getString(Paths.MSG));
						EnderdragonChecker ec = new EnderdragonChecker(sc.getWorld());
						PluginManager pm = getServer().getPluginManager();
						
						if (getConfig().getBoolean(Paths.RESPAWN_CRYSTALS)) {
							EnderdragonCrystals enderCrystals = new EnderdragonCrystals(plugin, ec.getWorld());
							pm.registerEvents(enderCrystals, plugin);
						}
						
						if (getConfig().getBoolean(Paths.RESPAWN_OBSIDIAN)) {
							Obsidian obsidian = new Obsidian(plugin, ec.getWorld());
							pm.registerEvents(obsidian, plugin);
						}
						
						pm.registerEvents(new EnderdragonSpawnTimer(plugin, enderCrystals, obsidian, getConfig().getInt(Paths.TIME)), plugin);
						pm.registerEvents(new EnderdragonPreventPortal(plugin, ec.getWorld(), getConfig().getBoolean(Paths.CREATE_PORTAL), getConfig().getBoolean(Paths.CREATE_EGG)), plugin);
					}
				}
			}
			
		}.runTaskLater(this, 1);
	}
	
	public void onDisable() {
	
	}
}
