/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-20 16:57:18
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-21 13:51:55
 */

package com.fuzzycraft.fuzzy.event.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.fuzzycraft.fuzzy.event.Enderdragon;
import com.fuzzycraft.fuzzy.event.files.Config;

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
          World world = Bukkit.getServer().getWorld(sWorld);

          if (world != null) {
            Config c = new Config(this.plugin, world);
            Enderdragon e = new Enderdragon(this.plugin, c);
            int eRemoved = e.removeAll();

            switch (eRemoved) {
            case 0:
              sender.sendMessage("No Enderdragons found/removed");
              break;
            default:
              sender.sendMessage(eRemoved + " Enderdragons found/removed");
              break;
            }
          }
        } else {
          sender.sendMessage("Command format: /start <world>");
        }

        return true;
      }
    }

    return false;
  }
}