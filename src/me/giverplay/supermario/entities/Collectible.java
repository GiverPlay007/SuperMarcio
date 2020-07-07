package me.giverplay.supermario.entities;

import java.awt.image.BufferedImage;

import me.giverplay.supermario.Game;

public class Collectible extends Entity
{
	public Collectible(double x, double y, BufferedImage sprite)
	{
		super(x, y, 10, 10, 0, sprite);
	}
	
	public void collect()
	{
		Game.getGame().getEntities().remove(this);
	}
}
