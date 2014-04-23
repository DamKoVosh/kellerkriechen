package dd.MapObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;


public class BlockingTerrain extends MapObject {
	
	public BlockingTerrain(int x, int y) {
		super(x,y);
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(new Color(127, 127, 127));
		g.fillRect(0, 0, 50, 50);
	}
	
}
