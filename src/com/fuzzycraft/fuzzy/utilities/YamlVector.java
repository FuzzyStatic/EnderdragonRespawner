/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-02-03 18:40:02
 * @Last Modified by:   FuzzyStatic
 * @Last Modified time: 2018-02-03 18:40:02
 */

package com.fuzzycraft.fuzzy.utilities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

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