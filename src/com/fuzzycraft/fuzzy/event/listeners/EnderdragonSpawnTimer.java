/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-18 10:11:30
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-20 21:02:55
 */

package com.fuzzycraft.fuzzy.event.listeners;

import org.bukkit.entity.EnderDragon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzzycraft.fuzzy.event.Enderdragon;

import java.io.IOException;

public class EnderdragonSpawnTimer implements Listener {

  private JavaPlugin plugin;
  private Enderdragon es;
  private EnderdragonCrystals ec;
  private Obsidian o;

  public EnderdragonSpawnTimer(JavaPlugin plugin, Enderdragon es,
                               EnderdragonCrystals ec, Obsidian o) {
    this.plugin = plugin;
    this.es = es;
    this.ec = ec;
    this.o = o;
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onEntityDeath(EntityDeathEvent event) {
    if (!(event.getEntity() instanceof EnderDragon) || this.es.exists()) {
      return;
    }

    new BukkitRunnable() {
      public void run() {
        try {
          ec.respawn();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
        .runTaskLater(this.plugin,
                      this.es.getConfigParameters().getTime() * 20);

    new BukkitRunnable() {
      public void run() {
        try {
          ec.respawn();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

    }
        .runTaskLater(this.plugin,
                      this.es.getConfigParameters().getTime() * 20);

    // Create the task anonymously to spawn Enderdragon and schedule to run it
    // once after specified time.
    new BukkitRunnable() {
      public void run() { es.spawn(); }
    }
        .runTaskLater(this.plugin,
                      this.es.getConfigParameters().getTime() * 20);
  }
}
