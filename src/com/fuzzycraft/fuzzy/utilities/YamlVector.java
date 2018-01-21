package com.fuzzycraft.fuzzy.utilities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Allen Flickinger (allen.flickinger@gmail.com)
 */

public class YamlVector {

  private FileConfiguration config;
  private String key;
  private static double x, y, z;
  private final Map<String, Object> map = new HashMap<String, Object>();

  public YamlVector(FileConfiguration config, String key) {
    this.config = config;
    this.key = key;
  }

  public Map<String, Object> getVectorMap() {
    map.put("x", this.config.get(this.key + ".x"));
    map.put("y", this.config.get(this.key + ".y"));
    map.put("z", this.config.get(this.key + ".z"));
    return map;
  }

  public void setVector(Map<String, Object> map) {
    this.config.set(this.key + ".x", map.get("x"));
    this.config.set(this.key + ".y", map.get("y"));
    this.config.set(this.key + ".z", map.get("z"));
  }

  public void setBlankVector() {
    this.config.set(this.key + ".x", x);
    this.config.set(this.key + ".y", y);
    this.config.set(this.key + ".z", z);
  }
}