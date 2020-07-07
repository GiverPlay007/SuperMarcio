package me.giverplay.supermario.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import me.giverplay.supermario.Game;
import me.giverplay.supermario.entities.Entity;

public class UI
{
	private Game game;
	
	public UI()
	{
		game = Game.getGame();
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.YELLOW);
		g.setFont(FontUtils.getFont(16, Font.PLAIN));
		
		String txt = game.getCoins()  + "/" + game.getMaxCoins();
		int loc = Game.WIDTH * Game.SCALE - FontUtils.stringWidth(g, txt);
		
		g.drawString(txt, loc - 4, 22);
		g.drawImage(Entity.SPRITE_COINS, loc - 32, 4, 24, 24, null);
	}
}
