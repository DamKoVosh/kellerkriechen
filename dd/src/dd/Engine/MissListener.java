package dd.Engine;

import dd.Actions.Attack.Attack;
import dd.Creature.Creature;

public interface MissListener {

	void miss(Attack attack, Creature target);
	
}
