package dd.Engine;

import dd.Creature.Creature;
import dd.Creature.Creature.DamageType;

public interface TakeDamageListener {

	void takeDamage(Creature creature, int amount, DamageType type, Creature damager);
	
}
