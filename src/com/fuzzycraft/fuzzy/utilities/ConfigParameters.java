package com.fuzzycraft.fuzzy.utilities;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

/**
 * @author Allen Flickinger (allen.flickinger@gmail.com)
 */

public class ConfigParameters {

    private static final String PATH_ACTIVE = "active";
    private static final String PATH_LOCATION = "spawnLocation";
    private static final String PATH_AMOUNT = "numberOfDragons";
    private static final String PATH_TIME = "respawnTime";
    private static final String PATH_MSG = "spawnAlert";
    private static final String PATH_RESPAWN = "respawn";
    private static final String PATH_RESPAWN_CRYSTALS = PATH_RESPAWN + ".enderCrystals";
    private static final String PATH_RESPAWN_OBSIDIAN = PATH_RESPAWN + ".obsidian";
    private static final String PATH_CREATE_PORTAL = "createPortal";
    private static final String PATH_CREATE_EGG = "createEgg";

    private static final boolean ACTIVE = false;
    private static final double X = 0;
    private static final double Y = 20;
    private static final double Z = 0;
    private static final int AMOUNT = 1;
    private static final int TIME = 21600; // In seconds
    private static final String MSG = "The beast awakens from his slumber...";
    private static final boolean RESPAWN_CRYSTALS = true;
    private static final boolean RESPAWN_OBSIDIAN = false;
    private static final boolean CREATE_PORTAL = false;
    private static final boolean CREATE_EGG = true;

    private ConfigAccessor configAccessor;
    private FileConfiguration config;
    private World world;

    public ConfigParameters(JavaPlugin plugin, World world, String filename) {
        if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Plugin must be initialized!");
        }

        this.world = world;
        this.configAccessor = new ConfigAccessor(plugin, filename);
        this.config = configAccessor.getConfig();
    }

    public void setDefaults() {
        this.config.set(PATH_ACTIVE, ACTIVE);
        Vector vector = new Vector(X, Y, Z);
        this.config.set(PATH_LOCATION, new SerializableVector(vector).serialize());
        this.config.set(PATH_AMOUNT, AMOUNT);
        this.config.set(PATH_TIME, TIME);
        this.config.set(PATH_MSG, MSG);
        this.config.set(PATH_RESPAWN_CRYSTALS, RESPAWN_CRYSTALS);
        this.config.set(PATH_RESPAWN_OBSIDIAN, RESPAWN_OBSIDIAN);
        this.config.set(PATH_CREATE_PORTAL, CREATE_PORTAL);
        this.config.set(PATH_CREATE_EGG, CREATE_EGG);
        this.configAccessor.saveConfig();
    }

    public World getWorld() {
        return this.world;
    }

    public boolean getActive() {
        return this.config.getBoolean(PATH_ACTIVE);
    }

    public Location getLocation() {
        YamlVector yv = new YamlVector(this.config, PATH_LOCATION);
        SerializableVector sv = new SerializableVector(yv.getVectorMap());
        Vector vector = sv.getVector();
        return new Location(this.world, vector.getX(), vector.getY(), vector.getZ());
    }

    public int getAmount() {
        return this.config.getInt(PATH_AMOUNT);
    }

    public int getTime() {
        return this.config.getInt(PATH_TIME);
    }

    public String getMsg() {
        return this.config.getString(PATH_MSG);
    }

    public boolean getRespawnCrystals() {
        return this.config.getBoolean(PATH_RESPAWN_CRYSTALS);
    }

    public boolean getRespawnObsidian() {
        return this.config.getBoolean(PATH_RESPAWN_OBSIDIAN);
    }

    public boolean getCreatePortal() {
        return this.config.getBoolean(PATH_CREATE_PORTAL);
    }

    public boolean getCreateEgg() {
        return this.config.getBoolean(PATH_CREATE_EGG);
    }
}
