/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-20 16:57:18
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-28 21:37:36
 */

package com.fuzzycraft.fuzzy.event.commands;

import java.io.IOException;
import java.util.HashMap;

import com.fuzzycraft.fuzzy.event.Management;
import com.fuzzycraft.fuzzy.event.Structure;
import com.fuzzycraft.fuzzy.event.files.Config;
import com.fuzzycraft.fuzzy.event.listeners.EnderdragonCrystals;
import com.fuzzycraft.fuzzy.event.listeners.Obsidian;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Stop implements CommandExecutor {
  private JavaPlugin plugin;

  public Stop(JavaPlugin plugin) { this.plugin = plugin; }

  public boolean onCommand(CommandSender sender, Command cmd,
                           String commandLabel, String[] args) {
    if (commandLabel.equalsIgnoreCase(Cmd.STOP)) {
      if (sender instanceof ConsoleCommandSender ||
          (sender instanceof Player && sender.hasPermission(Perm.STOP))) {
        if (args.length > 0) {
          String sWorld = args[0];
          World w = Bukkit.getServer().getWorld(sWorld);

          if (w != null) {
            Management m = new Management(this.plugin, w);
            HashMap<World, Structure> eventMap = Management.getEventMap();
            Structure s = eventMap.get(w);
            Config c = s.getConfig();

            int removed = m.removeAllEnderdragons();
            switch (removed) {
            case 0:
              sender.sendMessage("No Enderdragons found/removed");
              break;
            default:
              sender.sendMessage(removed + " Enderdragons found/removed");
              break;
            }

            if (c.getRespawnCrystals()) {
              try {
                EnderdragonCrystals.respawn(plugin, w);
              } catch (IOException e) {
                e.printStackTrace();
              }
            }

            if (c.getRespawnObsidian()) {
              try {
                Obsidian.respawn(plugin, w);
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          }
        } else {
          sender.sendMessage("Command format: /erstop <world>");
        }

        return true;
      }
    }

    return false;
  }
}