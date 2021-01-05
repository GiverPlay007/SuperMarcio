package me.giverplay.supermario.world;

import java.awt.Graphics;

public class AirTile extends Tile
{
	public AirTile(int x, int y)
	{
		super(x, y, false, null);
	}
	
	@Override
	public void render(Graphics g)
	{
		// Nada :)
	}
}
