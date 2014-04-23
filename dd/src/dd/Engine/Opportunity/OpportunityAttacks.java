package dd.Engine.Opportunity;

import java.awt.Point;
import java.util.List;

import dd.Actions.Default.Action;
import dd.Creature.Creature;
import dd.Encounter.Encounter;
import dd.Engine.CreatureEvents;
import dd.Engine.Engine;
import dd.Engine.CreatureEbents.Listener.LeaveSquareListener;
import dd.Movement.Move;

public class OpportunityAttacks {
	
	static {
		CreatureEvents.addLeaveSquareListener(new LeaveSquareListener() {
			
			@Override
			public void leaveSquare(Creature c, Point position, Move move) {
				OpportunityAttacks.leaveSquare(c, position, move);
			}
		});
	}
	
	private static void leaveSquare(Creature c, Point position, Move move) {
		if (!(move instanceof Move)) {
			return;
		}
		
		for (Creature enemy : Encounter.getCurrentEncounter().combatants) {
			if (!c.isEnemyOf(enemy))
				continue;
			
			if (Engine.getCreatureDistance(enemy, c) > 1)
				continue;
			
			offerOpportunityAttack(enemy, c);
		}
		
	}
	
	private static void offerOpportunityAttack(Creature attacker,
			Creature target) {
		
		List<Action> actions;
		
		actions = attacker.getOpportunityActions();
		
		for (Action act : actions) {
			
		}
		
	}
	
}
