/*
 * @Author: Allen Flickinger (allen.flickinger@gmail.com)
 * @Date: 2018-01-20 18:08:07
 * @Last Modified by: FuzzyStatic
 * @Last Modified time: 2018-02-03 08:54:51
 */

package com.fuzzycraft.fuzzy.event.files;

import java.time.Instant;

public class Parameter {
  protected static final boolean ACTIVE = false;
  protected static final boolean RESPAWN_CRYSTALS = true;
  protected static final boolean RESPAWN_OBSIDIAN = false;
  protected static final boolean CREATE_PORTAL = false;
  protected static final boolean CREATE_EGG = true;
  protected static final int AMOUNT = 1;
  protected static final int TIME = 21600; // In seconds
  protected static final double X = 0;
  protected static final double Y = 100;
  protected static final double Z = 0;
  protected static final String MSG = "The beast awakens from its slumber...";
  public static final long BEGINNING_OF_TIME = Instant.EPOCH.toEpochMilli();
}