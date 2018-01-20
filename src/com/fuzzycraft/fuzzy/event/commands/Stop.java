/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-20 16:57:18
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-20 18:37:25
 */

package com.fuzzycraft.fuzzy.event.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.fuzzycraft.fuzzy.event.Enderdragon;

public class Stop implements CommandExecutor {
  private JavaPlugin plugin;

  public Stop(JavaPlugin plugin) { this.plugin = plugin; }

  public boolean onCommand(CommandSender sender, Command cmd,
                           String commandLabel, String[] args) {
    if (commandLabel.equalsIgnoreCase(Cmd.STOP)) {
      if (sender instanceof ConsoleCommandSender ||
          (sender instanceof Player && sender.hasPermission(Perm.STOP))) {
        String world = args[0];

        if (Bukkit.getServer().getWorld(world) != null) {
          Enderdragon e = new Enderdragon(this.plugin);
          int eRemoved = e.removeAll();

          switch (eRemoved) {
          case 0:
            sender.sendMessage("No Enderdragons found/removed");
          default:
            sender.sendMessage(eRemoved + " Enderdragons found/removed");
          }

          return true;
        }
      }
    }

    return false;
  }
}