package me.giverplay.supermarcio.entities;

import me.giverplay.supermarcio.Game;
import me.giverplay.supermarcio.graphics.Spritesheet;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.Random;

import static me.giverplay.supermarcio.world.World.TILE_SIZE;

public class Entity {
  public static final BufferedImage[] SPRITE_PLAYER_RIGHT;
  public static final BufferedImage[] SPRITE_PLAYER_LEFT;
  public static final BufferedImage[] SPRITE_ENEMY;

  public static final BufferedImage SPRITE_COINS;
  public static final BufferedImage SPRITE_LIFEPACK;
  public static final BufferedImage SPRITE_LOCK;
  public static final BufferedImage SPRITE_NEXTLEVEL;
  public static final BufferedImage SPRITE_LIFE_FULL;
  public static final BufferedImage SPRITE_LIFE_NON_FULL;

  public static Comparator<Entity> sortDepth = Comparator.comparingInt(Entity::getDepth);

  protected static Random random = new Random();

  private static final Game game = Game.getGame();

  static {
    Spritesheet sprites = Game.getGame().getSpritesheet();

    SPRITE_PLAYER_RIGHT = new BufferedImage[3];
    SPRITE_PLAYER_LEFT = new BufferedImage[3];
    SPRITE_ENEMY = new BufferedImage[6];

    for(int i = 0; i < 3; i++) {
      SPRITE_PLAYER_RIGHT[i] = sprites.getSprite(i * TILE_SIZE, TILE_SIZE, TILE_SIZE, TILE_SIZE);
      SPRITE_PLAYER_LEFT[i] = sprites.getSprite(i * TILE_SIZE, 2 * TILE_SIZE, TILE_SIZE, TILE_SIZE);

      SPRITE_ENEMY[i] = sprites.getSprite(i * TILE_SIZE, TILE_SIZE * 3, TILE_SIZE, TILE_SIZE);
      SPRITE_ENEMY[i + 2] = sprites.getSprite((i + 2) * TILE_SIZE, TILE_SIZE * 3, TILE_SIZE, TILE_SIZE);
      SPRITE_ENEMY[i + 3] = sprites.getSprite((i + 3) * TILE_SIZE, TILE_SIZE * 3, TILE_SIZE, TILE_SIZE);
    }

    SPRITE_COINS = sprites.getSprite(0, 4 * TILE_SIZE, 2 * TILE_SIZE, 2 * TILE_SIZE);
    SPRITE_LIFEPACK = sprites.getSprite(TILE_SIZE * 3, TILE_SIZE * 4, TILE_SIZE, TILE_SIZE);
    SPRITE_NEXTLEVEL = sprites.getSprite(TILE_SIZE * 2, TILE_SIZE * 4, TILE_SIZE, TILE_SIZE);
    SPRITE_LOCK = sprites.getSprite(TILE_SIZE * 2, TILE_SIZE * 5, TILE_SIZE, TILE_SIZE);
    SPRITE_LIFE_FULL = sprites.getSprite(TILE_SIZE * 4, TILE_SIZE * 4, TILE_SIZE, TILE_SIZE);
    SPRITE_LIFE_NON_FULL = sprites.getSprite(TILE_SIZE * 5, TILE_SIZE * 4, TILE_SIZE, TILE_SIZE);
  }

  private final int width;
  private final int height;
  private final BufferedImage sprite;
  protected double x;
  protected double y;
  protected double speed;
  private int depth;

  public Entity(double x, double y, int width, int height, double speed, BufferedImage sprite) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.speed = speed;
    this.depth = 0;

    this.sprite = sprite;
  }

  public static boolean isCollifingEntity(Entity e1, Entity e2) {
    Rectangle e1m = new Rectangle(e1.getX(), e1.getY(), e1.getWidth(), e1.getHeight());
    Rectangle e2m = new Rectangle(e2.getX(), e2.getY(), e2.getWidth(), e2.getHeight());

    return e1m.intersects(e2m);
  }

  public void tick() {

  }

  public void render(Graphics g) {
    g.drawImage(sprite, getX() - game.getCamera().getX(), getY() - game.getCamera().getY(), width, height, null);
  }

  public void destroy() {
    game.removeEntity(this);
  }

  public void moveX(double d) {
    x += d;
  }

  public void moveY(double d) {
    y += d;
  }

  public int getX() {
    return (int) this.x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return (int) this.y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  public int getDepth() {
    return this.depth;
  }

  public void setDepth(int toSet) {
    this.depth = toSet;
  }

  public BufferedImage getSprite() {
    return this.sprite;
  }

  public double pointDistance(int x1, int y1, int x2, int y2) {
    return Math.sqrt((x2 - x1) * (x2 - x1) + ((y2 - y1) * (y2 - y1)));
  }
}
