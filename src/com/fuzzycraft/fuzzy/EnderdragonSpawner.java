package com.fuzzycraft.fuzzy;

import com.fuzzycraft.fuzzy.configurations.ConfigParameters;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Allen Flickinger (allen.flickinger@gmail.com)
 */

public class EnderdragonSpawner {

    private JavaPlugin plugin;
    private ConfigParameters cp;

    public EnderdragonSpawner(JavaPlugin plugin, ConfigParameters cp) {
        this.plugin = plugin;
        this.cp = cp;
    }

    public void spawnEnderdragon() {
        for (int i = 0; i < this.cp.getAmount(); i++) {
            this.cp.getWorld().spawnEntity(this.cp.getLocation(), EntityType.ENDER_DRAGON);
        }

        this.plugin.getServer().broadcastMessage(ChatColor.DARK_RED + this.cp.getMsg());
    }

    public boolean exists() {
        for (Entity entity : this.cp.getWorld().getEntities()) {
            if (entity instanceof EnderDragon && !entity.isDead()) {
                return true;
            }
        }

        return false;
    }


    public ConfigParameters getConfigParameters() {
        return this.cp;
    }
}
