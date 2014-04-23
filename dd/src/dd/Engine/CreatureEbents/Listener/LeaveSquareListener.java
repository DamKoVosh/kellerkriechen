package dd.Engine.CreatureEbents.Listener;

import java.awt.Point;

import dd.Creature.Creature;
import dd.Movement.Move;

public interface LeaveSquareListener {

	void leaveSquare(Creature c, Point position, Move move);
	
}
