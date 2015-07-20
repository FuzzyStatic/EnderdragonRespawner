package com.fuzzycraft.fuzzy.configurations;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import com.fuzzycraft.fuzzy.EnderdragonRespawner;
import com.fuzzycraft.fuzzy.constants.Defaults;
import com.fuzzycraft.fuzzy.constants.Paths;
import com.fuzzycraft.fuzzy.utilities.ConfigAccessor;
import com.fuzzycraft.fuzzy.utilities.SerializableLocation;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class ConfigParameters {

	protected final ConfigAccessor configAccessor;
	protected final FileConfiguration config;
	private EnderdragonRespawner plugin;
	private Location location;
	private World world;
	private double x = Defaults.X;
	private double y = Defaults.Y;
	private double z = Defaults.Z;
	
	public ConfigParameters(EnderdragonRespawner plugin, String directory, String world) {
		this.plugin = plugin;
		
		if (!this.plugin.isEnabled()) {
			throw new IllegalArgumentException("Plugin must be initialized!");
		}
		
		this.world = this.plugin.getServer().getWorld(world);
		this.location = new Location(this.world, this.x, this.y, this.z);
		this.configAccessor = new ConfigAccessor(this.plugin, directory + File.separator + world + ".yml");
		this.config = configAccessor.getConfig();
	}
	
	public void setDefaults() {
		this.config.addDefault(Paths.LOCATION, new SerializableLocation(this.location).serialize());
		this.config.addDefault(Paths.AMOUNT, Defaults.AMOUNT);
		this.config.addDefault(Paths.TIME, Defaults.TIME);
		this.config.addDefault(Paths.MSG, Defaults.MSG);
		this.config.addDefault(Paths.RESPAWN_CRYSTALS, Defaults.RESPAWN_CRYSTALS);
		this.config.addDefault(Paths.RESPAWN_OBSIDIAN, Defaults.RESPAWN_OBSIDIAN);
		this.config.addDefault(Paths.CREATE_PORTAL, Defaults.CREATE_PORTAL);
		this.config.addDefault(Paths.CREATE_EGG, Defaults.CREATE_EGG);
		this.configAccessor.saveDefaultConfig();
	}
}
