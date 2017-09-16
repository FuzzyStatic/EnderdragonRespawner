package com.fuzzycraft.fuzzy.utilities;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.World;


/**
 * @author Allen Flickinger (allen.flickinger@gmail.com)
 */

public class DirectoryStructure {

    public static final String FILE_CONFIG = "config.yml";
    public static final String FILE_OBSIDIAN = "obsidian.data";
    public static final String FILE_CRYSTAL = "crystal.data";

    public static final String DIR_WORLDS = "worlds";
    public static final String DIR_DATA = "data";

    private JavaPlugin plugin;
    private static File parentDirectory, worldsDirectory;

    public DirectoryStructure(JavaPlugin plugin) {
        this.plugin = plugin;
        parentDirectory = new File(this.plugin.getDataFolder() + File.separator);
        worldsDirectory = new File(getParentDirectoryPath() + File.separator + DIR_WORLDS + File.separator);
    }

    public void createDirectoryStructure() throws IOException {
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

    private void createWorldDirectories() throws IOException {
        for (World world : this.plugin.getServer().getWorlds()) {
            File worldDirectory = new File(getWorldsDirectoryPath() + File.separator + world.getName() + File.separator);

            if (!worldDirectory.exists()) {
                this.plugin.getLogger().log(Level.INFO, "Creating World Directory " + worldDirectory.toString() + ": " +
                        worldDirectory.mkdir());
                createDataDirectory(worldDirectory.toString());
            }
        }
    }

    private void createWorldDirectory(World world) throws IOException {
        File worldDirectory = new File(getWorldsDirectoryPath() + File.separator + world.getName() + File.separator);

        if (!worldDirectory.exists()) {
            this.plugin.getLogger().log(Level.INFO, "Creating World Directory " + worldDirectory.toString() + ": " +
                    worldDirectory.mkdir());
            createDataDirectory(worldDirectory.toString());
        }
    }

    // TODO: Make this non reliant on initial world folder creation
    private void createDataDirectory(String worldDirectory) throws IOException {
        File dataDirectory = new File(worldDirectory + File.separator + DIR_DATA + File.separator);
        File crystalFile = new File(dataDirectory + File.separator + FILE_CRYSTAL + File.separator);
        File obsidianFile = new File(dataDirectory + File.separator + FILE_OBSIDIAN + File.separator);

        if (!dataDirectory.exists()) {
            this.plugin.getLogger().log(Level.INFO, "Creating Data Directory for " + worldDirectory + ": " +
                    dataDirectory.mkdir());

            if (!crystalFile.exists()) {
                this.plugin.getLogger().log(Level.INFO, "Creating Enderdragon Crystal Data File for " + worldDirectory + ": " +
                        crystalFile.createNewFile());
            }

            if (!obsidianFile.exists()) {
                this.plugin.getLogger().log(Level.INFO, "Creating Obsidian Data File for " + worldDirectory + ": " +
                        obsidianFile.createNewFile());
            }
        }


    }

    public void createWorldDefaultConfigurations() {
        for (World world : this.plugin.getServer().getWorlds()) {
            File file = new File(getWorldsDirectoryPath() + File.separator + world.getName() + File.separator + FILE_CONFIG);

            if (!file.exists()) {
                this.plugin.getLogger().log(Level.INFO, "Creating Default Configuration for " + world.getName());
                ConfigParameters defaultConfig = new ConfigParameters(this.plugin, world, file.toString());
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