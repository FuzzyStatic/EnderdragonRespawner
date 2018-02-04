/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-02-03 18:39:20
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-02-03 20:52:52
 */

package com.fuzzycraft.fuzzy.utilities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.plugin.java.JavaPlugin;

public class SerializableLocation implements ConfigurationSerializable {

  private final World world;
  private final double x, y, z;
  private final float yaw, pitch;
  private final Location location;

  public SerializableLocation(Location location) {
    this.world = location.getWorld();
    this.x = location.getBlockX();
    this.y = location.getBlockY();
    this.z = location.getBlockZ();
    this.pitch = location.getPitch();
    this.yaw = location.getYaw();
    this.location = location;
  }

  public SerializableLocation(Map<String, Object> map) {
    this.world = Bukkit.getWorld(map.get("world").toString());
    this.x = (Double)map.get("x");
    this.y = (Double)map.get("y");
    this.z = (Double)map.get("z");
    this.pitch = Float.intBitsToFloat((Integer)map.get("pitch"));
    this.yaw = Float.intBitsToFloat((Integer)map.get("yaw"));
    this.location =
        new Location(this.world, this.x, this.y, this.z, this.yaw, this.pitch);
  }

  public Map<String, Object> serialize() {
    Map<String, Object> map = new HashMap<String, Object>();

    if (this.location.getWorld() != null) {
      map.put("world", this.location.getWorld().getName());
    } else {
      map.put("world", "replace_me");
    }

    map.put("x", this.location.getX());
    map.put("y", this.location.getY());
    map.put("z", this.location.getZ());
    map.put("pitch", Float.floatToIntBits(this.location.getPitch()));
    map.put("yaw", Float.floatToIntBits(this.location.getYaw()));
    return map;
  }

  public String serializeString() {
    StringBuilder s = new StringBuilder();
    s.append(this.location.getWorld().getName());
    s.append(",");
    s.append(this.location.getX());
    s.append(",");
    s.append(this.location.getY());
    s.append(",");
    s.append(this.location.getZ());
    s.append(",");
    s.append(Float.floatToIntBits(this.location.getPitch()));
    s.append(",");
    s.append(Float.floatToIntBits(this.location.getYaw()));
    return s.toString();
  }

  public static Location deserializeString(JavaPlugin plugin, String location) {
    String[] arg = location.split(",");
    double[] xyz = new double[3];
    float[] pitchYaw = new float[2];

    for (int i = 0; i < 3; i++) {
      xyz[i] = Double.parseDouble(arg[i + 1]);
    }

    for (int i = 0; i < 2; i++) {
      pitchYaw[i] = Float.parseFloat(arg[i + 1]);
    }

    return new Location(plugin.getServer().getWorld(arg[0]), xyz[0], xyz[1],
                        xyz[2], pitchYaw[0], pitchYaw[1]);
  }

  public Location getLocation() { return this.location; }

  public World getWorld() { return this.world; }
}