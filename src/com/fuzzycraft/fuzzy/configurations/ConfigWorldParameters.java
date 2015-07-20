package com.fuzzycraft.fuzzy.configurations;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import com.fuzzycraft.fuzzy.EnderdragonRespawner;
import com.fuzzycraft.fuzzy.constants.Defaults;
import com.fuzzycraft.fuzzy.constants.Paths;
import com.fuzzycraft.fuzzy.utilities.ConfigAccessor;
import com.fuzzycraft.fuzzy.utilities.SerializableLocation;
import com.fuzzycraft.fuzzy.utilities.YamlLocation;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class ConfigWorldParameters {
	
	private ConfigAccessor configAccessor;
	private FileConfiguration config;
	private EnderdragonRespawner plugin;
	private Location location;
	private World world;
	private File filename;
	
	/**
	 * Constructor from filename.
	 * @param plugin
	 * @param directory
	 * @param filename
	 */
	public ConfigWorldParameters(EnderdragonRespawner plugin, String directory, File filename) {
		this.plugin = plugin;
		
		if (!this.plugin.isEnabled()) {
			throw new IllegalArgumentException("Plugin must be initialized!");
		}
		
		this.filename = filename;
		this.configAccessor = new ConfigAccessor(this.plugin, directory + File.separator + filename);
		this.config = configAccessor.getConfig();
	}
	
	public void setDefaults() {
		this.world = this.plugin.getServer().getWorld(FilenameUtils.removeExtension(this.filename.toString()));
		this.location = new Location(this.world, Defaults.X, Defaults.Y, Defaults.Z);
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
	
	public Location getLocation() {
		YamlLocation location = new YamlLocation(this.config, this.world.getName());
		return new SerializableLocation(location.getLocationMap()).getLocation();
	}
	
	public int getAmount() {
		return this.config.getInt(Paths.AMOUNT);
	}
	
	public int getTime() {
		return this.config.getInt(Paths.TIME);
	}
	
	public String getMsg() {
		return this.config.getString(Paths.MSG);
	}
	
	public boolean getRespawnCrystals() {
		return this.config.getBoolean(Paths.RESPAWN_CRYSTALS);
	}
	
	public boolean getRespawnObsidian() {
		return this.config.getBoolean(Paths.RESPAWN_OBSIDIAN);
	}
	
	public boolean getCreatePortal() {
		return this.config.getBoolean(Paths.CREATE_PORTAL);
	}
	
	public boolean getCreateEgg() {
		return this.config.getBoolean(Paths.CREATE_EGG);
	}
}
