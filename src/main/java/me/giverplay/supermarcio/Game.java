package me.giverplay.supermarcio;

import me.giverplay.supermarcio.entities.Entity;
import me.giverplay.supermarcio.entities.Player;
import me.giverplay.supermarcio.events.Listeners;
import me.giverplay.supermarcio.graphics.Camera;
import me.giverplay.supermarcio.graphics.FontUtils;
import me.giverplay.supermarcio.graphics.FutureRender;
import me.giverplay.supermarcio.graphics.Spritesheet;
import me.giverplay.supermarcio.graphics.UI;
import me.giverplay.supermarcio.sound.Sound;
import me.giverplay.supermarcio.world.World;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Game extends Canvas implements Runnable {
  public static final int WIDTH = 320;
  public static final int HEIGHT = 240;
  public static final int SCALE = 2;

  private final int maxLevel = 2;

  private static final long serialVersionUID = 1L;

  private static Game game;
  private static int FPS = 0;
  private final int maxGameOverFrames = 30;

  private List<Entity> entities;
  private List<Entity> toRemoveEntities;
  private List<FutureRender> smoothRenders;

  private BufferedImage image;
  private Thread thread;
  private JFrame frame;

  private Camera camera;
  private Spritesheet sprite;
  private World world;
  private Player player;
  private UI ui;

  private boolean isRunning = false;
  private boolean showGameOver = true;
  private boolean gameOver = false;
  private boolean nextLevel = false;
  private boolean victory = false;

  private int gameOverFrames = 0;
  private int coins = 0;
  private int maxCoins = 0;
  private int enemyC = 0;
  private int maxEnemyC = 0;
  private int level = 1;

  public Game() {
    setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

    setupFrame();
    setupAssets();
    initLevel();

    Sound.init();
  }

  public static Game getGame() {
    return game;
  }

  public static void main(String[] args) {
    Game game = new Game();
    game.start();
  }

  public static void handleRestart() {
    game.restart();
  }

  private void setupFrame() {
    frame = new JFrame("Game 04 - Super Márcio");
    frame.add(this);
    frame.setResizable(false);
    frame.setUndecorated(false);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    new Listeners(this);
  }

  private void setupAssets() {
    game = this;

    entities = new ArrayList<>();
    toRemoveEntities = new ArrayList<>();
    smoothRenders = new ArrayList<>();

    camera = new Camera(0, 0);
    sprite = new Spritesheet("/Spritesheet.png");

    ui = new UI();

    image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_BGR);

    nextLevel = false;
    gameOver = false;
    level = 1;
    victory = false;
  }

  private void initLevel() {
    coins = 0;
    maxCoins = 0;
    enemyC = 0;
    maxEnemyC = 0;

    smoothRenders.clear();
    toRemoveEntities.clear();
    entities.clear();

    player = new Player(1, 1, 16, 16);
    entities.add(player);
    world = new World(String.format("/World%d.png", level));
  }

  public synchronized void start() {
    isRunning = true;
    thread = new Thread(this);
    thread.start();
  }

  public synchronized void stop() {
    isRunning = false;

    try {
      thread.join();
    } catch(InterruptedException e) {
      e.printStackTrace();
    }
  }

  public synchronized void restart() {
    setupAssets();
    initLevel();
  }

  @Override
  public void run() {
    requestFocus();

    long lastTime = System.nanoTime();
    long timer = System.currentTimeMillis();

    double ticks = 60.0D;
    double ns = 1000000000 / ticks;
    double delta = 0.0D;

    int fps = 0;

    while(isRunning) {
      long now = System.nanoTime();
      delta += (now - lastTime) / ns;
      lastTime = now;

      if(delta >= 1) {
        tick();
        render();

        delta--;
        fps++;
      }

      if(System.currentTimeMillis() - timer >= 1000) {
        FPS = fps;
        fps = 0;
        timer += 1000;
      }
    }

    stop();
  }

  public synchronized void tick() {
    if(!gameOver && !victory) {
      entities.forEach(Entity::tick);
      toRemoveEntities.forEach(toRemoveEntity -> entities.remove(toRemoveEntity));
      toRemoveEntities.clear();
    }

    if(nextLevel) {
      initLevel();
    }
  }

  public synchronized void render() {
    if(nextLevel) {
      nextLevel = false;
      return;
    }

    BufferStrategy bs = this.getBufferStrategy();

    if(bs == null) {
      this.createBufferStrategy(3);
      return;
    }

    Graphics g = image.getGraphics();

    g.setColor(new Color(110, 200, 255));
    g.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);

    world.render(g);
    entities.sort(Entity.sortDepth);

    for(Entity entity : entities) entity.render(g);

    g.dispose();
    g = bs.getDrawGraphics();
    g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

    renderSmooth(g);

    if(gameOver || victory) {
      Graphics2D g2 = (Graphics2D) g;

      g2.setColor(new Color(0, 0, 0, 100));
      g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);

      String txt = gameOver ? "Game Over" : "Você Venceu!";
      g.setColor(Color.WHITE);
      g.setFont(FontUtils.getFont(32, Font.BOLD));
      g.drawString(txt, (WIDTH * SCALE - g.getFontMetrics(g.getFont()).stringWidth(txt)) / 2, HEIGHT * SCALE / 2);

      gameOverFrames++;

      if(gameOverFrames > maxGameOverFrames) {
        gameOverFrames = 0;
        showGameOver = !showGameOver;
      }

      if(showGameOver) {
        g.setFont(FontUtils.getFont(24, Font.BOLD));
        g.drawString("> Aperte ENTER para reiniciar <", (WIDTH * SCALE - g.getFontMetrics(g.getFont()).stringWidth("> Aperte ENTER para reiniciar <")) / 2, HEIGHT * SCALE / 2 + 28);
      }
    }

    bs.show();
  }

  public void renderSmooth(Graphics g) {
    smoothRenders.forEach(smoothRender -> smoothRender.render(g));
    smoothRenders.clear();

    ui.render(g);

    g.setColor(new Color(100, 100, 100));
    g.setFont(FontUtils.getFont(18, Font.PLAIN));

    g.setColor(Color.WHITE);
    g.setFont(FontUtils.getFont(11, Font.PLAIN));
    g.drawString("FPS: " + FPS, 2, 12);
  }

  public Player getPlayer() {
    return this.player;
  }

  public Spritesheet getSpritesheet() {
    return this.sprite;
  }

  public World getWorld() {
    return this.world;
  }

  public List<Entity> getEntities() {
    return this.entities;
  }

  public boolean isGameOver() {
    return this.gameOver;
  }

  public boolean isNextLevel() {
    return this.nextLevel;
  }

  public void handleGameOver() {
    this.gameOver = true;
    Sound.lose.play();
  }

  public Camera getCamera() {
    return this.camera;
  }

  public void addCoin() {
    this.coins++;
  }

  public void addMaxCoin() {
    this.maxCoins++;
  }

  public int getCoins() {
    return this.coins;
  }

  public int getMaxCoins() {
    return this.maxCoins;
  }

  public void addSmoothRender(FutureRender run) {
    smoothRenders.add(run);
  }

  public int getMaxEnemies() {
    return this.maxEnemyC;
  }

  public int getEnemyCount() {
    return this.enemyC;
  }

  public void addEnemyCount() {
    this.enemyC++;
  }

  public void addMaxEnemyCount() {
    this.maxEnemyC++;
  }

  public boolean canLevelUP() {
    return maxEnemyC - enemyC == 0 && maxCoins - coins == 0;
  }

  public void handleLevelUP() {
    if(level == maxLevel) {
      this.victory = true;
      return;
    }

    nextLevel = true;
    level++;
  }

  public boolean isVictory() {
    return victory;
  }

  public void removeEntity(Entity entity) {
    toRemoveEntities.add(entity);
  }
}
