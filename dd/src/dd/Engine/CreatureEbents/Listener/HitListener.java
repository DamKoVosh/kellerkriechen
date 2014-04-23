package dd.Engine.CreatureEbents.Listener;

import dd.Actions.Attack.Attack;
import dd.Creature.Creature;

public interface HitListener {

	void hit(Attack attack, Creature target);
}
