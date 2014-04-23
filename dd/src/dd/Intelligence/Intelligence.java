package dd.Intelligence;

import java.awt.Point;
import java.util.List;

import dd.Actions.Attack.Attack;
import dd.Creature.Creature;
import dd.Movement.Move;

public interface Intelligence {
	
	public void takeActions(Creature c);

	public Point getNextMoveStep(Move move, Creature c);

	public List<Creature> selectTargets(Attack attack, int maxTargets);

	public boolean takeReaction();
	
}
