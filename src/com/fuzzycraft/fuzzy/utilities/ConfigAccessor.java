package com.fuzzycraft.fuzzy.utilities;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class ConfigAccessor {
	 
    private final String filename;
    private final JavaPlugin plugin;
 
    private File configFile;
    private FileConfiguration fileConfiguration;
 
    public ConfigAccessor(JavaPlugin plugin, String filename) {
		if (!plugin.isEnabled()) {
			throw new IllegalArgumentException("Plugin must be enabled!");
		}
		
        this.plugin = plugin;
        this.filename = filename;
    }
 
    public void reloadConfig() {
        if (this.configFile == null) {
        	this.configFile = new File(this.filename);
        }
        
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.configFile);
    }
 
    public FileConfiguration getConfig() {
        if (this.fileConfiguration == null) {
        	this.reloadConfig();
        }
        
        return this.fileConfiguration;
    }
 
    public void saveConfig() {
    	if (this.fileConfiguration != null || this.configFile != null) {
            try {
                getConfig().save(this.configFile);
            } catch (IOException e) {
            	this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, e);
            }
        }
    }
 
    public void saveDefaultConfig() {
        if (!this.configFile.exists()) {
        	this.plugin.saveResource(this.filename, false);
        }
    }
}
