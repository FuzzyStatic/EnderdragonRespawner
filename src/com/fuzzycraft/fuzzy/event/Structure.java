/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-20 17:06:03
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-28 14:15:28
 */

package com.fuzzycraft.fuzzy.event;

import com.fuzzycraft.fuzzy.event.files.Config;
import com.fuzzycraft.fuzzy.event.listeners.EnderdragonCrystals;
import com.fuzzycraft.fuzzy.event.listeners.EnderdragonPreventPortal;
import com.fuzzycraft.fuzzy.event.listeners.EnderdragonSpawnTimer;
import com.fuzzycraft.fuzzy.event.listeners.Obsidian;

public class Structure {
  private EnderdragonCrystals ec;
  private EnderdragonPreventPortal epp;
  private EnderdragonSpawnTimer est;
  private Obsidian o;
  private Config c;

  public Structure(EnderdragonCrystals ec, EnderdragonPreventPortal epp,
                   EnderdragonSpawnTimer est, Obsidian o, Config c) {
    this.ec = ec;
    this.epp = epp;
    this.est = est;
    this.o = o;
    this.c = c;
  }

  public void setEnderdragonCrystals(EnderdragonCrystals ec) { this.ec = ec; }

  public void setEnderdragonPreventPortal(EnderdragonPreventPortal epp) {
    this.epp = epp;
  }

  public void setEnderdragonSpawnTimer(EnderdragonSpawnTimer est) {
    this.est = est;
  }

  public void setObsidian(Obsidian o) { this.o = o; }

  public void setConfig(Config c) { this.c = c; }

  public EnderdragonCrystals getEnderdragonCrystals() { return this.ec; }

  public EnderdragonPreventPortal getEnderdragonPreventPortal() {
    return this.epp;
  }

  public EnderdragonSpawnTimer getEnderdragonSpawnTimer() { return this.est; }

  public Obsidian getObsidian() { return this.o; }

  public Config getConfig() { return this.c; }
}