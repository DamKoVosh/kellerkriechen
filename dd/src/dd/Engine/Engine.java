package dd.Engine;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import dd.Board;
import dd.Actions.Default.Walk;
import dd.Creature.Creature;
import dd.Encounter.Encounter;
import dd.Movement.Move;
import dd.UI.Window;

public class Engine {
	
	private static Window w = null;
	private static Board board;
	
	public static void init() {
		try {
			Class.forName(Walk.class.getName());
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void log(String text, Object... args) {
		log(String.format(text, args));
	}
	
	public static void log(String text) {
		if (w == null) {
			System.out.println(text);
		} else {
			w.log(text);
		}
		
	}
	
	public static void setWindow(Window w2) {
		w = w2;
	}
	
	public static Window getWindow() {
		return w;
	}
	
	public static Board getBoard() {
		return board;
	}
	
	public static int getDistance(Point newPos, Point oldPos) {
		return Math.max(Math.abs(newPos.x - oldPos.x),
				Math.abs(newPos.y - oldPos.y));
	}
	
	public static int getSquareEnterSteps(Point newPos, Move move) {
		return 1;
	}
	
	public static boolean isSquareBlocked(Point newPos) {
		return !w.getMap().isUnoccupied(newPos.x, newPos.y);
	}
	
	public static void setBoard(Board b) {
		board = b;
	}
	
	public static List<Point> getAdjacentSquares(Point p) {
		List<Point> l = new LinkedList<Point>();
		
		l.add(new Point(p.x - 1, p.y - 1));
		l.add(new Point(p.x, p.y - 1));
		l.add(new Point(p.x + 1, p.y - 1));
		
		l.add(new Point(p.x - 1, p.y));
		l.add(new Point(p.x + 1, p.y));
		
		l.add(new Point(p.x - 1, p.y + 1));
		l.add(new Point(p.x, p.y + 1));
		l.add(new Point(p.x + 1, p.y + 1));
		return l;
	}
	
	public static int getCreatureDistance(Creature c1, Creature c2) {
		return getDistance(c1.getPosition(), c2.getPosition());
	}
	
	public static void removeCreature(Creature creature) {
		board.removeObject(creature);
		Encounter.getCurrentEncounter().removeCreature(creature);
	}
}
