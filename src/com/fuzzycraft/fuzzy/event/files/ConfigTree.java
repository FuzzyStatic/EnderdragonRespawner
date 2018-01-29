/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-20 18:08:07
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-21 22:47:06
 */

package com.fuzzycraft.fuzzy.event.files;

import com.fuzzycraft.fuzzy.utilities.ConfigAccessor;
import com.fuzzycraft.fuzzy.utilities.SerializableVector;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

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
    for (World world : this.plugin.getServer().getWorlds()) {
      File worldDirectory =
          new File(getWorldsDirectory().toString() + File.separator +
                   world.getName() + File.separator);

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

  public File getWorldConfigPath(World world) {
    return new File(getWorldsDirectory().toString() + File.separator +
                    world.getName() + File.separator + Name.YML_CONFIG);
  }

  public HashMap<World, File> getWorldDirectories() {
    HashMap<World, File> hm = new HashMap<World, File>();

    for (File worldsFile : getWorldsDirectory().listFiles()) {
      if (worldsFile.isDirectory()) {
        World world = plugin.getServer().getWorld(worldsFile.getName());

        if (world != null) {
          hm.put(world, worldsFile);
        } else {
          this.plugin.getLogger().log(Level.WARNING, "World " +
                                                         worldsFile.getName() +
                                                         " Does Not Exist");
        }
      }
    }

    return hm;
  }

  public void setDefaults(ConfigAccessor configAccessor) {
    FileConfiguration config = configAccessor.getConfig();
    config.set(Path.ACTIVE, Parameter.ACTIVE);
    config.set(Path.SPAWNLOCATION,
               new SerializableVector(
                   new Vector(Parameter.X, Parameter.Y, Parameter.Z))
                   .serialize());
    config.set(Path.AMOUNT, Parameter.AMOUNT);
    config.set(Path.TIME, Parameter.TIME);
    config.set(Path.MSG, Parameter.MSG);
    config.set(Path.RESPAWN_CRYSTALS, Parameter.RESPAWN_CRYSTALS);
    config.set(Path.RESPAWN_OBSIDIAN, Parameter.RESPAWN_OBSIDIAN);
    config.set(Path.CREATE_PORTAL, Parameter.CREATE_PORTAL);
    config.set(Path.CREATE_EGG, Parameter.CREATE_EGG);
    configAccessor.saveConfig();
  }

  public boolean createWorldSampleConfig() {
    File file = new File(getWorldsDirectory().toString() + File.separator +
                         Name.YML_CONFIG);
    ConfigAccessor configAccessor = new ConfigAccessor(plugin, file.toString());

    if (!file.exists()) {
      this.plugin.getLogger().log(Level.INFO, "Creating sample configuration");
      setDefaults(configAccessor);

      return true;
    }

    return false;
  }
}
