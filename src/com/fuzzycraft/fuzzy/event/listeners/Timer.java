/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-18 10:11:30
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-02-10 14:39:10
 */

package com.fuzzycraft.fuzzy.event.listeners;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

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
import org.bukkit.scheduler.BukkitTask;

public class Timer implements Listener {
  private JavaPlugin plugin;

  public Timer(JavaPlugin plugin) { this.plugin = plugin; }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onEntityDeath(EntityDeathEvent event) {
    final Entity entity = event.getEntity();
    final World w = entity.getWorld();

    if (!Management.isActive(w)) {
      return;
    }

    if (!(entity instanceof EnderDragon) || Management.exists(w)) {
      return;
    }

    final Structure s = Management.getEventMap().get(w);
    final Config c = s.getConfig();

    if (c.getCreateEgg()) {
    }

    nextEvent(this.plugin, w,
              Management.getEventMap().get(w).getConfig().getTime());
  }

  public static void nextEvent(JavaPlugin plugin, World w, int time) {
    final Structure s = Management.getEventMap().get(w);
    final Config c = s.getConfig();

    BukkitTask btec = new BukkitRunnable() {
      public void run() { EnderCrystals.respawn(plugin, w); }
    }.runTaskLater(plugin, time * 20);
    Management.getEventMap().get(w).setBukkitTaskEnderCrystals(btec);

    BukkitTask bto = new BukkitRunnable() {
      public void run() { Obsidian.respawn(plugin, w); }
    }.runTaskLater(plugin, time * 20);
    Management.getEventMap().get(w).setBukkitObsidian(bto);

    // Create the task anonymously to spawn Enderdragon and schedule to run it
    // once after specified time.
    BukkitTask btse = new BukkitRunnable() {
      public void run() {
        Management.spawnEnderdragons(plugin, w);
        c.setNextStartTime(-1);
      }
    }.runTaskLater(plugin, time * 20);
    plugin.getLogger().log(Level.INFO, "Next event for " + w.getName() +
                                           " in " + time + " seconds");
    c.setNextStartTime(Instant.now().toEpochMilli() +
                       TimeUnit.SECONDS.toMillis(time));
    Management.getEventMap().get(w).setBukkitTaskSpawnEnderdragons(btse);
  }
}
