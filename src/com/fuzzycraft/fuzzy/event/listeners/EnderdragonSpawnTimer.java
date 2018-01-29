/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-18 10:11:30
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-29 15:54:07
 */

package com.fuzzycraft.fuzzy.event.listeners;

import java.io.IOException;

import com.fuzzycraft.fuzzy.event.Management;
import com.fuzzycraft.fuzzy.event.Structure;
import com.fuzzycraft.fuzzy.event.files.Config;

import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class EnderdragonSpawnTimer implements Listener {
  private JavaPlugin plugin;

  public EnderdragonSpawnTimer(JavaPlugin plugin) { this.plugin = plugin; }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onEntityDeath(EntityDeathEvent event) {
    final Entity entity = event.getEntity();
    final World w = entity.getWorld();

    if (!Management.isEventActive(w)) {
      return;
    }

    final Structure s = Management.getEventMap().get(w);
    final Config c = s.getConfig();

    if (!(entity instanceof EnderDragon)) {
      return;
    }

    if (!(event.getEntity() instanceof EnderDragon) || entity.isDead()) {
      return;
    }

    new BukkitRunnable() {
      public void run() {
        try {
          EnderCrystals.respawn(plugin, w);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
        .runTaskLater(this.plugin, c.getTime() * 20);

    new BukkitRunnable() {
      public void run() {
        try {
          Obsidian.respawn(plugin, w);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
        .runTaskLater(this.plugin, c.getTime() * 20);

    // Create the task anonymously to spawn Enderdragon and schedule to run it
    // once after specified time.
    new BukkitRunnable() {
      public void run() { Management.spawnEnderdragons(plugin, w); }
    }
        .runTaskLater(this.plugin, c.getTime() * 20);
  }
}