package com.fuzzycraft.fuzzy;

import java.io.File;
import java.util.logging.Level;

import com.fuzzycraft.fuzzy.constants.Defaults;
import com.fuzzycraft.fuzzy.constants.Paths;
import com.fuzzycraft.fuzzy.utilities.*;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import com.fuzzycraft.fuzzy.configurations.ConfigParameters;
import com.fuzzycraft.fuzzy.listeners.EnderdragonCrystals;
import com.fuzzycraft.fuzzy.listeners.Obsidian;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author Allen Flickinger (allen.flickinger@gmail.com)
 */

public class EnderdragonRespawner extends JavaPlugin {
    public static EnderdragonSpawner es;
    public static EnderdragonChecker ec;

    private EnderdragonRespawner plugin = this;

    private DirectoryStructure ds;

    private EnderdragonCrystals enderCrystals;
    private Obsidian obsidian;

    public void onEnable() {
        // Check Directory Structure
        this.plugin.getLogger().log(Level.INFO, "Checking Directory Structure");
        this.ds = new DirectoryStructure(this);
        this.ds.createDirectoryStructure();

        // Create Default Configurations
        this.plugin.getLogger().log(Level.INFO, "Creating Default Configurations");
        this.ds.createWorldDefaultConfigurations();

        BukkitTask bukkitTask = new BukkitRunnable() {
            public void run() {
                for (File worldsFile : DirectoryStructure.getWorldsDirectory().listFiles()) {
                    if (worldsFile.isDirectory()) {
                        for (File file : worldsFile.listFiles()) {
                            if (file.getName().equals(Defaults.CONFIG_NAME) && file.isFile()) {
                                ConfigParameters cp = new ConfigParameters(plugin, file.toString());
                                if (!cp.getActive()) {
                                    SerializableVector sv = new SerializableVector(new YamlVector(getConfig(), (Paths.LOCATION)).getVectorMap());
                                    plugin.getLogger().log(Level.INFO, String.valueOf(sv));
                                }
                            }

                            plugin.getLogger().log(Level.INFO, file.toString());
                        }
                    }
                    //ConfigParameters cwp = new ConfigParameters(plugin, ds.getWorldsDirectory().toString(), file);

                    /*if (cwp.getLocation().getWorld() != null) {
                        SerializableLocation sc = new SerializableLocation(new YamlLocation(getConfig(), (Paths.LOCATION)).getLocationMap());
                        EnderdragonSpawner es = new EnderdragonSpawner(plugin, sc.getWorld(), sc.getLocation(), getConfig().getInt(Paths.AMOUNT), getConfig().getString(Paths.MSG));
                        EnderdragonChecker ec = new EnderdragonChecker(sc.getWorld());
                        PluginManager pm = getServer().getPluginManager();

                        if (getConfig().getBoolean(Paths.RESPAWN_CRYSTALS)) {
                            EnderdragonCrystals enderCrystals = new EnderdragonCrystals(plugin, ec.getWorld());
                            pm.registerEvents(enderCrystals, plugin);
                        }

                        if (getConfig().getBoolean(Paths.RESPAWN_OBSIDIAN)) {
                            Obsidian obsidian = new Obsidian(plugin, ec.getWorld());
                            pm.registerEvents(obsidian, plugin);
                        }

                        pm.registerEvents(new EnderdragonSpawnTimer(plugin, enderCrystals, obsidian, getConfig().getInt(Paths.TIME)), plugin);
                        pm.registerEvents(new EnderdragonPreventPortal(plugin, ec.getWorld(), getConfig().getBoolean(Paths.CREATE_PORTAL), getConfig().getBoolean(Paths.CREATE_EGG)), plugin);
                    }*/
                }
            }

        }.runTaskLater(this, 1);
    }

    public void onDisable() {

    }
}
