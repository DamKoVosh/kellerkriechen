package dd.MapTree;

import dd.MapObjects.MapObject;


public class MapTreeWare extends MapTreeObject {
	private boolean vertical;
	private int position;
	private MapTreeObject partA;
	private MapTreeObject partB;
	public MapTreeWare(boolean vertical, int position, MapTreeObject partA, MapTreeObject partB) {
		this.vertical  = vertical;
		this.position = position;
		this.partA = partA;
		this.partB = partB;
	}

	public boolean isVertical() {
		return vertical;
	}

	public int getPosition() {
		return position;
	}


	public MapTreeObject getPartA() {
		return partA;
	}


	public MapTreeObject getPartB() {
		return partB;
	}

	@Override
	public void addObject(MapObject obj) {
		int val;
		val = vertical ? obj.getPosition().x : obj.getPosition().y;
		if (val < position) {
			partA.addObject(obj);
		} else {
			partB.addObject(obj);
		}
	}

	@Override
	public void removeObject(MapObject obj) {
		int val;
		val = vertical ? obj.getPosition().x : obj.getPosition().y;
		if (val < position) {
			partA.removeObject(obj);
		} else {
			partB.removeObject(obj);
		}
		
	}

	
}
