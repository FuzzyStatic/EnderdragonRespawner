package com.fuzzycraft.fuzzy;

import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class EnderdragonChecker {

	private World world;
	
	/**
	 * Insert which world to check for Enderdragons.
	 * @param world
	 */
	public EnderdragonChecker(World world) {
		this.world = world;
	}
	
	/**
	 * Check for entity Enderdragons1 in specified world.
	 * @return
	 */
	public boolean exists() {
		for (Entity entity : this.world.getEntities()) {
			if (entity instanceof EnderDragon && !entity.isDead()) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Return current world.
	 * @return
	 */
	public World getWorld() {
		return this.world;
	}
	
}
