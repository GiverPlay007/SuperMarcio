package me.giverplay.supermarcio.entities;

import me.giverplay.supermarcio.Game;
import me.giverplay.supermarcio.sound.Sound;

import java.awt.Graphics;

public class NextLevel extends Collectible {
  private final Game game;

  public NextLevel(double x, double y) {
    super(x, y, SPRITE_NEXTLEVEL);
    this.game = Game.getGame();
  }

  @Override
  public void tick() {
    if(!game.canLevelUP())
      return;

    if(isCollifingEntity(this, game.getPlayer()))
      collect();
  }

  @Override
  public void collect() {
    super.collect();
    Sound.up.play();
    game.handleLevelUP();
  }

  @Override
  public void render(Graphics g) {
    super.render(g);

    if(!game.canLevelUP()) {
      g.drawImage(SPRITE_LOCK, (getX()) - game.getCamera().getX(), (getY() - 14) - game.getCamera().getY(), 16, 16, null);
    }
  }

}
