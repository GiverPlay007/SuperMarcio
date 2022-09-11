package me.giverplay.supermario.events;

import me.giverplay.supermario.Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Listeners implements KeyListener {
  private final Game game;

  public Listeners(Game game) {
    this.game = game;
    this.game.addKeyListener(this);
  }

  @Override
  public void keyPressed(KeyEvent event) {
    if(!game.isGameOver() && !game.isVictory()) {
      if(event.getKeyCode() == KeyEvent.VK_SPACE || event.getKeyCode() == KeyEvent.VK_W || event.getKeyCode() == KeyEvent.VK_UP) {
        game.getPlayer().handleJump();
      }

      if(event.getKeyCode() == KeyEvent.VK_RIGHT || event.getKeyCode() == KeyEvent.VK_D) {
        game.getPlayer().setWalkingRight(true);
      }

      if(event.getKeyCode() == KeyEvent.VK_LEFT || event.getKeyCode() == KeyEvent.VK_A) {
        game.getPlayer().setWalkingLeft(true);
      }
    } else {
      if(event.getKeyCode() == KeyEvent.VK_ENTER) {
        Game.handleRestart();
      }
    }
  }

  @Override
  public void keyReleased(KeyEvent event) {
    if(event.getKeyCode() == KeyEvent.VK_RIGHT || event.getKeyCode() == KeyEvent.VK_D) {
      game.getPlayer().setWalkingRight(false);
    }

    if(event.getKeyCode() == KeyEvent.VK_LEFT || event.getKeyCode() == KeyEvent.VK_A) {
      game.getPlayer().setWalkingLeft(false);
    }
  }

  @Override
  public void keyTyped(KeyEvent arg0) {

  }
}
