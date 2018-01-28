/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.s.getConfig()om)
 * @Date: 2018-01-20 17:06:03
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-28 14:15:15
 */

package com.fuzzycraft.fuzzy.event;

import com.fuzzycraft.fuzzy.event.files.Config;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

public class Management {
  private static HashMap<World, Structure> eventMap;

  private JavaPlugin plugin;
  private World w;
  private Structure s;

  public Management(JavaPlugin plugin, World w) {
    this.plugin = plugin;
    this.w = w;

    this.s = eventMap.get(w);

    // If Structure doesn't exist, initialize it
    if (s == null) {
      Config c = new Config(plugin, this.w);
      this.s.setConfig(c);
      eventMap.put(this.w, this.s);
    }
  }

  public void spawnEnderdragons() {
    for (int i = 0; i < this.s.getConfig().getAmount(); i++) {
      this.s.getConfig().getWorld().spawnEntity(
          this.s.getConfig().getLocation(), EntityType.ENDER_DRAGON);
    }

    this.plugin.getServer().broadcastMessage(ChatColor.DARK_RED +
                                             this.s.getConfig().getMsg());
  }

  public boolean enderdragonsExist() {
    for (Entity entity : this.s.getConfig().getWorld().getEntities()) {
      if (entity instanceof EnderDragon && !entity.isDead()) {
        return true;
      }
    }

    return false;
  }

  // Remove all Enderdragon entities from ConfigParameters world
  public int removeAllEnderdragons() {
    int entityCounter = 0;

    for (Entity entity : this.s.getConfig().getWorld().getEntities()) {
      if (entity instanceof EnderDragon && !entity.isDead()) {
        entity.remove();
        entityCounter++;
      }
    }

    return entityCounter;
  }

  // Remove all Enderdragon entities from specified world
  public static int removeAll(World world) {
    int entityCounter = 0;

    for (Entity entity : world.getEntities()) {
      if (entity instanceof EnderDragon && !entity.isDead()) {
        entity.remove();
        entityCounter++;
      }
    }

    return entityCounter;
  }

  public Config getConfigParameters() { return this.s.getConfig(); }

  public HashMap<World, Structure> getEventMap() { return eventMap; }
}
