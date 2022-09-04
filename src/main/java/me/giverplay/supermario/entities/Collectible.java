package me.giverplay.supermario.entities;

import me.giverplay.supermario.Game;

import java.awt.image.BufferedImage;

public class Collectible extends Entity {

  public Collectible(double x, double y, BufferedImage sprite) {
    super(x, y, 16, 16, 0, sprite);
    setDepth(0);
  }

  public void collect() {
    Game.getGame().removeEntity(this);
  }
}
