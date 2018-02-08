/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-20 16:57:18
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-02-03 10:04:57
 */

package com.fuzzycraft.fuzzy.event.command;

import com.fuzzycraft.fuzzy.event.files.Config;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Management implements CommandExecutor {
  private JavaPlugin plugin;

  public Management(JavaPlugin plugin) { this.plugin = plugin; }

  public boolean onCommand(CommandSender sender, Command cmd,
                           String commandLabel, String[] args) {
    if (commandLabel.equalsIgnoreCase(Arg.BASE)) {
      if (sender instanceof ConsoleCommandSender ||
          (sender instanceof Player && sender.hasPermission(Perm.BASE))) {
        if (args.length > 0) {
          if (args.length > 1) {
            World w = Bukkit.getServer().getWorld(args[1]);

            if (w != null) {
              Config c = com.fuzzycraft.fuzzy.event.Management.getEventMap()
                             .get(w)
                             .getConfig();

              switch (args[0]) {
              /*case "start":
                if (c.getActive()) {
                  this.start(sender, w);
                  break;
                }

                sender.sendMessage(
                    "Event not active. Activate with /er active <world> true");
                break;*/
              case "stop":
                this.stop(sender, w);
                break;
              case "restart":
                this.stop(sender, w);

                if (c.getActive()) {
                  this.start(sender, w);
                  break;
                }

                sender.sendMessage(
                    "Event not active. Activate with /er active <world> true");
                break;
              case "active":
                if (args.length > 2) {
                  switch (args[2]) {
                  case "true":
                    c.setActive(true);
                    sender.sendMessage("Event for " + w.getName() +
                                       " activated");
                    sender.sendMessage(
                        "Make sure to run /er restart <world> to start event");
                    break;
                  case "false":
                    c.setActive(false);
                    sender.sendMessage("Event for " + w.getName() +
                                       " deactivated");
                    sender.sendMessage(
                        "Make sure to run /er stop <world> to stop event completely");
                    break;
                  }
                  break;
                }

                sender.sendMessage("Usage: /er active <world> [true|false]");
                break;
              default:
                sender.sendMessage("Usage: /er [restart|stop] <world>");
                break;
              }

              return true;
            }

            sender.sendMessage("World not recognized");
            return true;
          }

          sender.sendMessage("Usage: /er [restart|stop] <world>");
          return true;
        }
      }
    }

    return false;
  }

  private void stop(CommandSender sender, World w) {
    int removedStop =
        com.fuzzycraft.fuzzy.event.Management.stop(this.plugin, w);
    sender.sendMessage("Previous event for " + w.getName() + " stopped");
    switch (removedStop) {
    case 0:
      sender.sendMessage("No Enderdragons found/removed");
      break;
    default:
      sender.sendMessage(removedStop + " Enderdragons found/removed");
      break;
    }
  }

  private void start(CommandSender sender, World w) {
    int added = com.fuzzycraft.fuzzy.event.Management.start(this.plugin, w);
    sender.sendMessage("New event for " + w.getName() + " started");
    switch (added) {
    case 0:
      sender.sendMessage("No Enderdragons spawned");
      break;
    default:
      sender.sendMessage(added + " Enderdragons spawned");
      break;
    }
  }
}