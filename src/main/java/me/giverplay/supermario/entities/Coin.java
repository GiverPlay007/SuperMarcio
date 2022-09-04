package me.giverplay.supermario.entities;

import me.giverplay.supermario.Game;
import me.giverplay.supermario.sound.Sound;

import java.awt.Graphics;

public class Coin extends Collectible {
  private final Game game;

  public Coin(double x, double y) {
    super(x, y, null);

    game = Game.getGame();
  }

  @Override
  public void tick() {
    if(isCollifingEntity(this, Game.getGame().getPlayer()))
      collect();
  }

  @Override
  public void render(Graphics g) {
    int xx = (getX() - game.getCamera().getX()) * Game.SCALE;
    int yy = ((getY() + 3) - game.getCamera().getY()) * Game.SCALE;

    game.addSmoothRender(g1 -> g1.drawImage(SPRITE_COINS, xx, yy, 20, 20, null));
  }

  @Override
  public void collect() {
    super.collect();
    game.addCoin();
    Sound.coin.play();
  }
}
