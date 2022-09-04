package me.giverplay.supermario.world;

import me.giverplay.supermario.Game;
import me.giverplay.supermario.entities.Coin;
import me.giverplay.supermario.entities.Enemy;
import me.giverplay.supermario.entities.LifePack;
import me.giverplay.supermario.entities.NextLevel;
import me.giverplay.supermario.graphics.Camera;
import me.giverplay.supermario.utils.Cores;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class World {
  public static final int TILE_SIZE = 16;

  private static Tile[] tiles;

  private final Game game;
  private final Camera camera;

  private int width;
  private int height;

  public World(String path) {
    game = Game.getGame();
    camera = game.getCamera();

    initializeWorld(path);
  }

  public static boolean canMove(int xn, int yn) {
    int x1 = xn / TILE_SIZE;
    int y1 = yn / TILE_SIZE;

    int x2 = (xn + TILE_SIZE - 1) / TILE_SIZE;
    int y2 = yn / TILE_SIZE;

    int x3 = xn / TILE_SIZE;
    int y3 = (yn + TILE_SIZE - 1) / TILE_SIZE;

    int x4 = (xn + TILE_SIZE - 1) / TILE_SIZE;
    int y4 = (yn + TILE_SIZE - 1) / TILE_SIZE;

    World world = Game.getGame().getWorld();

    int index1 = x1 + (y1 * world.getWidth());
    int index2 = x2 + (y2 * world.getWidth());
    int index3 = x3 + (y3 * world.getWidth());
    int index4 = x4 + (y4 * world.getWidth());

    return !(tiles[index1].isRigid()
      || tiles[index2].isRigid()
      || tiles[index3].isRigid()
      || tiles[index4].isRigid());
  }

  private void initializeWorld(String path) {
    try {
      BufferedImage map = ImageIO.read(getClass().getResource(path));

      width = map.getWidth();
      height = map.getHeight();

      int lenght = width * height;
      int[] pixels = new int[lenght];

      tiles = new Tile[lenght];

      map.getRGB(0, 0, width, height, pixels, 0, width);

      for(int xx = 0; xx < width; xx++) {
        for(int yy = 0; yy < height; yy++) {
          int index = xx + (yy * width);

          tiles[index] = new AirTile(xx * TILE_SIZE, yy * TILE_SIZE);

          switch(pixels[index]) {
            case Cores.TILE_GRAMA:
              tiles[index] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE, yy == 0 || pixels[xx + (yy - 1) * width] != Cores.TILE_GRAMA);
              break;

            case Cores.LOC_JOGADOR:
              game.getPlayer().setX(xx * TILE_SIZE);
              game.getPlayer().setY(yy * TILE_SIZE);
              break;

            case Cores.LOC_ENEMY:
              game.getEntities().add(new Enemy(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, 1));
              game.addMaxEnemyCount();
              break;

            case Cores.LOC_COIN:
              game.getEntities().add(new Coin(xx * TILE_SIZE, yy * TILE_SIZE));
              game.addMaxCoin();
              break;

            case Cores.LOC_CHECK:
              game.getEntities().add(new NextLevel(xx * TILE_SIZE, yy * TILE_SIZE));
              break;

            case Cores.LOC_LIFEPACK:
              game.getEntities().add(new LifePack(xx * TILE_SIZE, yy * TILE_SIZE));
              break;

            default:
              break;
          }
        }
      }

    } catch(IOException e) {
      System.out.println("Falha ao ler o mapa");
    }
  }

  public void render(Graphics g) {
    int xs = camera.getX() >> 4;
    int ys = camera.getY() >> 4;
    int xf = (camera.getX() + Game.WIDTH) >> 4;
    int yf = (camera.getX() + Game.HEIGHT) >> 4;


    for(int xx = xs; xx <= xf; xx++) {
      for(int yy = ys; yy <= yf; yy++) {

        if(xx < xs || yy < ys || xx >= width || yy >= height)
          continue;

        Tile tile = tiles[xx + yy * width];

        if(tile != null)
          tile.render(g);
      }
    }
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  public Tile[] getTiles() {
    return tiles;
  }
}
