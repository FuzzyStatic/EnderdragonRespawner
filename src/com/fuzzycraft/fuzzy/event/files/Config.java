/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-20 18:08:07
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-21 22:34:35
 */

package com.fuzzycraft.fuzzy.event.files;

import com.fuzzycraft.fuzzy.utilities.ConfigAccessor;
import com.fuzzycraft.fuzzy.utilities.SerializableVector;
import com.fuzzycraft.fuzzy.utilities.YamlVector;
import java.io.File;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Config extends ConfigTree {
  private JavaPlugin plugin;
  private FileConfiguration config;
  private World world;

  public Config(JavaPlugin plugin, World world) {
    super(plugin);
    this.plugin = plugin;

    if (!this.plugin.isEnabled()) {
      throw new IllegalArgumentException("Plugin must be initialized!");
    }

    this.world = world;
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

  public boolean createWorldDefConfig() {
    File file =
        new File(super.getWorldsDirectory().toString() + File.separator +
                 this.world.getName() + File.separator + Name.YML_CONFIG);

    if (!file.exists()) {
      this.plugin.getLogger().log(
          Level.INFO, "Creating default configuration for " + world.getName());

      ConfigAccessor configAccessor =
          new ConfigAccessor(plugin, file.toString());
      super.setDefaults(configAccessor);

      return true;
    }

    return false;
  }
}
