/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-20 18:08:07
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-30 23:12:00
 */

package com.fuzzycraft.fuzzy.event.files;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import com.fuzzycraft.fuzzy.utilities.ConfigAccessor;
import com.fuzzycraft.fuzzy.utilities.SerializableVector;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class ConfigTree {
  private JavaPlugin plugin;
  private File parentDirectory, worldsDirectory;

  public ConfigTree(JavaPlugin plugin) {
    this.plugin = plugin;

    if (!this.plugin.isEnabled()) {
      throw new IllegalArgumentException("Plugin must be initialized!");
    }

    parentDirectory = new File(this.plugin.getDataFolder() + File.separator);
    worldsDirectory =
        new File(getParentDirectory().toString() + File.separator +
                 Name.DIR_WORLDS + File.separator);
  }

  public void createDirectoryStructure() throws IOException {
    if (this.createParentDirectory()) {
      if (this.createWorldsDirectory()) {
        createWorldDirectories();
      }
    }
  }

  public boolean createParentDirectory() {
    if (!parentDirectory.exists()) {
      this.plugin.getLogger().log(Level.INFO, "Creating Parent Directory " +
                                                  parentDirectory.toString() +
                                                  ": " +
                                                  parentDirectory.mkdir());
    }

    return parentDirectory.exists();
  }

  private boolean createWorldsDirectory() {
    if (!worldsDirectory.exists()) {
      this.plugin.getLogger().log(Level.INFO, "Creating Worlds Directory " +
                                                  worldsDirectory.toString() +
                                                  ": " +
                                                  worldsDirectory.mkdir());
    }

    return worldsDirectory.exists();
  }

  private void createWorldDirectories() throws IOException {
    for (World w : this.plugin.getServer().getWorlds()) {
      File worldDirectory =
          new File(getWorldsDirectory().toString() + File.separator +
                   w.getName() + File.separator);

      if (!worldDirectory.exists()) {
        this.plugin.getLogger().log(Level.INFO, "Creating World Directory " +
                                                    worldDirectory.toString() +
                                                    ": " +
                                                    worldDirectory.mkdir());
        createDataDirectory(worldDirectory.toString());
      }
    }
  }

  // TODO: Make this non reliant on initial world folder creation
  private void createDataDirectory(String worldDirectory) throws IOException {
    File dataDirectory = new File(worldDirectory + File.separator +
                                  Name.DIR_DATA + File.separator);
    File crystalFile = new File(dataDirectory + File.separator +
                                Name.DAT_CRYSTAL + File.separator);
    File obsidianFile = new File(dataDirectory + File.separator +
                                 Name.DAT_OBSIDIAN + File.separator);

    if (!dataDirectory.exists()) {
      this.plugin.getLogger().log(Level.INFO, "Creating Data Directory for " +
                                                  worldDirectory + ": " +
                                                  dataDirectory.mkdir());

      if (!crystalFile.exists()) {
        this.plugin.getLogger().log(
            Level.INFO, "Creating Enderdragon Crystal Data File for " +
                            worldDirectory + ": " +
                            crystalFile.createNewFile());
      }

      if (!obsidianFile.exists()) {
        this.plugin.getLogger().log(
            Level.INFO, "Creating Obsidian Data File for " + worldDirectory +
                            ": " + obsidianFile.createNewFile());
      }
    }
  }

  public File getParentDirectory() { return parentDirectory; }

  public File getWorldsDirectory() { return worldsDirectory; }

  public String getWorldConfigPath(World w) {
    return getWorldsDirectory().toString() + File.separator + w.getName() +
        File.separator + Name.YML_CONFIG;
  }

  public void createAllWorldsDefaultConfiguration() {
    for (World w : this.plugin.getServer().getWorlds()) {
      File file = new File(getWorldsDirectory().toString() + File.separator +
                           w.getName() + File.separator + Name.YML_CONFIG);

      if (!file.exists()) {
        Config c = new Config(plugin, w);
        c.createWorldDefConfig();
      }
    }
  }

  public void setDefaults(ConfigAccessor ca) {
    FileConfiguration fc = ca.getConfig();
    fc.set(Path.ACTIVE, Parameter.ACTIVE);
    fc.set(Path.SPAWNLOCATION,
           new SerializableVector(
               new Vector(Parameter.X, Parameter.Y, Parameter.Z))
               .serialize());
    fc.set(Path.AMOUNT, Parameter.AMOUNT);
    fc.set(Path.TIME, Parameter.TIME);
    fc.set(Path.MSG, Parameter.MSG);
    fc.set(Path.RESPAWN_CRYSTALS, Parameter.RESPAWN_CRYSTALS);
    fc.set(Path.RESPAWN_OBSIDIAN, Parameter.RESPAWN_OBSIDIAN);
    fc.set(Path.CREATE_PORTAL, Parameter.CREATE_PORTAL);
    fc.set(Path.CREATE_EGG, Parameter.CREATE_EGG);
    ca.saveConfig();
  }
}
