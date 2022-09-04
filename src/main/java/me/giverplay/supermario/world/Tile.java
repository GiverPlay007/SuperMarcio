package me.giverplay.supermario.world;

import me.giverplay.supermario.Game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import static me.giverplay.supermario.world.World.TILE_SIZE;

public class Tile {
  private static final Game game = Game.getGame();

  public static BufferedImage TILE_PAREDE = game.getSpritesheet().getSprite(0, 0, TILE_SIZE, TILE_SIZE);
  public static BufferedImage TILE_PLATFORM = game.getSpritesheet().getSprite(TILE_SIZE * 2, 0, TILE_SIZE, TILE_SIZE);
  public static BufferedImage TILE_PLATFORM_DOWN = game.getSpritesheet().getSprite(TILE_SIZE, 0, TILE_SIZE, TILE_SIZE);

  private final BufferedImage sprite;

  private final boolean isRigid;

  private final int x;
  private final int y;

  public Tile(int x, int y, boolean isRigid, BufferedImage sprite) {
    this.x = x;
    this.y = y;
    this.sprite = sprite;
    this.isRigid = isRigid;
  }

  public void render(Graphics g) {
    g.drawImage(sprite, x - game.getCamera().getX(), y - game.getCamera().getY(), null);
  }

  public boolean isRigid() {
    return this.isRigid;
  }
}
