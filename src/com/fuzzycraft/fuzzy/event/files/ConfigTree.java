/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-20 18:08:07
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-02-03 20:53:26
 */

package com.fuzzycraft.fuzzy.event.files;

import com.fuzzycraft.fuzzy.utilities.SerializableLocation;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

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

  public static String createCrystalDataFile(JavaPlugin plugin, World w) {
    ConfigTree ct = new ConfigTree(plugin);
    String filename = ct.getWorldsDirectory().toString() + File.separator +
                      w.getName() + File.separator + Name.DIR_DATA +
                      File.separator + Name.DAT_CRYSTAL;
    File f = new File(filename);

    if (!f.exists()) {
      try {
        f.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return filename;
  }

  public static String createObsidianDataFile(JavaPlugin plugin, World w) {
    ConfigTree ct = new ConfigTree(plugin);
    String filename = ct.getWorldsDirectory().toString() + File.separator +
                      w.getName() + File.separator + Name.DIR_DATA +
                      File.separator + Name.DAT_OBSIDIAN;
    File f = new File(filename);

    if (!f.exists()) {
      try {
        f.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return filename;
  }

  public static void addLocationToFile(String filename, Location location) {
    BufferedWriter bw = null;

    try {
      bw = new BufferedWriter(new FileWriter(filename, true));
      SerializableLocation sl = new SerializableLocation(location);
      bw.write(sl.serializeString());
      bw.newLine();
      bw.flush();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (bw != null)
        try {
          bw.close();
        } catch (IOException e2) {
          e2.printStackTrace();
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
}
