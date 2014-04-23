package dd.Movement;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dd.Engine.Engine;
import dd.MapObjects.FreeTerrain;

public class PathFinding {
	private static class PathPoint {
		public PathPoint(Point p, PathPoint previous) {
			this.p = p;
			this.previous = previous;
		}
		
		public Point p;
		public PathPoint previous;
	}
	
	public static List<Point> findShortestPath(Point start, Point end) {
		Queue<PathPoint> toCheck = new LinkedList<PathPoint>();
		List<Point> alreadyChecked = new LinkedList<Point>();
		List<Point> path = new LinkedList<Point>();
		
		toCheck.add(new PathPoint(start, null));
		
		PathPoint checkPoint;
		
		while (!toCheck.isEmpty()) {
			
			checkPoint = toCheck.remove();
			
			if (checkPoint.p.equals(end)) {
				PathPoint pp = checkPoint;
				
				while (pp != null) {
					path.add(0, pp.p);
					
					pp = pp.previous;
				}
				
				/*
				Engine.log("Path from (%d,%d) to (%d,%d):", start.x, start.y,
						end.x, end.y);
				for (Point a : path) {
					Engine.log("(%d,%d)", a.x, a.y);
				}*/
				path.remove(0);
				
				return path;
				
			}
			
			for (Point newPoint : Engine.getAdjacentSquares(checkPoint.p)) {
				
				if (!(Engine.getBoard().getObjectAtPosition(newPoint) instanceof FreeTerrain)) {
					continue;
				}
				if (alreadyChecked.contains(newPoint)) {
					continue;
				}
				
				alreadyChecked.add(newPoint);
				toCheck.add(new PathPoint(newPoint, checkPoint));
			}
		}
		
		return null;
	}
}
