package com.fuzzycraft.fuzzy.utilities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
 
/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class SerializableLocation implements ConfigurationSerializable {
    
	private final World world;
	private final double x, y, z;
	private final float yaw, pitch;
	private final Location location;
 
	/**
	 * Location parameters from location.
	 * @param location
	 */
    public SerializableLocation(Location location) {
    	this.world = location.getWorld();
    	this.x = location.getBlockX();
    	this.y = location.getBlockY();
    	this.z = location.getBlockZ();
    	this.pitch = location.getPitch();
    	this.yaw = location.getYaw();
        this.location = location;
    }
 
    /**
     * Location parameters from map.
     * @param map
     */
    public SerializableLocation(Map<String, Object> map) {
    	this.world = Bukkit.getWorld(map.get("world").toString());
    	this.x = (Double) map.get("x");
    	this.y = (Double) map.get("y");
    	this.z = (Double) map.get("z");
    	this.pitch = Float.intBitsToFloat((Integer) map.get("pitch"));    
    	this.yaw = Float.intBitsToFloat((Integer) map.get("yaw"));
    	this.location = new Location(this.world, this.x, this.y, this.z, this.yaw, this.pitch);
    }
 
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("world", this.location.getWorld().getName());
        map.put("x", this.location.getX());
        map.put("y", this.location.getY());
        map.put("z", this.location.getZ());
        map.put("pitch", Float.floatToIntBits(this.location.getPitch()));
        map.put("yaw", Float.floatToIntBits(this.location.getYaw()));
        return map;
    }
    
    /**
     * Return location.
     * @return
     */
    public Location getLocation() {
        return this.location;
    }
    
    /**
     * Return world location.
     * @return
     */
    public World getWorld() {
        return this.world;
    }
}