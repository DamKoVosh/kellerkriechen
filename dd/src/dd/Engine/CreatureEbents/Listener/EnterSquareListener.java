package dd.Engine.CreatureEbents.Listener;

import java.awt.Point;

import dd.Creature.Creature;
import dd.Movement.Move;

public interface EnterSquareListener {

	void enterSquare(Creature c, Point newPosition, Move move);
	
}
