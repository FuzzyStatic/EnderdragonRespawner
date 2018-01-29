/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-20 16:57:18
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-28 21:36:58
 */

package com.fuzzycraft.fuzzy.event.commands;

import com.fuzzycraft.fuzzy.event.Management;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Start implements CommandExecutor {
  private JavaPlugin plugin;

  public Start(JavaPlugin plugin) { this.plugin = plugin; }

  public boolean onCommand(CommandSender sender, Command cmd,
                           String commandLabel, String[] args) {
    if (commandLabel.equalsIgnoreCase(Cmd.START)) {
      if (sender instanceof ConsoleCommandSender ||
          (sender instanceof Player && sender.hasPermission(Perm.START))) {
        if (args.length > 0) {
          String sWorld = args[0];
          World w = Bukkit.getServer().getWorld(sWorld);

          if (w != null) {
            Management m = new Management(this.plugin, w);
            int removed = m.removeAllEnderdragons();

            switch (removed) {
            case 0:
              sender.sendMessage("No Enderdragons found/removed");
              break;
            default:
              sender.sendMessage(removed + " Enderdragons found/removed");
              break;
            }
          }
        } else {
          sender.sendMessage("Command format: /erstart <world>");
        }

        return true;
      }
    }

    return false;
  }
}