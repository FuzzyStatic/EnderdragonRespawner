/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-20 17:06:03
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-20 18:14:25
 */
package com.fuzzycraft.fuzzy.event;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import com.fuzzycraft.fuzzy.utilities.ConfigParameters;

public class Enderdragon {

  private JavaPlugin plugin;
  private ConfigParameters cp;

  public Enderdragon(JavaPlugin plugin) { this.plugin = plugin; }

  public void spawn() {
    for (int i = 0; i < this.cp.getAmount(); i++) {
      this.cp.getWorld().spawnEntity(this.cp.getLocation(),
                                     EntityType.ENDER_DRAGON);
    }

    this.plugin.getServer().broadcastMessage(ChatColor.DARK_RED +
                                             this.cp.getMsg());
  }

  public boolean exists() {
    for (Entity entity : this.cp.getWorld().getEntities()) {
      if (entity instanceof EnderDragon && !entity.isDead()) {
        return true;
      }
    }

    return false;
  }

  // Remove all Enderdragon entities from ConfigParameters world
  public int removeAll() {
    int entityCounter = 0;

    for (Entity entity : this.cp.getWorld().getEntities()) {
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

  public ConfigParameters getConfigParameters() { return this.cp; }
}
