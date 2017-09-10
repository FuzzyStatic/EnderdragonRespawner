package com.fuzzycraft.fuzzy.listeners;

import com.fuzzycraft.fuzzy.EnderdragonSpawner;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Allen Flickinger (allen.flickinger@gmail.com)
 */

public class EnderdragonSpawnTimer implements Listener {

    private JavaPlugin plugin;
    private EnderdragonSpawner es;
    private EnderdragonCrystals ec;
    private Obsidian o;

    public EnderdragonSpawnTimer(JavaPlugin plugin, EnderdragonSpawner es, EnderdragonCrystals ec, Obsidian o) {
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

        // Locations currently in memory. Respawn Ender Crystals and obsidian now just in case of server shutdown.
        if (this.ec != null) {
            this.ec.respawn();
        }

        if (this.o != null) {
            this.o.respawn();
        }

        // Create the task anonymously to spawn Enderdragon and schedule to run it once after specified time.
        new BukkitRunnable() {

            public void run() {
                es.spawnEnderdragon();
            }

        }.runTaskLater(this.plugin, this.es.getConfigParameters().getTime() * 20);
    }
}
