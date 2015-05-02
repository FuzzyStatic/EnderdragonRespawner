package com.fuzzycraft.fuzzy.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzzycraft.fuzzy.EnderdragonRespawner;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class EnderdragonPreventPortal implements Listener {

	public EnderdragonRespawner plugin;
	private World world;
	private boolean createPortal, createEgg;

	/**
	 * Constructs listener for EnderdragonPreventPortal.
	 * @param plugin
	 * @param world
	 * @param createPortal
	 * @param createEgg
	 */
	public EnderdragonPreventPortal(EnderdragonRespawner plugin, World world, boolean createPortal, boolean createEgg) {
		this.plugin = plugin;
		this.world = world;
		this.createPortal = createPortal;
		this.createEgg = createEgg;
	}
	
	/**
	 * Checks for portal creation by an Enderdragon in specified world. Removes portal blocks if specified.
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
	public void onEntityCreatePortal(EntityCreatePortalEvent event) {		
		Entity entity = event.getEntity();
		
		if(!(entity instanceof EnderDragon) || !(entity.getWorld() == this.world) || this.createPortal) {
			return;
		}
		
		List<BlockState> blocks = new ArrayList<BlockState>(event.getBlocks());

		for (BlockState block : event.getBlocks()) {
			// Remove any bedrock or ender portal blocks.
			if (block.getType() == Material.BEDROCK || block.getType() == Material.ENDER_PORTAL) {
				blocks.remove(block);
			}

			// Remove Enderdragon egg.
			if (block.getType() == Material.DRAGON_EGG) {
				blocks.remove(block);
				
				// Drop egg
				if (this.createEgg) {
					entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.DRAGON_EGG));
				}
			}
		}
		
		if (blocks.size() != event.getBlocks().size()) {
			// Cancel current event
			event.setCancelled(true);
		
			// Create new event with whatever blocks are left
			this.plugin.getServer().getPluginManager().callEvent(new EntityCreatePortalEvent((LivingEntity) entity, blocks, event.getPortalType()));
		}
	}
}
