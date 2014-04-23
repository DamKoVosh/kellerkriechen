package dd.Movement;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import dd.Actions.Default.Action;
import dd.Creature.Creature;
import dd.Engine.CreatureEvents;
import dd.Engine.Engine;

public class Move {
	
	public int remainingSteps;
	private Action action;
	
	
	
	public void move(Creature mover, Creature controller, int steps,
			Action action) {
		remainingSteps = steps;
		continuedMove(mover, controller);
		
		this.action = action;
		
		Engine.log("%s finished moving", mover.getName());
	}
	
	private void continuedMove(Creature c, Creature controller) {
		
		Point newPos;
		Point oldPos;
		int dist;
		int reqSteps;
		boolean blocked;
		
		while (remainingSteps > 0) {
			newPos = controller.intelligence.getNextMoveStep(this, c);
			oldPos = c.getPosition();
			
			if (newPos == null) {
				break;
			}
			
			dist = Engine.getDistance(newPos, oldPos);
			reqSteps = Engine.getSquareEnterSteps(newPos, this);
			blocked = Engine.isSquareBlocked(newPos);
			
			if (dist != 1) {
				Engine.log("Step too far away!");
				break;
			}
			if (reqSteps > remainingSteps) {
				Engine.log(
						"Square requires %d steps to enter but only %d remaining",
						reqSteps, remainingSteps);
				break;
			}
			if (blocked) {
				Engine.log("Square is occupied");
				break;
			}
			
			CreatureEvents.eventLeaveSquare(c, c.getPosition(), this);
			
			dist = Engine.getDistance(newPos, oldPos);
			reqSteps = Engine.getSquareEnterSteps(newPos, this);
			blocked = Engine.isSquareBlocked(newPos);
			
			if (dist != 1) {
				Engine.log("Step too far away!");
				continue;
			}
			if (reqSteps > remainingSteps) {
				Engine.log(
						"Square requires %d steps to enter but only %d remaining",
						reqSteps, remainingSteps);
				continue;
			}
			if (blocked) {
				Engine.log("Square is occupied");
				continue;
			}
			if (!c.canDoAction(action)) {
				Engine.log("%s no longer possible", action.getDescription());
				break;
			}
			
			reposition(c, newPos);
			
			CreatureEvents.eventEnterSquare(c, newPos, this);
			
			remainingSteps -= reqSteps;
			
			//Engine.log("Moved. %d steps left", remainingSteps);
			
			if (!c.canDoAction(action)) {
				Engine.log("%s no longer possible", action.getDescription());
				break;
			}
			
		}
		
	}
	
	private void reposition(Creature c, Point newPosition) {
		
		Engine.getBoard().updatePosition(c, newPosition.x, newPosition.y);
		// c.setPosition(newPosition.x, newPosition.y);
	}
	
	
	
	
}
