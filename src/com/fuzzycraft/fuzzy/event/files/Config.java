/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-20 18:08:07
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-30 23:51:35
 */

package com.fuzzycraft.fuzzy.event.files;

import java.io.File;
import java.util.logging.Level;

import com.fuzzycraft.fuzzy.utilities.ConfigAccessor;
import com.fuzzycraft.fuzzy.utilities.SerializableVector;
import com.fuzzycraft.fuzzy.utilities.YamlVector;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Config extends ConfigTree {
  private JavaPlugin plugin;
  private World w;
  private ConfigAccessor ca;
  private FileConfiguration fc;

  public Config(JavaPlugin plugin, World w) {
    super(plugin);
    this.plugin = plugin;
    this.w = w;
    this.ca = new ConfigAccessor(this.plugin, getWorldConfigPath(w));
    this.fc = ca.getConfig();
  }

  public World getWorld() { return this.w; }

  public boolean getActive() { return this.fc.getBoolean(Path.ACTIVE); }

  public Location getLocation() {
    YamlVector yv = new YamlVector(this.fc, Path.SPAWNLOCATION);
    SerializableVector sv = new SerializableVector(yv.getVectorMap());
    Vector vector = sv.getVector();
    return new Location(this.w, vector.getX(), vector.getY(), vector.getZ());
  }

  public int getAmount() { return this.fc.getInt(Path.AMOUNT); }

  public int getTime() { return this.fc.getInt(Path.TIME); }

  public String getMsg() { return this.fc.getString(Path.MSG); }

  public boolean getRespawnCrystals() {
    return this.fc.getBoolean(Path.RESPAWN_CRYSTALS);
  }

  public boolean getRespawnObsidian() {
    return this.fc.getBoolean(Path.RESPAWN_OBSIDIAN);
  }

  public boolean getCreatePortal() {
    return this.fc.getBoolean(Path.CREATE_PORTAL);
  }

  public boolean getCreateEgg() { return this.fc.getBoolean(Path.CREATE_EGG); }

  public void setActive(boolean active) {
    FileConfiguration fc = ca.getConfig();
    fc.set(Path.ACTIVE, active);
    this.ca.saveConfig();
  }

  public boolean createWorldDefConfig() {
    File f = new File(super.getWorldsDirectory().toString() + File.separator +
                      this.w.getName() + File.separator + Name.YML_CONFIG);

    if (!f.exists()) {
      this.plugin.getLogger().log(
          Level.INFO, "Creating default configuration for " + w.getName());

      ConfigAccessor ca = new ConfigAccessor(this.plugin, f.toString());
      setDefaults(ca);

      return true;
    }

    return false;
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
