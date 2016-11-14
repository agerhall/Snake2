package lab1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * A triangular tile designed to represent the head of the Snake
 * Paints a filed triangle on a specified area of the screen
 * 
 * Whenever the object should paint itself,
 * it is told what size and position that
 * should be used to paint it.
 */

public class SnakeHeadTile extends GameTile{
	
	/** The color of the head*/
	private final Color color;
	
	/**
	 * Creates a snake head tile
	 * 
	 * @param color
	 * 			the color of the head
	 */
	
	public SnakeHeadTile(final Color color){
		this.color=color;
	}
	
	/**
	 * * Draws itself in a given graphics context, position and size.
	 * 
	 * @param g
	 *            graphics context to draw on.
	 * @param x
	 *            pixel x coordinate of the tile to be drawn.
	 * @param y
	 *            pixel y coordinate of the tile to be drawn.
	 * @param d
	 *            size of the head in pixels 
	 */
	
	public void draw(Graphics g, final int x, final int y, Dimension d){
		g.setColor(this.color);
		int[] xCoord={x+d.width/2,x, x+d.width};
		int[] yCoord={y,y+d.height,y+d.height};
		g.fillPolygon(xCoord, yCoord, 3);
	}
	
	

}
