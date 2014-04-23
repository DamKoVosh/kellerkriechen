package dd.MapObjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;


public abstract class MapObject {
	
	private Point position;
	
	public MapObject(int x, int y) {
		this.position = new Point(x, y);
	}
	
	

	public Point getPosition() {
		return position;
	}

	public void setPosition(int x, int y) {
		this.position = new Point(x,y);
	}

	public void draw(Graphics g) {
	}
	
	
}
