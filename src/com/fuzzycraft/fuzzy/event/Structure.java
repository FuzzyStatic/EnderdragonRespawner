/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-20 17:06:03
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-01-29 16:13:04
 */

package com.fuzzycraft.fuzzy.event;

import com.fuzzycraft.fuzzy.event.files.Config;
import com.fuzzycraft.fuzzy.event.listeners.EnderdragonSpawnTimer;

public class Structure {
  private EnderdragonSpawnTimer est;
  private Config c;

  public Structure(EnderdragonSpawnTimer est, Config c) {
    this.est = est;
    this.c = c;
  }

  public void setEnderdragonSpawnTimer(EnderdragonSpawnTimer est) {
    this.est = est;
  }

  public void setConfig(Config c) { this.c = c; }

  public EnderdragonSpawnTimer getEnderdragonSpawnTimer() { return this.est; }

  public Config getConfig() { return this.c; }
}