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
	 * Constructor
	 * @param world
	 */
	public EnderdragonChecker (World world) {
		this.world = world;
	}
	
	/**
	 * Check for entity Enderdragon in this.world
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
	
}
