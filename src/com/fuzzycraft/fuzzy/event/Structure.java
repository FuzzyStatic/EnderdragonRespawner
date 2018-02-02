/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-20 17:06:03
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-29 16:13:04
 */

package com.fuzzycraft.fuzzy.event;

import com.fuzzycraft.fuzzy.event.files.Config;
import org.bukkit.scheduler.BukkitTask;

public class Structure {
  private BukkitTask btse;
  private BukkitTask btec;
  private BukkitTask bto;
  private Config c;

  public Structure(BukkitTask btse, BukkitTask btec, BukkitTask bto, Config c) {
    this.btse = btse;
    this.c = c;
  }

  public void setBukkitTaskSpawnEnderdragons(BukkitTask btse) {
    this.btse = btse;
  }

  public void setBukkitTaskEnderCrystals(BukkitTask btec) { this.btse = btec; }

  public void setBukkitObsidian(BukkitTask bto) { this.btse = bto; }

  public void setConfig(Config c) { this.c = c; }

  public BukkitTask getBukkitTaskSpawnEnderdragons() { return this.btse; }

  public BukkitTask getBukkitTaskEnderCrystals() { return this.btec; }

  public BukkitTask getBukkitTaskObsidian() { return this.bto; }

  public Config getConfig() { return this.c; }

  public boolean cancelTasks() {
    boolean anyTaskCancelled = false;

    if (this.btse != null) {
      this.btse.cancel();
      anyTaskCancelled = true;
    }

    if (this.btec != null) {
      this.btec.cancel();
      anyTaskCancelled = true;
    }

    if (this.bto != null) {
      this.bto.cancel();
      anyTaskCancelled = true;
    }

    return anyTaskCancelled;
  }
}