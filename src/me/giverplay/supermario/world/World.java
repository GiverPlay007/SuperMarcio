package me.giverplay.supermario.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import me.giverplay.supermario.Game;
import me.giverplay.supermario.utils.Cores;

public class World
{
	public static final int TILE_SIZE = 16;
	
	private static Tile[] tiles;
	
	private Game game;
	
	private int width;
	private int height;
	
	public int x1 = 0;
	public int x2;
	
	private BufferedImage background;
	
	public World(String path)
	{
		game = Game.getGame();
		
		initializeWorld(path);
		
		try
		{
			this.background = ImageIO.read(Game.class.getResourceAsStream("/Background.png"));
		} 
		catch (IOException e)
		{
			System.out.println("Background error");
		}
		
		this.x2 = width;
	}
	
	private void initializeWorld(String path)
	{
		try
		{
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			
			width = map.getWidth();
			height = map.getHeight();
			
			int lenght = width * height;
			int[] pixels = new int[lenght];
			
			tiles = new Tile[lenght];
			
			map.getRGB(0, 0, width, height, pixels, 0, width);
			
			for(int xx = 0; xx < width; xx++)
			{
				for(int yy = 0; yy < height; yy++)
				{
					int index = xx + (yy * width);
					
					tiles[index] = new AirTile(xx * TILE_SIZE, yy * TILE_SIZE);
					
					switch (pixels[index])
					{	
						case Cores.MAPA_GRAMA:
							tiles[index] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE);
							break;
							
						case Cores.MAPA_JOGADOR:
							game.getPlayer().setX(xx * TILE_SIZE);
							game.getPlayer().setY(yy * TILE_SIZE);
							break;
							
						default:
							break;
					}
				}
			}
			
		} catch (IOException e)
		{
			System.out.println("Falha ao ler o mapa");
		}
	}
	
	public void renderBackground(Graphics g)
	{
		x1--;
		x2--;
		
		if(x1 < -background.getWidth()) 
		{
			x1 = 0;
		}  
		
		if(x2 < 0) 
		{
			x2 = background.getWidth();
		}
		
		g.drawImage(background, x1, 0, background.getWidth(), Game.HEIGHT, null);
		g.drawImage(background, x2, 0, background.getWidth(), Game.HEIGHT, null);
	}
	
	public void render(Graphics g)
	{
		int xf = (Game.WIDTH * Game.SCALE >> 4);
		int yf = (Game.HEIGHT * Game.SCALE >> 4);
		
		
		for(int xx = 0; xx <= xf; xx++)
		{
			for(int yy = 0; yy <= yf; yy++)
			{
				
				if(xx < 0 || yy < 0 || xx >= width || yy >= height)
					continue;
				
				Tile tile = tiles[xx + yy * width];
				
				if(tile != null)
					tile.render(g);
			}
		}
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public Tile[] getTiles()
	{
		return tiles;
	}
	
	public static boolean canMove(int xn, int yn)
	{
		int x1 = xn / TILE_SIZE;
		int y1 = yn / TILE_SIZE;
		
		int x2 = (xn + TILE_SIZE -1) / TILE_SIZE;
		int y2 = yn / TILE_SIZE;
		
		int x3 = xn / TILE_SIZE;
		int y3 = (yn + TILE_SIZE -1) / TILE_SIZE;
		
		int x4 = (xn + TILE_SIZE -1) / TILE_SIZE;
		int y4 = (yn + TILE_SIZE -1) / TILE_SIZE;
		
		World world = Game.getGame().getWorld();
		
		int index1 = x1 + (y1 * world.getWidth());
		int index2 = x2 + (y2 * world.getWidth());
		int index3 = x3 + (y3 * world.getWidth());
		int index4 = x4 + (y4 * world.getWidth());
		
		return !(tiles[index1].isRigid()
				|| tiles[index2].isRigid()
				|| tiles[index3].isRigid()
				|| tiles[index4].isRigid());
	}
}
