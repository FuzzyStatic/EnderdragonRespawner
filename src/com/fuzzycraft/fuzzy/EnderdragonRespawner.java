package com.fuzzycraft.fuzzy;

import java.io.File;
import java.util.logging.Level;

import com.fuzzycraft.fuzzy.utilities.ConfigParameters;
import com.fuzzycraft.fuzzy.listeners.EnderdragonCrystals;
import com.fuzzycraft.fuzzy.listeners.EnderdragonPreventPortal;
import com.fuzzycraft.fuzzy.listeners.EnderdragonSpawnTimer;
import com.fuzzycraft.fuzzy.listeners.Obsidian;
import com.fuzzycraft.fuzzy.utilities.*;

import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Allen Flickinger (allen.flickinger@gmail.com)
 */

public class EnderdragonRespawner extends JavaPlugin {
    private EnderdragonRespawner plugin = this;

    public void onEnable() {
        // Check Directory Structure
        this.plugin.getLogger().log(Level.INFO, "Checking Directory Structure");
        DirectoryStructure ds = new DirectoryStructure(this);
        ds.createDirectoryStructure();

        // Create Default Configurations
        ds.createWorldDefaultConfigurations();

        new BukkitRunnable() {
            public void run() {
                for (File worldsFile : DirectoryStructure.getWorldsDirectory().listFiles()) {
                    if (worldsFile.isDirectory()) {
                        World world = plugin.getServer().getWorld(worldsFile.getName());

                        if (world != null) {
                            for (File file : worldsFile.listFiles()) {
                                if (file.getName().equals(DirectoryStructure.CONFIG_NAME) && file.isFile()) {
                                    ConfigParameters cp = new ConfigParameters(plugin, world, file.toString());

                                    if (cp.getActive()) {
                                        EnderdragonCrystals ec = null;
                                        Obsidian o = null;

                                        plugin.getLogger().log(Level.INFO, "Activating Configuration For World: " + worldsFile.getName());
                                        PluginManager pm = getServer().getPluginManager();
                                        EnderdragonSpawner es = new EnderdragonSpawner(plugin, cp);

                                        if (cp.getRespawnCrystals()) {
                                            plugin.getLogger().log(Level.INFO, "Activating Enderdragon Crystal Respawn For World: " + worldsFile.getName());
                                            ec = new EnderdragonCrystals(plugin, cp.getWorld());
                                            pm.registerEvents(ec, plugin);
                                        }

                                        if (cp.getRespawnObsidian()) {
                                            plugin.getLogger().log(Level.INFO, "Activating Obsidian Respawn For World: " + worldsFile.getName());
                                            o = new Obsidian(plugin, cp.getWorld());
                                            pm.registerEvents(o, plugin);
                                        }

                                        plugin.getLogger().log(Level.INFO, "Activating Listeners For World: " + worldsFile.getName());
                                        pm.registerEvents(new EnderdragonSpawnTimer(plugin, es, ec, o), plugin);
                                        pm.registerEvents(new EnderdragonPreventPortal(plugin, cp), plugin);
                                    }
                                }
                            }
                        } else {
                            plugin.getLogger().log(Level.WARNING, "World " + worldsFile.getName() + " Does Not Exist");
                        }
                    }
                }
            }

        }.runTaskLater(this, 1);
    }

    public void onDisable() {

    }
}
