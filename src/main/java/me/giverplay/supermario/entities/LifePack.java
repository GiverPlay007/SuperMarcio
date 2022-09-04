package me.giverplay.supermario.entities;

import me.giverplay.supermario.Game;
import me.giverplay.supermario.sound.Sound;

public class LifePack extends Collectible {

  public LifePack(double x, double y) {
    super(x, y, SPRITE_LIFEPACK);
  }

  @Override
  public void tick() {
    if(isCollifingEntity(this, Game.getGame().getPlayer()))
      collect();
  }

  @Override
  public void collect() {
    super.collect();
    Sound.life.play();
    Game.getGame().getPlayer().modifyLife(+1);
  }
}
