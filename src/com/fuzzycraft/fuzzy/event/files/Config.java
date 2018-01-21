/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-20 18:08:07
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-20 22:34:54
 */

package com.fuzzycraft.fuzzy.event.files;

import com.fuzzycraft.fuzzy.utilities.ConfigAccessor;
import com.fuzzycraft.fuzzy.utilities.SerializableVector;
import com.fuzzycraft.fuzzy.utilities.YamlVector;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Config extends ConfigTree {
  private JavaPlugin plugin;
  private ConfigAccessor configAccessor;
  private FileConfiguration config;
  private World world;

  public Config(JavaPlugin plugin, World world) {
    super(plugin);
    this.plugin = plugin;

    if (!this.plugin.isEnabled()) {
      throw new IllegalArgumentException("Plugin must be initialized!");
    }

    this.world = world;
    this.configAccessor =
        new ConfigAccessor(plugin, getWorldConfigPath().toString());
    this.config = configAccessor.getConfig();
  }

  public void setDefaults() {
    this.config.set(Path.ACTIVE, Parameter.ACTIVE);
    this.config.set(Path.SPAWNLOCATION,
                    new SerializableVector(
                        new Vector(Parameter.X, Parameter.Y, Parameter.Z))
                        .serialize());
    this.config.set(Path.AMOUNT, Parameter.AMOUNT);
    this.config.set(Path.TIME, Parameter.TIME);
    this.config.set(Path.MSG, Parameter.MSG);
    this.config.set(Path.RESPAWN_CRYSTALS, Parameter.RESPAWN_CRYSTALS);
    this.config.set(Path.RESPAWN_OBSIDIAN, Parameter.RESPAWN_OBSIDIAN);
    this.config.set(Path.CREATE_PORTAL, Parameter.CREATE_PORTAL);
    this.config.set(Path.CREATE_EGG, Parameter.CREATE_EGG);
    this.configAccessor.saveConfig();
  }

  public World getWorld() { return this.world; }

  public boolean getActive() { return this.config.getBoolean(Path.ACTIVE); }

  public Location getLocation() {
    YamlVector yv = new YamlVector(this.config, Path.SPAWNLOCATION);
    SerializableVector sv = new SerializableVector(yv.getVectorMap());
    Vector vector = sv.getVector();
    return new Location(this.world, vector.getX(), vector.getY(),
                        vector.getZ());
  }

  public int getAmount() { return this.config.getInt(Path.AMOUNT); }

  public int getTime() { return this.config.getInt(Path.TIME); }

  public String getMsg() { return this.config.getString(Path.MSG); }

  public boolean getRespawnCrystals() {
    return this.config.getBoolean(Path.RESPAWN_CRYSTALS);
  }

  public boolean getRespawnObsidian() {
    return this.config.getBoolean(Path.RESPAWN_OBSIDIAN);
  }

  public boolean getCreatePortal() {
    return this.config.getBoolean(Path.CREATE_PORTAL);
  }

  public boolean getCreateEgg() {
    return this.config.getBoolean(Path.CREATE_EGG);
  }

  public void createWorldDefaultConfigurations() {
    for (World world : this.plugin.getServer().getWorlds()) {
      File file =
          new File(super.getWorldsDirectory().toString() + File.separator +
                   world.getName() + File.separator + Name.YML_CONFIG);

      if (!file.exists()) {
        this.plugin.getLogger().log(Level.INFO,
                                    "Creating Default Configuration for " +
                                        world.getName());
        this.setDefaults();
      }
    }
  }

  public File getWorldConfigPath() {
    return new File(super.getWorldsDirectory().toString() + File.separator +
                    this.world.getName() + File.separator + Name.YML_CONFIG);
  }
}
