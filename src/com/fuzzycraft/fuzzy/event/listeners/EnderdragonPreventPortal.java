package com.fuzzycraft.fuzzy.event.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
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

import com.fuzzycraft.fuzzy.event.files.Config;

/**
 * @author Allen Flickinger (allen.flickinger@gmail.com)
 */

public class EnderdragonPreventPortal implements Listener {

  private JavaPlugin plugin;
  private Config c;

  public EnderdragonPreventPortal(JavaPlugin plugin, Config c) {
    this.plugin = plugin;
    this.c = c;
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onEntityCreatePortal(EntityCreatePortalEvent event) {
    Entity entity = event.getEntity();

    if (!(entity instanceof EnderDragon) ||
        !(entity.getWorld() == this.c.getWorld()) || this.c.getCreatePortal()) {
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
        if (this.c.getCreateEgg())
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
