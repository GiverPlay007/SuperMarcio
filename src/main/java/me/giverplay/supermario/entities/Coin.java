package me.giverplay.supermario.entities;

import java.awt.Graphics;

import me.giverplay.supermario.Game;
import me.giverplay.supermario.graphics.FutureRender;
import me.giverplay.supermario.sound.Sound;

public class Coin extends Collectible
{
	private Game game;
	
	public Coin(double x, double y)
	{
		super(x, y, null);
		
		game = Game.getGame();
	}
	
	@Override
	public void tick()
	{
		if(isCollifingEntity(this, Game.getGame().getPlayer()))
			collect();
	}
	
	@Override
	public void render(Graphics g)
	{
		game.addSmoothRender(new FutureRender()
		{
			@Override
			public void render(Graphics g)
			{
				g.drawImage(SPRITE_COINS, (getX() - game.getCamera().getX()) *  Game.SCALE, ((getY() + 3)- game.getCamera().getY()) * Game.SCALE, 20, 20, null);
			}
		});
	}
	
	@Override
	public void collect()
	{
		super.collect();
		game.addCoin();
		Sound.coin.play();
	}
}
