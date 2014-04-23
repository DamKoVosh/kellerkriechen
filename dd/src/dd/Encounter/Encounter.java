package dd.Encounter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import dd.Creature.Creature;
import dd.Engine.Engine;

public class Encounter {
	
	public List<Creature> combatants = new ArrayList<Creature>();
	private List<InitiativeEntry> iniList;
	
	private IniComparator iniComparator = new IniComparator();
	
	private Creature currentTurnActor = null;
	
	private static Encounter currentEnc = null;
	
	public static Encounter getCurrentEncounter() {
		return currentEnc;
	}
	
	public void removeCreature(Creature c) {
		combatants.remove(c);
	}
	
	public void start() {
		currentEnc = this;
		
		for (Creature c : combatants) {
			c.takePartInEncounter(this);
		}
		
		rollInitiative();
		
		Boolean end = false;
		while (!end) {
			for (InitiativeEntry e : iniList) {
				if (encounterOver()) {
					end = true;
					break;
				}
				
				Creature c = e.creature;
				if (!combatants.contains(c))
					continue;
				
				Engine.log("It's now %s's turn", c.getName());
				currentTurnActor = c;
				
				c.takeTurn();
				Engine.getWindow().updateMap();
				
				
			}
		}
		Engine.log("The encounter is over!");
		
	}
	
	private boolean encounterOver() {
		
		List<Creature> checkList;
		checkList = new LinkedList<Creature>(combatants);
		
		Creature checker;
		while (!checkList.isEmpty()) {
			checker = checkList.get(0);
			checkList.remove(0);
			
			for (Creature aite : checkList) {
				if (checker.isEnemyOf(aite)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	private void rollInitiative() {
		
		Engine.log("Rolling initiative");
		
		int roll, base, ini;
		iniList = new ArrayList<InitiativeEntry>();
		
		for (Creature c : combatants) {
			roll = Roll.d20();
			base = c.getInitiative();
			ini = base + roll;
			
			Engine.log("%s rolled %d initiative (%d + %d base)", c.getName(),
					ini, roll, base);
			
			iniList.add(new InitiativeEntry(ini, c));
			
			// CreatureEffects.addStatusEffect(new Effect(c, new Stunned(), new
			// UntilEndOfNextTurn(c)));
		}
		
		Collections.sort(iniList, iniComparator);
		Collections.reverse(iniList);
	}
	
	public Creature getCurrentTurnActor() {
		return currentTurnActor;
	}
	
}
