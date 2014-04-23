package dd.Encounter.StatusEffects;

import dd.Creature.ActionSet;
import dd.Creature.Creature;
import dd.Engine.CreatureEffects;
import dd.Engine.CreatureEvents;
import dd.Engine.Engine;
import dd.Engine.CreatureEbents.Listener.GiveActionListener;

public class Stunned extends StatusEffect {
	
	static {
		CreatureEvents.addGiveActionListener(new GiveActionListener() {
			
			@Override
			public ActionSet calculateActions(Creature c, ActionSet as) {
				StatusEffect e;
				e = CreatureEffects.getStatusEffectOnCreature(c, Stunned.class);
				if (!(e instanceof Stunned)) {
					return as;
				}
				
				Engine.log("%s is stunned and can take no actions", c.getName());
				
				return new ActionSet(0, 0, 0);
			}
			
		});
	}
	
	@Override
	public String getDescription() {
		return "stunned";
	}
	
}
