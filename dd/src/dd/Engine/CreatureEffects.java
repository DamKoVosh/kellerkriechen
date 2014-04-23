package dd.Engine;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dd.Creature.Creature;
import dd.Encounter.StatusEffects.StatusEffect;
import dd.Engine.CreatureEbents.Listener.EndTurnListener;
import dd.Engine.CreatureEbents.Listener.StartTurnListener;
import dd.Engine.Durations.Effect;
import dd.Engine.Durations.UntilEndOfNextTurn;
import dd.Engine.Durations.UntilEndOfThisTurn;
import dd.Engine.Durations.UntilStartOfNextTurn;

public class CreatureEffects {
	
	public static List<Effect> effects = new LinkedList<Effect>();
	
	static {
		CreatureEvents.addStartTurnListener(new StartTurnListener() {
			
			@Override
			public void startTurn(Creature c) {
				
				Iterator<Effect> it = effects.listIterator();
				
				Effect effect;
				while (it.hasNext()) {
					effect = it.next();
					
					if (effect.duration instanceof UntilStartOfNextTurn) {
						if (((UntilStartOfNextTurn) effect.duration).turner == c) {
							Engine.log("Effect %s has expired on %s",
									effect.effect.getDescription(),
									effect.creature.getName());
							
							it.remove();
						}
					} else if (effect.duration instanceof UntilEndOfNextTurn) {
						if (((UntilEndOfNextTurn) effect.duration).turner == c) {
							((UntilEndOfNextTurn) effect.duration).next = true;
						}
					}
					
				}
				
			}
			
		});
		
		CreatureEvents.addEndTurnListener(new EndTurnListener() {
			
			@Override
			public void endTurn(Creature c) {
				Iterator<Effect> it = effects.listIterator();
				
				Effect effect;
				while (it.hasNext()) {
					effect = it.next();
					
					if (effect.duration instanceof UntilEndOfNextTurn) {
						if (((UntilEndOfNextTurn) effect.duration).next) {
							if (((UntilEndOfNextTurn) effect.duration).turner == c) {
								Engine.log("Effect %s has expired on %s",
										effect.effect.getDescription(),
										effect.creature.getName());
								
								it.remove();
							}
						}
					}
					
					if (effect.duration instanceof UntilEndOfThisTurn) {
						if (((UntilEndOfThisTurn)effect.duration).turner == c) {
							Engine.log("Effect %s has expired on %s",
									effect.effect.getDescription(),
									effect.creature.getName());
							
							it.remove();
						}
					}
					
				}
			}
		});
	}
	
	public static void addStatusEffect(Effect e) {
		effects.add(e);
	}
	
	public static void removeStatusEffect(StatusEffect se) {
		Iterator<Effect> it = effects.listIterator();
		
		Effect effect;
		while (it.hasNext()) {
			effect = it.next();
			if (se == effect.effect) {
				it.remove();
			}
		}
	}
	
	public static StatusEffect getStatusEffectOnCreature(Creature c,
			Class<?> effectType) {
		
		for (Effect effect : effects) {
			if (effectType.isInstance(effect.effect)) {
				if (effect.creature == c) {
					return effect.effect;
				}
			}
		}
		
		return null;
	}
	
	public static boolean hasStatusEffect(Creature c, Class<?> effectType) {
		return getStatusEffectOnCreature(c, effectType) != null;
	}
	
}
