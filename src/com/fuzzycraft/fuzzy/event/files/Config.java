/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-20 18:08:07
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-29 16:55:46
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
  private FileConfiguration c;

  public Config(JavaPlugin plugin, World w) {
    super(plugin);
    this.plugin = plugin;
    this.w = w;
    this.c = new ConfigAccessor(this.plugin, getWorldConfigPath(w)).getConfig();
  }

  public World getWorld() { return this.w; }

  public boolean getActive() { return this.c.getBoolean(Path.ACTIVE); }

  public Location getLocation() {
    YamlVector yv = new YamlVector(this.c, Path.SPAWNLOCATION);
    SerializableVector sv = new SerializableVector(yv.getVectorMap());
    Vector vector = sv.getVector();
    return new Location(this.w, vector.getX(), vector.getY(), vector.getZ());
  }

  public int getAmount() { return this.c.getInt(Path.AMOUNT); }

  public int getTime() { return this.c.getInt(Path.TIME); }

  public String getMsg() { return this.c.getString(Path.MSG); }

  public boolean getRespawnCrystals() {
    return this.c.getBoolean(Path.RESPAWN_CRYSTALS);
  }

  public boolean getRespawnObsidian() {
    return this.c.getBoolean(Path.RESPAWN_OBSIDIAN);
  }

  public boolean getCreatePortal() {
    return this.c.getBoolean(Path.CREATE_PORTAL);
  }

  public boolean getCreateEgg() { return this.c.getBoolean(Path.CREATE_EGG); }

  public boolean createWorldDefConfig() {
    File f = new File(super.getWorldsDirectory().toString() + File.separator +
                      this.w.getName() + File.separator + Name.YML_CONFIG);

    if (!f.exists()) {
      this.plugin.getLogger().log(
          Level.INFO, "Creating default configuration for " + w.getName());

      ConfigAccessor ca = new ConfigAccessor(this.plugin, f.toString());
      super.setDefaults(ca);

      return true;
    }

    return false;
  }
}
