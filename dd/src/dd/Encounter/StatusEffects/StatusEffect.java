package dd.Encounter.StatusEffects;

import dd.Creature.Creature;

public abstract class StatusEffect {
	
	
	
	public static void applyUntilEndOfNextTurn(StatusEffect effect, Creature turner,
			Creature target, Creature source) {
		
		
	}
	
	public abstract String getDescription();
	
}
