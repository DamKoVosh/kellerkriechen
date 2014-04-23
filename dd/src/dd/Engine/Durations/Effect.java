package dd.Engine.Durations;

import dd.Creature.Creature;
import dd.Encounter.StatusEffects.StatusEffect;

public class Effect {
	
	public Creature creature;
	public StatusEffect effect;
	public Duration duration;
	
	public Effect(Creature c, StatusEffect e, Duration d) {
		this.creature = c;
		this.effect = e;
		this.duration = d;
	}
}
