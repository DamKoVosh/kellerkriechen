package dd;

import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import dd.MapObjects.BlockingTerrain;
import dd.MapObjects.FreeTerrain;
import dd.MapObjects.MapObject;
import dd.MapTree.MapTreeHa;
import dd.MapTree.MapTreeObject;
import dd.MapTree.MapTreeWare;

public class Board {
	public MapTreeObject objectTree;
	
	private List<MapObject> objects = new LinkedList<MapObject>();
	
	public Board() {
		
		List<MapObject> blocks = new LinkedList<MapObject>();
		
		for (int i = 0; i < 10; i++) {
			blocks.add(new BlockingTerrain(i, 0));
		}
		
		for (int i = 0; i < 10; i++) {
			blocks.add(new BlockingTerrain(0, i + 1));
		}
		
		for (int i = 0; i < 10; i++) {
			blocks.add(new BlockingTerrain(i + 1, 10));
		}
		
		for (int i = 0; i < 10; i++) {
			blocks.add(new BlockingTerrain(10, i));
		}
		objects.addAll(blocks);
		
		createTree();
		
	}
	
	public MapObject getObjectAtPosition(Point p) {
		return getObjectAtCoordinates(p.x, p.y);
	}
	
	public MapObject getObjectAtCoordinates(int x, int y) {
		return getObjectAtCoordinatesSub(x, y, objectTree);
	}
	
	private MapObject getObjectAtCoordinatesSub(int x, int y,
			MapTreeObject subTree) {
		if (subTree instanceof MapTreeHa) {
			for (MapObject obj : ((MapTreeHa) subTree).getObjects()) {
				if (obj.getPosition().x == x && obj.getPosition().y == y) {
					return obj;
				}
			}
			return new FreeTerrain(x, y);
		} else if (subTree instanceof MapTreeWare) {
			MapTreeWare w = (MapTreeWare) subTree;
			if ((w.isVertical() ? x : y) < w.getPosition()) {
				return getObjectAtCoordinatesSub(x, y, w.getPartA());
			} else {
				return getObjectAtCoordinatesSub(x, y, w.getPartB());
			}
		}
		
		return null;
	}
	
	private void createTree() {
		
		this.objectTree = createTreeSub(objects, false, false);
	}
	
	private MapTreeObject createTreeSub(List<MapObject> objects,
			boolean vertical, boolean noChange) {
		if (objects.size() <= 3) {
			return new MapTreeHa(objects);
		}
		
		List<Integer> values = new LinkedList<Integer>();
		
		for (MapObject object : objects) {
			values.add((int) (vertical ? object.getPosition().getX() : object
					.getPosition().getY()));
		}
		Collections.sort(values);
		
		int breakValue = values.get(values.size() / 2);
		
		if (breakValue == values.get(0))
			breakValue++;
		
		List<MapObject> partA = new LinkedList<MapObject>();
		List<MapObject> partB = new LinkedList<MapObject>();
		
		for (MapObject object : objects) {
			if ((int) (vertical ? object.getPosition().getX() : object
					.getPosition().getY()) < breakValue) {
				partA.add(object);
			} else {
				partB.add(object);
			}
		}
		
		if (partA.size() == 0) {
			if (!noChange) {
				return createTreeSub(partB, !vertical, true);
			} else {
				System.out.println("hen yo!");
				return new MapTreeHa(partB);
			}
		} else if (partB.size() == 0) {
			if (!noChange) {
				return createTreeSub(partA, !vertical, true);
			} else {
				System.out.println("zettai hen!");
				return new MapTreeHa(partA);
			}
		} else {
			
			System.out.println((vertical ? "vertical" : "horizontal")
					+ " split at " + breakValue);
			
			MapTreeObject subA = createTreeSub(partA, !vertical, false);
			MapTreeObject subB = createTreeSub(partB, !vertical, false);
			
			return new MapTreeWare(vertical, breakValue, subA, subB);
		}
		
	}
	
	public MapTreeObject getTree() {
		return objectTree;
	}
	
	public void addObject(MapObject obj) {
		objects.add(obj);
		objectTree.addObject(obj);
	}
	
	public void removeObject(MapObject obj) {
		objects.remove(obj);
		objectTree.removeObject(obj);
	}
	
	public void updatePosition(MapObject obj, int newX, int newY) {
		objectTree.removeObject(obj);
		obj.setPosition(newX, newY);
		objectTree.addObject(obj);
		
		createTree(); // great fun!
	}
	
}
