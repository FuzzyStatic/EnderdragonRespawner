package com.fuzzycraft.fuzzy.utilities;

import java.io.File;
import java.util.logging.Level;

import com.fuzzycraft.fuzzy.configurations.ConfigParameters;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.World;

import com.fuzzycraft.fuzzy.constants.Defaults;

/**
 * @author Allen Flickinger (allen.flickinger@gmail.com)
 */

public class DirectoryStructure {

    private JavaPlugin plugin;
    private static File parentDirectory, worldsDirectory;

    public DirectoryStructure(JavaPlugin plugin) {
        this.plugin = plugin;
        parentDirectory = new File(this.plugin.getDataFolder() + File.separator);
        worldsDirectory = new File(getParentDirectoryPath() + File.separator + Defaults.DIR_WORLDS + File.separator);
    }

    public void createDirectoryStructure() {
        if (this.createParentDirectory()) {
            if (this.createWorldsDirectory()) {
                createWorldDirectories();
            }
        }
    }

    private boolean createParentDirectory() {
        if (!parentDirectory.exists()) {
            this.plugin.getLogger().log(Level.INFO, "Creating Parent Directory " + parentDirectory.toString() + ": " +
                    parentDirectory.mkdir());
        }

        return parentDirectory.exists();
    }

    private boolean createWorldsDirectory() {
        if (!worldsDirectory.exists()) {
            this.plugin.getLogger().log(Level.INFO, "Creating Worlds Directory " + worldsDirectory.toString() + ": " +
                    worldsDirectory.mkdir());
        }

        return worldsDirectory.exists();
    }

    private void createWorldDirectories() {
        for (World world : this.plugin.getServer().getWorlds()) {
            File worldDirectory = new File(getWorldsDirectoryPath() + File.separator + world.getName() + File.separator);

            if (!worldDirectory.exists()) {
                this.plugin.getLogger().log(Level.INFO, "Creating World Directory " + worldDirectory.toString() + ": " +
                        worldDirectory.mkdir());
            }
        }
    }

    private void createWorldDirectory(World world) {
        File worldDirectory = new File(getWorldsDirectoryPath() + File.separator + world.getName() + File.separator);

        if (!worldDirectory.exists()) {
            this.plugin.getLogger().log(Level.INFO, "Creating World Directory " + worldDirectory.toString() + ": " +
                    worldDirectory.mkdir());
        }
    }

    public void createWorldDefaultConfigurations() {
        for (World world : this.plugin.getServer().getWorlds()) {
            File file = new File(getWorldsDirectoryPath() + File.separator + world.getName() + File.separator + Defaults.CONFIG_NAME);

            if (!file.exists()) {
                this.plugin.getLogger().log(Level.INFO, "Creating Default Configuration for " + world.getName());
                ConfigParameters defaultConfig = new ConfigParameters(this.plugin, file.toString());
                defaultConfig.setDefaults();
            }
        }
    }

    public static File getParentDirectory() {
        return parentDirectory;
    }

    public static String getParentDirectoryPath() {
        return parentDirectory.toString();
    }

    public static File getWorldsDirectory() {
        return worldsDirectory;
    }

    public static String getWorldsDirectoryPath() {
        return worldsDirectory.toString();
    }
}