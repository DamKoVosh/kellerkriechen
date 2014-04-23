package dd.Intelligence;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import dd.Actions.Attack.Attack;
import dd.Actions.Attack.MeleeAttack;
import dd.Actions.Default.Action;
import dd.Actions.Default.Walk;
import dd.Creature.Creature;
import dd.Encounter.Encounter;
import dd.Engine.Engine;
import dd.Movement.Move;
import dd.Movement.PathFinding;

public class MeleeAI implements Intelligence {
	
	Creature creature;
	
	List<Point> movePath;
	
	@Override
	public void takeActions(Creature c) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		creature = c;
		
		if (!hasAdjacentEnemy()) {
			moveAdjacent();
		}
		
		if (hasAdjacentEnemy()) {
			attackAdjacentEnemy();
		}
		
	}
	
	private void moveAdjacent() {
		Action act;
		act = getMoveAction();
		
		List<Point> path = null;
		List<Point> pathFound = null;
		boolean isPathFound = false;
		
		if (act != null) {
			
			for (Creature e : creature.getEnemies()) {
				for (Point adjSquare : Engine.getAdjacentSquares(e
						.getPosition())) {
					path = PathFinding.findShortestPath(creature.getPosition(),
							adjSquare);
					
					if (path != null) {
						if (path.size() <= creature.getSpeed()) {
							
							if (isPathFound) {
								if (path.size() < pathFound.size()) {
									pathFound = path;
								}
							} else {
								pathFound = path;
								isPathFound = true;
							}
							
						} else {
							path = null;
						}
					}
					
				}
				
				if (isPathFound) {
					break;
				}
			}
			if (isPathFound) {
				this.movePath = pathFound;
				
				act.perform();
			}
			
		}
	}
	
	private void attackAdjacentEnemy() {
		Action act;
		act = getMeleeAttackAction();
		if (act != null) {
			act.perform();
		}
	}
	
	private Action getMoveAction() {
		List<Action> actions = creature.getPerformableActions();
		
		for (Action act : actions) {
			if (act instanceof Walk) {
				return act;
			}
		}
		
		return null;
	}
	
	private Action getMeleeAttackAction() {
		List<Action> actions = creature.getPerformableActions();
		
		for (Action act : actions) {
			if (act instanceof MeleeAttack) {
				return act;
			}
		}
		
		return null;
	}
	
	private boolean hasAdjacentEnemy() {
		
		for (Creature c : Encounter.getCurrentEncounter().combatants) {
			if (creature.isEnemyOf(c)) {
				if (Engine.getCreatureDistance(creature, c) <= 1) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public Point getNextMoveStep(Move move, Creature c) {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (movePath != null && movePath.size() > 0) {
			Point p;
			p = movePath.get(0);
			movePath.remove(0);
			
			return p;
		}
		return null;
	}
	
	@Override
	public List<Creature> selectTargets(Attack attack, int maxTargets) {
		List<Creature> targets = new LinkedList<Creature>();
		
		for (Creature c : Encounter.getCurrentEncounter().combatants) {
			if (targets.size() > maxTargets)
				break;
			
			if (!c.isEnemyOf(creature))
				continue;
			
			if (attack.isValidTarget(c)) {
				targets.add(c);
			}
		}
		
		return targets;
	}
	
	@Override
	public boolean takeReaction() {
		return true;
	}
	
}
