package com.fuzzycraft.fuzzy.utilities;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class DirectoryStructure {
	
	private JavaPlugin plugin;
	private String directoryName;
	private File directory;
	
	public DirectoryStructure(JavaPlugin plugin, String directoryName) {
		this.plugin = plugin;
		this.directoryName = directoryName;
		this.directory = new File(this.plugin.getDataFolder() + File.separator + this.directoryName + File.separator);
		
	}

	public void createDirectory() {		
		if (!this.directory.exists()) {
			this.directory.mkdir();
		}
	}
	
	public File getDirectory() {
		return this.directory;
	}
}