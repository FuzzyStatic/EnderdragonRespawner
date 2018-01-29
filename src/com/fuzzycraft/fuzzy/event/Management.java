/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.s.getConfig()om)
 * @Date: 2018-01-20 17:06:03
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-29 17:04:08
 */

package com.fuzzycraft.fuzzy.event;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import com.fuzzycraft.fuzzy.event.files.Config;
import com.fuzzycraft.fuzzy.event.listeners.EnderCrystals;
import com.fuzzycraft.fuzzy.event.listeners.Obsidian;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

public class Management {
  private static HashMap<World, Structure> eventMap;

  public Management() { eventMap = new HashMap<World, Structure>(); }

  // Spawn the number of Enderdragons required for this event
  public static int spawnEnderdragons(JavaPlugin plugin, World w) {
    Structure s = eventMap.get(w);
    int added = 0;

    for (int i = 0; i < s.getConfig().getAmount(); i++) {
      w.spawnEntity(s.getConfig().getLocation(), EntityType.ENDER_DRAGON);
      added++;
    }

    plugin.getServer().broadcastMessage(ChatColor.DARK_RED +
                                        s.getConfig().getMsg());

    return added;
  }

  // Remove all Enderdragon entities from specified world
  public static int removeAllEnderdragons(World w) {
    int removed = 0;

    for (Entity entity : w.getEntities()) {
      if (entity instanceof EnderDragon && !entity.isDead()) {
        entity.remove();
        removed++;
      }
    }

    return removed;
  }

  public static HashMap<World, Structure> getEventMap() { return eventMap; }

  public static boolean isEventActive(World w) {
    return Management.getEventMap().containsKey(w);
  }

  private static boolean add(JavaPlugin plugin, World w) {
    if (!eventMap.containsKey(w)) {
      Config c = new Config(plugin, w);
      c.createWorldDefConfig();
      Structure s = new Structure(null, c);
      eventMap.put(w, s);
      return true;
    }

    return false;
  }

  public static int stop(JavaPlugin plugin, World w) {
    if (!eventMap.containsKey(w)) {
      plugin.getLogger().log(Level.WARNING, "No event found for this world");
      return 0;
    }

    Structure s = eventMap.get(w);
    Config c = s.getConfig();

    // Remove all Enderdragons
    int removed = removeAllEnderdragons(w);

    // Respawn any crystals
    if (c.getRespawnCrystals()) {
      try {
        EnderCrystals.respawn(plugin, w);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    // Respawn obsidian
    if (c.getRespawnObsidian()) {
      try {
        Obsidian.respawn(plugin, w);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    // Remove event from map
    eventMap.remove(w);

    return removed;
  }

  public static int start(JavaPlugin plugin, World w) {
    if (add(plugin, w)) {
      plugin.getLogger().log(
          Level.WARNING,
          "No event found for this world, creating default configuration");
    }

    int added = spawnEnderdragons(plugin, w);

    return added;
  }
}
