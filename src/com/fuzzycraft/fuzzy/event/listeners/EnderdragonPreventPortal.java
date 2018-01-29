/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-28 21:26:09
 * @Last Modified by:   FuzzyStatic
 * @Last Modified time: 2018-01-28 21:26:09
 */

package com.fuzzycraft.fuzzy.event.listeners;

import java.util.ArrayList;
import java.util.List;

import com.fuzzycraft.fuzzy.event.Management;
import com.fuzzycraft.fuzzy.event.Structure;
import com.fuzzycraft.fuzzy.event.files.Config;

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
import org.bukkit.plugin.java.JavaPlugin;

public class EnderdragonPreventPortal implements Listener {
  private JavaPlugin plugin;

  public EnderdragonPreventPortal(JavaPlugin plugin) { this.plugin = plugin; }

  @EventHandler(priority = EventPriority.HIGH)
  public void onEntityCreatePortal(EntityCreatePortalEvent event) {
    final Entity entity = event.getEntity();
    final World w = entity.getWorld();

    if (!Management.isEventActive(w)) {
      return;
    }

    final Structure s = Management.getEventMap().get(w);
    final Config c = s.getConfig();

    if (!(entity instanceof EnderDragon) || c.getCreatePortal()) {
      return;
    }

    List<BlockState> blocks = new ArrayList<BlockState>(event.getBlocks());

    for (BlockState block : event.getBlocks()) {
      // Remove any bedrock or ender portal blocks.
      if (block.getType() == Material.BEDROCK ||
          block.getType() == Material.ENDER_PORTAL) {
        blocks.remove(block);
      }

      // Remove Enderdragon egg.
      if (block.getType() == Material.DRAGON_EGG) {
        blocks.remove(block);

        // Drop egg
        if (c.getCreateEgg())
          entity.getWorld().dropItemNaturally(
              entity.getLocation(), new ItemStack(Material.DRAGON_EGG));
      }
    }

    if (blocks.size() != event.getBlocks().size()) {
      // Cancel current event
      event.setCancelled(true);

      // Create new event with whatever blocks are left
      this.plugin.getServer().getPluginManager().callEvent(
          new EntityCreatePortalEvent((LivingEntity)entity, blocks,
                                      event.getPortalType()));
    }
  }
}
