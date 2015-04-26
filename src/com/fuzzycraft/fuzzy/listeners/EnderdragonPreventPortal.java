package com.fuzzycraft.fuzzy.listeners;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCreatePortalEvent;

import com.fuzzycraft.fuzzy.EnderdragonRespawner;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class EnderdragonPreventPortal implements Listener {

	public EnderdragonRespawner plugin;
	private World checkWorld;
	private boolean createPortal, createEgg;

	/**
	 * Constructs listener for EnderdragonPreventPortal.
	 * @param plugin
	 * @param world
	 */
	public EnderdragonPreventPortal(EnderdragonRespawner plugin, World checkWorld, boolean createPortal, boolean createEgg) {
		this.plugin = plugin;
		this.checkWorld = checkWorld;
		this.createPortal = createPortal;
		this.createEgg = createEgg;
	}
	
	/**
	 * Checks for portal creation by an Enderdragon in specified world. Removes portal blocks if specified.
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true)
	public void onEntityCreatePortal(EntityCreatePortalEvent event) {
		if(!(event.getEntity() instanceof EnderDragon) && !(event.getEntity().getWorld() == this.checkWorld) && this.createPortal) {
			return;
		}

		for (BlockState block : event.getBlocks()) {
			// Remove any bedrock or ender portal blocks.
			if (block.getType() == Material.BEDROCK || block.getType() == Material.ENDER_PORTAL) {
				event.getBlocks().remove(block);
			}
			
			// Remove Enderdragon egg if specified.
			if(!createEgg) {
				if (block.getType() == Material.DRAGON_EGG) {
					event.getBlocks().remove(block);
				}
			}		
		}
	}
}
