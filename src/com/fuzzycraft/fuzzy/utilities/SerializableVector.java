/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-02-03 18:39:41
 * @Last Modified by:   FuzzyStatic
 * @Last Modified time: 2018-02-03 18:39:41
 */

package com.fuzzycraft.fuzzy.utilities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;

public class SerializableVector implements ConfigurationSerializable {

  private final double x, y, z;
  private final Vector vector;

  public SerializableVector(Vector vector) {
    this.x = vector.getBlockX();
    this.y = vector.getBlockY();
    this.z = vector.getBlockZ();
    this.vector = vector;
  }

  public SerializableVector(Map<String, Object> map) {
    this.x = (Double)map.get("x");
    this.y = (Double)map.get("y");
    this.z = (Double)map.get("z");
    this.vector = new Vector(this.x, this.y, this.z);
  }

  public Map<String, Object> serialize() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("x", this.vector.getX());
    map.put("y", this.vector.getY());
    map.put("z", this.vector.getZ());
    return map;
  }

  public Vector getVector() { return this.vector; }
}