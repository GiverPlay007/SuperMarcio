package me.giverplay.supermarcio.entities;

import me.giverplay.supermarcio.Game;
import me.giverplay.supermarcio.sound.Sound;
import me.giverplay.supermarcio.world.World;

import java.awt.Graphics;

import static me.giverplay.supermarcio.world.World.canMove;

public class Enemy extends Entity {
  private final Game game;
  private final int maxAnimF = 10;

  private boolean changeDir = false;

	private int coeff;
  private int anim;
  private int maxAnim = 3;
  private int animF = 0;

  public Enemy(double x, double y, int width, int height, double speed) {
    super(x, y, width, height, speed, null);

    setDepth(1);

    coeff = random.nextInt(6);
    coeff = coeff == 1 ? 0 : coeff == 3 ? 2 : coeff == 5 ? 4 : coeff;

    anim = coeff;
    maxAnim = coeff + 2;

    game = Game.getGame();
  }

  @Override
  public void tick() {
    if(isCollifingEntity(this, game.getPlayer())) {
      if(game.getPlayer().fallingRelative())
        destroy();
      else
        game.getPlayer().damage();
    }

    if(canMove(getX(), (int) (y + speed * 2)))
      moveY(speed * 2);

    if(canMove(getX() + (changeDir ? -World.TILE_SIZE : World.TILE_SIZE), (int) (y + 1))
      || !canMove(getX() + ((int) -speed), getY())) {
      changeDir = !changeDir;
    }

    moveX(changeDir ? -speed : speed);
  }

  @Override
  public void render(Graphics g) {
    animF++;

    if(animF >= maxAnimF) {
      anim++;
      animF = 0;

      if(anim >= maxAnim) {
        anim = coeff;
      }
    }

    g.drawImage(SPRITE_ENEMY[anim], getX() - game.getCamera().getX(), getY() - game.getCamera().getY(), null);
  }

  @Override
  public void destroy() {
    super.destroy();
    Sound.hit2.play();
    game.addEnemyCount();
  }
}
