/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-18 10:11:30
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-28 14:13:20
 */

package com.fuzzycraft.fuzzy.event.listeners;

import com.fuzzycraft.fuzzy.event.Management;
import com.fuzzycraft.fuzzy.event.Structure;
import java.io.IOException;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class EnderdragonSpawnTimer implements Listener {

  private JavaPlugin plugin;
  private Management m;
  private World w;
  private Structure s;

  public EnderdragonSpawnTimer(JavaPlugin plugin, Management m, World w) {
    this.plugin = plugin;
    this.m = m;
    this.w = w;
    this.s = this.m.getEventMap().get(this.w);
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onEntityDeath(EntityDeathEvent event) {
    if (!(event.getEntity() instanceof EnderDragon) ||
        this.m.enderdragonsExist()) {
      return;
    }

    new BukkitRunnable() {
      public void run() {
        try {
          s.getEnderdragonCrystals().respawn();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
        .runTaskLater(this.plugin, this.m.getConfigParameters().getTime() * 20);

    new BukkitRunnable() {
      public void run() {
        try {
          s.getEnderdragonCrystals().respawn();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

    }
        .runTaskLater(this.plugin, this.s.getConfig().getTime() * 20);

    // Create the task anonymously to spawn Enderdragon and schedule to run it
    // once after specified time.
    new BukkitRunnable() {
      public void run() { m.spawnEnderdragons(); }
    }
        .runTaskLater(this.plugin, this.s.getConfig().getTime() * 20);
  }
}
