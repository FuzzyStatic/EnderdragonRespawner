/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-28 21:26:09
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-02-10 15:02:29
 */

package com.fuzzycraft.fuzzy.event.listeners;

import java.util.logging.Level;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.plugin.java.JavaPlugin;

@Deprecated
public class PreventPortal implements Listener {
  private JavaPlugin plugin;

  public PreventPortal(JavaPlugin plugin) { this.plugin = plugin; }

  @EventHandler(priority = EventPriority.HIGH)
  public void onCreatePortal(PortalCreateEvent event) {
    this.plugin.getLogger().log(Level.INFO, event.getEventName());
    this.plugin.getLogger().log(Level.INFO, event.getReason().toString());
  }
}
